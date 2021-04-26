import { HttpResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { IAnswer } from "app/entities/answer/answer.model";
import { IChallenge } from "app/entities/challenge/challenge.model";
import { ChallengeService } from "app/entities/challenge/service/challenge.service";
import { PlayService } from "./play.service";

@Component({
    selector: 'jhi-play',
    templateUrl: './play.component.html',
    styleUrls: ['./play.component.scss'],
})
export class PlayComponent implements OnInit {

    challenges: IChallenge[] = [];
    answers: IAnswer[] = [];

    constructor(private challengeService: ChallengeService, private playService: PlayService) {}

    ngOnInit(): void {
        this.challengeService.query().subscribe((res: HttpResponse<IChallenge[]>) => {
            this.challenges = res.body ?? [];

            this.playService.query().subscribe((pRes: HttpResponse<IAnswer[]>) => {
                this.answers = pRes.body ?? [];
            })
        })
    }

    getAnswer(challengeId?: number): IAnswer | undefined {
        return this.answers.find(answer => answer.challenge?.id === challengeId);
    }
}