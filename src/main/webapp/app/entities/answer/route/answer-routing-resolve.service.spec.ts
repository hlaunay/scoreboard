jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAnswer, Answer } from '../answer.model';
import { AnswerService } from '../service/answer.service';

import { AnswerRoutingResolveService } from './answer-routing-resolve.service';

describe('Service Tests', () => {
  describe('Answer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AnswerRoutingResolveService;
    let service: AnswerService;
    let resultAnswer: IAnswer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AnswerRoutingResolveService);
      service = TestBed.inject(AnswerService);
      resultAnswer = undefined;
    });

    describe('resolve', () => {
      it('should return IAnswer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnswer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAnswer).toEqual({ id: 123 });
      });

      it('should return new IAnswer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnswer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAnswer).toEqual(new Answer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnswer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAnswer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
