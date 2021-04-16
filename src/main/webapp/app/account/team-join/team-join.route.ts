import { Route } from '@angular/router';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamJoinComponent } from './team-join.component';

export const teamRoute: Route = {
  path: 'team-join',
  component: TeamJoinComponent,
  data: {
    pageTitle: 'Team',
    authorities: [Authority.NO_TEAM],
  },
  canActivate: [UserRouteAccessService],
};
