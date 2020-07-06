package pl.home.david.todoapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.home.david.todoapp.model.Task;
import pl.home.david.todoapp.model.TaskGroup;
import pl.home.david.todoapp.model.TaskRepository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    @Profile({"!integration"})
    DataSource e2etestDataSpource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile({"integration"})
    TaskRepository testRepo() {
        return new TaskRepository() {
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
                int key = tasks.size() + 1;
                Field field = null;
                try {
                    field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(task, key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                tasks.put(key, task);
                return tasks.get(key);
            }

            @Override
            public List<Task> findAllByTaskGroup_Id(Integer id) {
                return List.of();
            }
        };
    }
}
