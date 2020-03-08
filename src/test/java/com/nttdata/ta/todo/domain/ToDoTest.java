package com.nttdata.ta.todo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.nttdata.ta.todo.web.rest.TestUtil;

public class ToDoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDo.class);
        ToDo toDo1 = new ToDo();
        toDo1.setId(1L);
        ToDo toDo2 = new ToDo();
        toDo2.setId(toDo1.getId());
        assertThat(toDo1).isEqualTo(toDo2);
        toDo2.setId(2L);
        assertThat(toDo1).isNotEqualTo(toDo2);
        toDo1.setId(null);
        assertThat(toDo1).isNotEqualTo(toDo2);
    }
}
