package pl.home.david.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class TestTaskRepository implements TaskRepository {
    private Map<Integer, Task> tasks = new HashMap<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public boolean existsByDoneIsFalseAndTaskGroup_Id(Integer taskGroupId) {
        return false;
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return null;
    }

    @Override
    public Task save(Task task) {
        return tasks.put(tasks.size() + 1, task);
    }
}
