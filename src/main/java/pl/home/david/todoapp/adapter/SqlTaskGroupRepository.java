package pl.home.david.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.home.david.todoapp.model.TaskGroup;
import pl.home.david.todoapp.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("select distinct g from TaskGroup g join fetch  g.tasks")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Long projectId);
}
