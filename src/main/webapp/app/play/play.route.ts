import { Route } from '@angular/router';
import { PlayComponent } from './play.component';

export const PLAY_ROUTE: Route = {
    path: 'play',
    component: PlayComponent,
    data: {
      pageTitle: 'Play',
    },
  };