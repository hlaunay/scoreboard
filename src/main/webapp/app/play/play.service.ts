import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { createRequestOption } from "app/core/request/request-util";
import { IAnswer } from "app/entities/answer/answer.model";
import * as dayjs from "dayjs";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { IAnswerSubmit } from "./play.model";

export type EntityResponseType = HttpResponse<IAnswer>;
export type EntityArrayResponseType = HttpResponse<IAnswer[]>;

@Injectable({ providedIn: 'root' })
export class PlayService {
    public resourceUrl = this.applicationConfigService.getEndpointFor('api/account/answers');

    constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) { }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    create(answer: IAnswerSubmit): Observable<EntityResponseType> {
        return this.http
            .post<IAnswer>(this.resourceUrl, answer, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
        res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((answer: IAnswer) => {
                answer.date = answer.date ? dayjs(answer.date) : undefined;
            });
        }
        return res;
    }
}