package pl.home.david.todoapp.logic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import pl.home.david.todoapp.TaskConfigurationProperties;
import pl.home.david.todoapp.model.*;
import pl.home.david.todoapp.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {


    @Test
    @DisplayName("shuld throw IllegalStateException when cofigured to allow jest 1 group")
    void createGroup_oneMultipleGroupsConfig_And_openGroups_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockTaskGroupRepository = groupRepositoryReturnig(true);
        ;
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(null, mockTaskGroupRepository, mockConfig, null);
        //when
        var exception = Assertions.catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //then
        Assertions.assertThat(exception).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only one undone group");

    }

    @Test
    @DisplayName("shuld throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_openGroups_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig, null);
        //when
        var exception = Assertions.catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //then
        Assertions.assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }


    @Test
    @DisplayName("shuld throw IllegalArgumentException when configuration cofigured to allow jest 1 group no groups and projects for a given id")
    void createGroup_oneMultipleGroupsConfig_And_NoUndoneGroups_NoProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository mockTaskGroupRepository = groupRepositoryReturnig(false);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, mockTaskGroupRepository, mockConfig, null);
        //when
        var exception = Assertions.catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //then
        Assertions.assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("Should create a new group from project")
    public void createGroup_configurationOk_existingProject_createsAndSaveFroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceBeforeCall = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig, serviceBeforeCall);

        //when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then
        Assertions.assertThat(result.getDescription()).isEqualTo("bar");
        Assertions.assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        Assertions.assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {

        var result = mock(Project.class);
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturnig(final boolean result) {
        var mockTaskGroupRepository = Mockito.mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository
                .existsByDoneIsFalseAndProject_Id(ArgumentMatchers.anyInt()))
                .thenReturn(result);
        return mockTaskGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = Mockito.mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTaskFromTemplate()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private Map<Integer, TaskGroup> map = new HashMap<>();
        private int index = 0;

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(int projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }
}