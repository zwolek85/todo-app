package pl.home.david.todoapp.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.home.david.todoapp.TaskConfigurationProperties;
import pl.home.david.todoapp.model.ProjectRepository;
import pl.home.david.todoapp.model.TaskGroupRepository;
import pl.home.david.todoapp.model.TaskRepository;

@Configuration
class LogicConfiguration {

    @Bean
    ProjectService projectService(
            ProjectRepository repository,
            TaskGroupRepository taskGroupRepository,
            TaskGroupService taskGroupService,
            TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(TaskGroupRepository taskGroupRepository,
                                      TaskRepository taskRepository) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
