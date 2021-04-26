export interface IAnswerSubmit {
    challengeId: number;
    answer: string;
  }
  
  export class AnswerSubmit implements IAnswerSubmit {
    constructor(public challengeId: number, public answer: string) {}
  }