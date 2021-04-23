import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAnswer, Answer } from '../answer.model';
import { AnswerService } from '../service/answer.service';
import { IChallenge } from 'app/entities/challenge/challenge.model';
import { ChallengeService } from 'app/entities/challenge/service/challenge.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'jhi-answer-update',
  templateUrl: './answer-update.component.html',
})
export class AnswerUpdateComponent implements OnInit {
  isSaving = false;

  challengesSharedCollection: IChallenge[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    challenge: [null, Validators.required],
    team: [null, Validators.required],
  });

  constructor(
    protected answerService: AnswerService,
    protected challengeService: ChallengeService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answer }) => {
      if (answer.id === undefined) {
        const today = dayjs().startOf('day');
        answer.date = today;
      }

      this.updateForm(answer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answer = this.createFromForm();
    if (answer.id !== undefined) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  trackChallengeById(index: number, item: IChallenge): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(answer: IAnswer): void {
    this.editForm.patchValue({
      id: answer.id,
      date: answer.date ? answer.date.format(DATE_TIME_FORMAT) : null,
      challenge: answer.challenge,
      team: answer.team,
    });

    this.challengesSharedCollection = this.challengeService.addChallengeToCollectionIfMissing(
      this.challengesSharedCollection,
      answer.challenge
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, answer.team);
  }

  protected loadRelationshipsOptions(): void {
    this.challengeService
      .query()
      .pipe(map((res: HttpResponse<IChallenge[]>) => res.body ?? []))
      .pipe(
        map((challenges: IChallenge[]) =>
          this.challengeService.addChallengeToCollectionIfMissing(challenges, this.editForm.get('challenge')!.value)
        )
      )
      .subscribe((challenges: IChallenge[]) => (this.challengesSharedCollection = challenges));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): IAnswer {
    return {
      ...new Answer(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      challenge: this.editForm.get(['challenge'])!.value,
      team: this.editForm.get(['team'])!.value,
    };
  }
}
