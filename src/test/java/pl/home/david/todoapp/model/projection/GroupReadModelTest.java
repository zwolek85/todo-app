package pl.home.david.todoapp.model.projection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.home.david.todoapp.model.Task;
import pl.home.david.todoapp.model.TaskGroup;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("shold create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline(){
        //given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar",null)));

        //when
        var result = new GroupReadModel(source);

        //then
        Assertions.assertThat(result).hasFieldOrPropertyWithValue("deadline",null);

    }

}