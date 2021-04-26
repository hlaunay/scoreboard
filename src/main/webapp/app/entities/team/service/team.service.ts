import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeam, getTeamIdentifier, IScore } from '../team.model';

export type EntityResponseType = HttpResponse<ITeam>;
export type EntityArrayResponseType = HttpResponse<ITeam[]>;

@Injectable({ providedIn: 'root' })
export class TeamService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/teams');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeam[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getScores(): Observable<HttpResponse<IScore[]>> {
    return this.http.get<IScore[]>(`${this.resourceUrl}/ranking`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeamToCollectionIfMissing(teamCollection: ITeam[], ...teamsToCheck: (ITeam | null | undefined)[]): ITeam[] {
    const teams: ITeam[] = teamsToCheck.filter(isPresent);
    if (teams.length > 0) {
      const teamCollectionIdentifiers = teamCollection.map(teamItem => getTeamIdentifier(teamItem)!);
      const teamsToAdd = teams.filter(teamItem => {
        const teamIdentifier = getTeamIdentifier(teamItem);
        if (teamIdentifier == null || teamCollectionIdentifiers.includes(teamIdentifier)) {
          return false;
        }
        teamCollectionIdentifiers.push(teamIdentifier);
        return true;
      });
      return [...teamsToAdd, ...teamCollection];
    }
    return teamCollection;
  }
}
