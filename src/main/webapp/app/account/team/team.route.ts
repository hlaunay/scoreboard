import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamComponent } from './team.component';

export const teamRoute: Route = {
  path: 'team',
  component: TeamComponent,
  data: {
    pageTitle: 'Team',
    teamManagement: true,
  },
  canActivate: [UserRouteAccessService],
};
