package pl.home.david.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup group);

    boolean existsByDoneIsFalseAndProject_Id(Long projectId);
}
