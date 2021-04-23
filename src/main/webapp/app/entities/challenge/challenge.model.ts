export interface IChallenge {
  id?: number;
  name?: string;
  description?: string | null;
  points?: number;
  answer?: string;
}

export class Challenge implements IChallenge {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public points?: number,
    public answer?: string
  ) {}
}

export function getChallengeIdentifier(challenge: IChallenge): number | undefined {
  return challenge.id;
}
