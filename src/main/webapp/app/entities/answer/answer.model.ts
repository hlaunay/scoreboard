import * as dayjs from 'dayjs';
import { IChallenge } from 'app/entities/challenge/challenge.model';
import { ITeam } from 'app/entities/team/team.model';

export interface IAnswer {
  id?: number;
  date?: dayjs.Dayjs;
  challenge?: IChallenge;
  team?: ITeam;
}

export class Answer implements IAnswer {
  constructor(public id?: number, public date?: dayjs.Dayjs, public challenge?: IChallenge, public team?: ITeam) {}
}

export function getAnswerIdentifier(answer: IAnswer): number | undefined {
  return answer.id;
}
