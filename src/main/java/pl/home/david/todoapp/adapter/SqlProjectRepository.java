package pl.home.david.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.home.david.todoapp.model.Project;
import pl.home.david.todoapp.model.ProjectRepository;
import pl.home.david.todoapp.model.TaskGroup;
import pl.home.david.todoapp.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct g from Project p join fetch  p.steps")
    List<Project> findAll();
}
