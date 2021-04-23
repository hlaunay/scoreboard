import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Authority } from 'app/config/authority.constants';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'team',
        data: {
          pageTitle: 'Teams',
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      {
        path: 'challenge',
        data: {
          pageTitle: 'Challenges',
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./challenge/challenge.module').then(m => m.ChallengeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
