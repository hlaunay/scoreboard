import { Component, OnInit   } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TeamJoinService } from './team-join.service';
import { TeamJoin } from './team-join.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
    selector: 'jhi-team-join',
    templateUrl: './team-join.component.html',
  })
  export class TeamJoinComponent implements OnInit {
    doNotMatch = false;
    isSaving = false;

    teams: ITeam[] = [];

    createForm = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      password: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
      confirmPassword: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    });

    joinForm = this.fb.group({
      id: [],
      name: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      password: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    });
  
    constructor(protected teamService: TeamService, protected teamJoinService: TeamJoinService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder, private router: Router, private accountService: AccountService) {}
  
    ngOnInit(): void {
      this.teamService.query({page: 0, size: 1000}).subscribe((res: HttpResponse<ITeam[]>) => {
        this.teams = res.body ?? [];
      });
    }

    create(): void {
      this.doNotMatch = false;
      
      const newPassword = this.createForm.get(['password'])!.value;
      if(newPassword !== this.createForm.get(['confirmPassword'])!.value) {
        this.doNotMatch = true;
        return;
      }

      this.isSaving = true;
      const team = this.getFromForm(this.createForm);
      this.subscribeToCreateResponse(this.teamJoinService.create(team));
    }

    join(): void {
      this.isSaving = true;
      const team = this.getFromForm(this.joinForm);
      this.subscribeToJoinResponse(this.teamJoinService.join(team));
    }
  
    protected subscribeToCreateResponse(result: Observable<HttpResponse<{}>>): void {
      result.pipe(finalize(() => this.onFinalize())).subscribe(
        () => this.onSuccess(),
        () => this.onError()
      );
    }

    protected subscribeToJoinResponse(result: Observable<{}>): void {
      result.pipe(finalize(() => this.onFinalize())).subscribe(
        () => this.onSuccess(),
        () => this.onError()
      )
    }
  
    protected onSuccess(): void {
      this.accountService.identity(true).subscribe(() => {this.router.navigate(['']); return;});
    }
  
    protected onError(): void {
      // Api for inheritance.
    }
  
    protected onFinalize(): void {
      this.isSaving = false;
    }
  
    protected getFromForm(form: FormGroup): TeamJoin {
      return new TeamJoin(
        form.get(['name'])!.value, 
        form.get(['password'])!.value
      );
    }
  } 