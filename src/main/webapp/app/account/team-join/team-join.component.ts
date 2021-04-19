import { Component   } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TeamJoinService } from './team-join.service';
import { TeamCreate } from './team-join.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
    selector: 'jhi-team-join',
    templateUrl: './team-join.component.html',
  })
  export class TeamJoinComponent {
    doNotMatch = false;
    isSaving = false;

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
  
    constructor(protected teamJoinService: TeamJoinService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder, private router: Router, private accountService: AccountService) {}
  
    create(): void {
      this.doNotMatch = false;
      
      const newPassword = this.createForm.get(['password'])!.value;
      if(newPassword !== this.createForm.get(['confirmPassword'])!.value) {
        this.doNotMatch = true;
        return;
      }

      this.isSaving = true;
      const team = this.createFromForm();
      this.subscribeToCreateResponse(this.teamJoinService.create(team));
    }

    join(): void {
      // TODO
    }
  
    protected subscribeToCreateResponse(result: Observable<HttpResponse<{}>>): void {
      result.pipe(finalize(() => this.onFinalize())).subscribe(
        () => this.onSuccess(),
        () => this.onError()
      );
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
  
    protected createFromForm(): TeamCreate {
      return new TeamCreate(
        this.createForm.get(['name'])!.value, 
        this.createForm.get(['password'])!.value
      );
    }

  } 