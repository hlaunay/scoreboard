import * as dayjs from "dayjs";

export interface ITeam {
  id?: number;
  name?: string;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}

export interface IScore {
  position: number;
  team: string;
  points: number;
  firstAnswer: dayjs.Dayjs;
}

export class Score implements IScore {
  constructor(
    public position: number,
    public team: string,
    public points: number,
    public firstAnswer: dayjs.Dayjs
  ) {}
}