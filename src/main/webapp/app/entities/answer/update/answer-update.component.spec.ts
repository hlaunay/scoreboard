jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnswerService } from '../service/answer.service';
import { IAnswer, Answer } from '../answer.model';
import { IChallenge } from 'app/entities/challenge/challenge.model';
import { ChallengeService } from 'app/entities/challenge/service/challenge.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { AnswerUpdateComponent } from './answer-update.component';

describe('Component Tests', () => {
  describe('Answer Management Update Component', () => {
    let comp: AnswerUpdateComponent;
    let fixture: ComponentFixture<AnswerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let answerService: AnswerService;
    let challengeService: ChallengeService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnswerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnswerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnswerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      answerService = TestBed.inject(AnswerService);
      challengeService = TestBed.inject(ChallengeService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Challenge query and add missing value', () => {
        const answer: IAnswer = { id: 456 };
        const challenge: IChallenge = { id: 29286 };
        answer.challenge = challenge;

        const challengeCollection: IChallenge[] = [{ id: 51862 }];
        spyOn(challengeService, 'query').and.returnValue(of(new HttpResponse({ body: challengeCollection })));
        const additionalChallenges = [challenge];
        const expectedCollection: IChallenge[] = [...additionalChallenges, ...challengeCollection];
        spyOn(challengeService, 'addChallengeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ answer });
        comp.ngOnInit();

        expect(challengeService.query).toHaveBeenCalled();
        expect(challengeService.addChallengeToCollectionIfMissing).toHaveBeenCalledWith(challengeCollection, ...additionalChallenges);
        expect(comp.challengesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const answer: IAnswer = { id: 456 };
        const team: ITeam = { id: 73612 };
        answer.team = team;

        const teamCollection: ITeam[] = [{ id: 92794 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ answer });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const answer: IAnswer = { id: 456 };
        const challenge: IChallenge = { id: 39343 };
        answer.challenge = challenge;
        const team: ITeam = { id: 83696 };
        answer.team = team;

        activatedRoute.data = of({ answer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(answer));
        expect(comp.challengesSharedCollection).toContain(challenge);
        expect(comp.teamsSharedCollection).toContain(team);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const answer = { id: 123 };
        spyOn(answerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ answer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: answer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(answerService.update).toHaveBeenCalledWith(answer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const answer = new Answer();
        spyOn(answerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ answer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: answer }));
        saveSubject.complete();

        // THEN
        expect(answerService.create).toHaveBeenCalledWith(answer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const answer = { id: 123 };
        spyOn(answerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ answer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(answerService.update).toHaveBeenCalledWith(answer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackChallengeById', () => {
        it('Should return tracked Challenge primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChallengeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTeamById', () => {
        it('Should return tracked Team primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
