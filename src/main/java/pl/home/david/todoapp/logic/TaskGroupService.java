package pl.home.david.todoapp.logic;

import org.springframework.stereotype.Service;
import pl.home.david.todoapp.TaskConfigurationProperties;
import pl.home.david.todoapp.model.TaskGroup;
import pl.home.david.todoapp.model.TaskGroupRepository;
import pl.home.david.todoapp.model.TaskRepository;
import pl.home.david.todoapp.model.projection.GroupReadModel;
import pl.home.david.todoapp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TaskGroupService {

    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toTaskGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    private void toggledGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndTaskGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks, Done all tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
    }

}
