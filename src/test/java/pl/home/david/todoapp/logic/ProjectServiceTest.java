package pl.home.david.todoapp.logic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import pl.home.david.todoapp.TaskConfigurationProperties;
import pl.home.david.todoapp.model.TaskGroupRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {


    @Test
    @DisplayName("shuld throw IllegalStateException when cofigured to allow jest 1 group")
    void createGroup_oneMultipleGroupsConfig_And_openGroups_throwsIllegalStateException() {
        //given
        var mockGroupRepository = Mockito.mock(TaskGroupRepository.class);
        when(mockGroupRepository
                .existsByDoneIsFalseAndProject_Id(ArgumentMatchers.anyInt()))
                .thenReturn(true);
        //and
        var mockTemplate = Mockito.mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTaskFromTemplate()).thenReturn(false);
        //and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        //system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);
        //when + then
        Assertions.assertThatThrownBy(() -> {
            toTest.createGroup(LocalDateTime.now(), 0);
        }).isInstanceOf(IllegalStateException.class);

        Assertions.assertThatIllegalStateException().isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));

    }
}