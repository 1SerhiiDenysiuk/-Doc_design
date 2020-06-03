package com.denysiuk.dental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.denysiuk.dental.web.rest.TestUtil;

public class ProcedureTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Procedure.class);
        Procedure procedure1 = new Procedure();
        procedure1.setId(1L);
        Procedure procedure2 = new Procedure();
        procedure2.setId(procedure1.getId());
        assertThat(procedure1).isEqualTo(procedure2);
        procedure2.setId(2L);
        assertThat(procedure1).isNotEqualTo(procedure2);
        procedure1.setId(null);
        assertThat(procedure1).isNotEqualTo(procedure2);
    }
}
