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
