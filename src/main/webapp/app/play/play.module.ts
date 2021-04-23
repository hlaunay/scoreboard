import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { SharedModule } from "app/shared/shared.module";
import { PlayComponent } from "./play.component";
import { PLAY_ROUTE } from "./play.route";

@NgModule({
    imports: [SharedModule, RouterModule.forChild([PLAY_ROUTE])],
    declarations: [PlayComponent],
  })
  export class PlayModule {}