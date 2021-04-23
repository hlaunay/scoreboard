import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AnswerService } from '../service/answer.service';

import { AnswerComponent } from './answer.component';

describe('Component Tests', () => {
  describe('Answer Management Component', () => {
    let comp: AnswerComponent;
    let fixture: ComponentFixture<AnswerComponent>;
    let service: AnswerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnswerComponent],
      })
        .overrideTemplate(AnswerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnswerComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AnswerService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.answers?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
