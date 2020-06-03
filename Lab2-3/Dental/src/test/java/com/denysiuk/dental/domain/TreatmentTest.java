package com.denysiuk.dental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.denysiuk.dental.web.rest.TestUtil;

public class TreatmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Treatment.class);
        Treatment treatment1 = new Treatment();
        treatment1.setId(1L);
        Treatment treatment2 = new Treatment();
        treatment2.setId(treatment1.getId());
        assertThat(treatment1).isEqualTo(treatment2);
        treatment2.setId(2L);
        assertThat(treatment1).isNotEqualTo(treatment2);
        treatment1.setId(null);
        assertThat(treatment1).isNotEqualTo(treatment2);
    }
}
