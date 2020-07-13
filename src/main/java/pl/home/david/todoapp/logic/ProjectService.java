package pl.home.david.todoapp.logic;

import pl.home.david.todoapp.TaskConfigurationProperties;
import pl.home.david.todoapp.model.Project;
import pl.home.david.todoapp.model.ProjectRepository;
import pl.home.david.todoapp.model.ProjectStep;
import pl.home.david.todoapp.model.TaskGroupRepository;
import pl.home.david.todoapp.model.projection.GroupReadModel;
import pl.home.david.todoapp.model.projection.GroupTaskWriteModel;
import pl.home.david.todoapp.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService taskGroupService;

    public ProjectService(ProjectRepository repository,
                          TaskGroupRepository taskGroupRepository,
                          TaskConfigurationProperties config,
                          TaskGroupService taskGroupService) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(Project save) {
        return repository.save(save);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTaskFromTemplate()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is aloowed");
        }
        GroupReadModel result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps()
                                    .stream()
                                    .map(projectStep -> {
                                        return createGroupTaskWriteModel(projectStep, deadline);
                                    }).collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return result;
    }

    private GroupTaskWriteModel createGroupTaskWriteModel(ProjectStep projectStep, LocalDateTime deadline) {
        GroupTaskWriteModel task = new GroupTaskWriteModel();
        task.setDescription(projectStep.getDescription());
        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
        return task;
    }
}
