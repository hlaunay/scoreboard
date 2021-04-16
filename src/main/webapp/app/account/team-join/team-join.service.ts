import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { TeamCreate } from "./team-join.model";

@Injectable({ providedIn: 'root' })
export class TeamJoinService {
    public resourceUrl = this.applicationConfigService.getEndpointFor('api/account/team');

    constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

    create(team: TeamCreate): Observable<HttpResponse<{}>> {
        return this.http.post<TeamCreate>(`${this.resourceUrl}/create`, team, { observe: 'response' });
    }
}