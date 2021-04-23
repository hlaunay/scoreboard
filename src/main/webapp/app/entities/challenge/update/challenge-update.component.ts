import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IChallenge, Challenge } from '../challenge.model';
import { ChallengeService } from '../service/challenge.service';

@Component({
  selector: 'jhi-challenge-update',
  templateUrl: './challenge-update.component.html',
})
export class ChallengeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(100)]],
    description: [],
    points: [null, [Validators.required]],
    answer: [null, [Validators.required]],
  });

  constructor(protected challengeService: ChallengeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ challenge }) => {
      this.updateForm(challenge);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const challenge = this.createFromForm();
    if (challenge.id !== undefined) {
      this.subscribeToSaveResponse(this.challengeService.update(challenge));
    } else {
      this.subscribeToSaveResponse(this.challengeService.create(challenge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChallenge>>): void {
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

  protected updateForm(challenge: IChallenge): void {
    this.editForm.patchValue({
      id: challenge.id,
      name: challenge.name,
      description: challenge.description,
      points: challenge.points,
      answer: challenge.answer,
    });
  }

  protected createFromForm(): IChallenge {
    return {
      ...new Challenge(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      points: this.editForm.get(['points'])!.value,
      answer: this.editForm.get(['answer'])!.value,
    };
  }
}
