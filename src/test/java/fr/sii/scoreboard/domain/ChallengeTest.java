package fr.sii.scoreboard.domain;

import fr.sii.scoreboard.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChallengeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Challenge.class);
        Challenge challenge1 = new Challenge();
        challenge1.setId(1L);
        Challenge challenge2 = new Challenge();
        challenge2.setId(challenge1.getId());
        assertThat(challenge1).isEqualTo(challenge2);
        challenge2.setId(2L);
        assertThat(challenge1).isNotEqualTo(challenge2);
        challenge1.setId(null);
        assertThat(challenge1).isNotEqualTo(challenge2);
    }
}
