import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITeam, Team } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
    selector: 'jhi-team-join',
    templateUrl: './team-join.component.html',
  })
  export class TeamJoinComponent implements OnInit {
    isSaving = false;

    editForm = this.fb.group({
      id: [],
      name: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      password: [null, [Validators.required, Validators.minLength(60), Validators.maxLength(60)]],
    });
  
    constructor(protected teamService: TeamService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}
  
    ngOnInit(): void {
      this.activatedRoute.data.subscribe(({ team }) => {
        this.updateForm(team);
      });
    }
  
    previousState(): void {
      window.history.back();
    }
  
    save(): void {
      this.isSaving = true;
      const team = this.createFromForm();
      if (team.id !== undefined) {
        this.subscribeToSaveResponse(this.teamService.update(team));
      } else {
        this.subscribeToSaveResponse(this.teamService.create(team));
      }
    }
  
    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
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
  
    protected updateForm(team: ITeam): void {
      this.editForm.patchValue({
        id: team.id,
        name: team.name,
        password: team.password,
      });
    }
  
    protected createFromForm(): ITeam {
      return {
        ...new Team(),
        id: this.editForm.get(['id'])!.value,
        name: this.editForm.get(['name'])!.value,
        password: this.editForm.get(['password'])!.value,
      };
    }
  } 