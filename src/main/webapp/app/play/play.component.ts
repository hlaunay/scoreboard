import { HttpResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { IAnswer } from "app/entities/answer/answer.model";
import { IChallenge } from "app/entities/challenge/challenge.model";
import { ChallengeService } from "app/entities/challenge/service/challenge.service";
import { PlayService } from "./play.service";
import { AlertService } from 'app/core/util/alert.service';

@Component({
    selector: 'jhi-play',
    templateUrl: './play.component.html',
    styleUrls: ['./play.component.scss'],
})
export class PlayComponent implements OnInit {

    challenges: IChallenge[] = [];
    answers: IAnswer[] = [];

    constructor(private challengeService: ChallengeService, private playService: PlayService, private alertService: AlertService) {}

    ngOnInit(): void {
        this.challengeService.query().subscribe((res: HttpResponse<IChallenge[]>) => {
            this.challenges = res.body ?? [];

            this.refreshAnswers();
        })
    }

    getAnswer(challengeId?: number): IAnswer | undefined {
        return this.answers.find(answer => answer.challenge?.id === challengeId);
    }

    onValidated(challenge: IChallenge): void {
        this.refreshAnswers();

        this.alertService.addAlert({
            type: 'success',
            message: `${challenge.name ?? 'UNKNOWN'} successfuly validated.`
        })
    }

    onInvalid(challenge: IChallenge): void {
        this.alertService.addAlert({
            type: 'warning',
            message: `Invalid answer for ${challenge.name ?? 'UNKNOWN'}`
        })
    }

    protected refreshAnswers(): void {
        this.playService.query().subscribe((res: HttpResponse<IAnswer[]>) => {
            this.answers = res.body ?? [];
        })
    }
}