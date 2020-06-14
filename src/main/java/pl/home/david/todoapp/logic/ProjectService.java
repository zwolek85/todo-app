package pl.home.david.todoapp.logic;

import pl.home.david.todoapp.TaskConfigurationProperties;
import pl.home.david.todoapp.model.*;
import pl.home.david.todoapp.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(Project save) {
        return repository.save(save);
    }

    private GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTaskFromTemplate()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is aloowed");
        }
        TaskGroup taskGroup = repository.findById(projectId)
                .map(project -> {
                    var result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> new Task(projectStep.getDescription(), deadline.plusDays(projectStep.getDaysToDeadline())))
                                    .collect(Collectors.toSet()));
                    return result;
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));

        return new GroupReadModel(taskGroup);
    }
}
