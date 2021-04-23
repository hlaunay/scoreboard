package fr.sii.scoreboard.service.dto;

import fr.sii.scoreboard.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChallengeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChallengeDTO.class);
        ChallengeDTO challengeDTO1 = new ChallengeDTO();
        challengeDTO1.setId(1L);
        ChallengeDTO challengeDTO2 = new ChallengeDTO();
        assertThat(challengeDTO1).isNotEqualTo(challengeDTO2);
        challengeDTO2.setId(challengeDTO1.getId());
        assertThat(challengeDTO1).isEqualTo(challengeDTO2);
        challengeDTO2.setId(2L);
        assertThat(challengeDTO1).isNotEqualTo(challengeDTO2);
        challengeDTO1.setId(null);
        assertThat(challengeDTO1).isNotEqualTo(challengeDTO2);
    }
}
