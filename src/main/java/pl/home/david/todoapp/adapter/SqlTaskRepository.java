package pl.home.david.todoapp.adapter;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.home.david.todoapp.model.Task;
import pl.home.david.todoapp.model.TaskRepository;

import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    @Query(nativeQuery = true, value = "select (count(*)) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndTaskGroup_Id(Integer taskGroupId);

    @Override
    List<Task> findAllByTaskGroup_Id(Integer id);

}
