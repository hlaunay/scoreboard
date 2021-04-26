package fr.sii.scoreboard.service.dto;

import javax.validation.constraints.NotNull;

public class AnswerSubmitDTO {

    @NotNull
    private Long challengeId;

    @NotNull
    private String answer;

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerSubmitDTO that = (AnswerSubmitDTO) o;

        if (challengeId != null ? !challengeId.equals(that.challengeId) : that.challengeId != null) return false;
        return answer != null ? answer.equals(that.answer) : that.answer == null;
    }

    @Override
    public int hashCode() {
        int result = challengeId != null ? challengeId.hashCode() : 0;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnswerSubmitDTO{" +
            "challengeId=" + challengeId +
            ", answer='" + answer + '\'' +
            '}';
    }
}
