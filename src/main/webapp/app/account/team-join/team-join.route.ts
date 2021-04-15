import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamJoinComponent } from './team-join.component';

export const teamRoute: Route = {
  path: 'team-join',
  component: TeamJoinComponent,
  data: {
    pageTitle: 'Team',
    teamManagement: true,
  },
  canActivate: [UserRouteAccessService],
};
