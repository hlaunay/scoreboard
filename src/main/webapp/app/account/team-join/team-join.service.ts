import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { TeamJoin } from "./team-join.model";

@Injectable({ providedIn: 'root' })
export class TeamJoinService {
    public resourceUrl = this.applicationConfigService.getEndpointFor('api/account/team');

    constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

    create(team: TeamJoin): Observable<HttpResponse<{}>> {
        return this.http.post<TeamJoin>(`${this.resourceUrl}/create`, team, { observe: 'response' });
    }

    join(team: TeamJoin): Observable<HttpResponse<{}>> {
        return this.http.put<TeamJoin>(`${this.resourceUrl}/join`, team, { observe: 'response'});
    }
}