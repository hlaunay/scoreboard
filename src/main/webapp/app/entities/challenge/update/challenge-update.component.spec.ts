jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChallengeService } from '../service/challenge.service';
import { IChallenge, Challenge } from '../challenge.model';

import { ChallengeUpdateComponent } from './challenge-update.component';

describe('Component Tests', () => {
  describe('Challenge Management Update Component', () => {
    let comp: ChallengeUpdateComponent;
    let fixture: ComponentFixture<ChallengeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let challengeService: ChallengeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChallengeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChallengeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChallengeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      challengeService = TestBed.inject(ChallengeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const challenge: IChallenge = { id: 456 };

        activatedRoute.data = of({ challenge });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(challenge));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const challenge = { id: 123 };
        spyOn(challengeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ challenge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: challenge }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(challengeService.update).toHaveBeenCalledWith(challenge);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const challenge = new Challenge();
        spyOn(challengeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ challenge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: challenge }));
        saveSubject.complete();

        // THEN
        expect(challengeService.create).toHaveBeenCalledWith(challenge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const challenge = { id: 123 };
        spyOn(challengeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ challenge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(challengeService.update).toHaveBeenCalledWith(challenge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
