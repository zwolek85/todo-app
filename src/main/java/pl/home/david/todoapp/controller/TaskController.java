package pl.home.david.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pl.home.david.todoapp.model.Task;
import pl.home.david.todoapp.model.TaskRepository;

import java.util.List;

@RepositoryRestController
class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    TaskController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTask() {
        logger.warn("Exposing all the task");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<?> readAllTask(Pageable page) {
        logger.info("Custom page");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }


}
