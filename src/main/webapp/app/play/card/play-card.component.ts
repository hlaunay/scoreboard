import { HttpResponse } from "@angular/common/http";
import { Component, Input } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { IAnswer } from "app/entities/answer/answer.model";
import { Challenge, IChallenge } from "app/entities/challenge/challenge.model";
import { Observable } from "rxjs";
import { finalize } from "rxjs/operators";
import { AnswerSubmit } from "../play.model";
import { PlayService } from "../play.service";

@Component({
    selector: 'jhi-play-card',
    templateUrl: './play-card.component.html',
    styleUrls: ['./play-card.component.scss'],
})
export class PlayCardComponent {
    
    @Input() challenge: IChallenge = new Challenge();

    @Input() answer: IAnswer | undefined;

    createForm = this.fb.group({
        answer: [null, [Validators.required]],
    });

    constructor (protected fb: FormBuilder, protected playService: PlayService) {}

    isNotResolved(): boolean {
        return this.answer === undefined;
    }

    getClasses(): string[] {
        if(this.isNotResolved()) {
            return ['card', 'h-100', 'bg-light', 'mb-3'];
        }

        return ['card', 'h-100', 'text-white', 'bg-success', 'mb-3'];
    }

    create(): void {
        const answer = this.getFromForm(this.createForm);
        this.subscribeToResponse(this.playService.create(answer));
      }
    
      protected subscribeToResponse(result: Observable<HttpResponse<{}>>): void {
        result.pipe(finalize(() => this.onFinalize())).subscribe(
          () => this.onSuccess(),
          () => this.onError()
        );
      }
    
      protected onSuccess(): void { 
        // TODO
      }
    
      protected onError(): void {
        // Api for inheritance.
      }
    
      protected onFinalize(): void { 
        // TODO
      }
    
      protected getFromForm(form: FormGroup): AnswerSubmit {
        return new AnswerSubmit(
          this.challenge.id ?? -1, 
          form.get(['answer'])!.value
        );
      }
}