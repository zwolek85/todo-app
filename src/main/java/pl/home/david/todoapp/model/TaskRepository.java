package pl.home.david.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface TaskRepository {

    List<Task> findAll();

    Page<Task> findAll(Pageable pageable);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndTaskGroup_Id(Integer taskGroupId);

    List<Task> findByDone(boolean done);

    Task save(Task task);

    List<Task> findAllByTaskGroup_Id(Integer id);
}
