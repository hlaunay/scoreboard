import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IScore } from 'app/entities/team/team.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  scores: IScore[] = [];

  constructor(private accountService: AccountService, private router: Router, private teamService: TeamService) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.teamService.getScores().subscribe((res: HttpResponse<IScore[]>) => {
      this.scores = res.body ?? [];
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  getRowClasses(row: IScore): string[] {
    if(row.team === this.account?.team?.name) {
      return ['table-info'];
    }
    return [];
  }
}
