package pl.home.david.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.home.david.todoapp.model.Task;
import pl.home.david.todoapp.model.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    TaskController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    ResponseEntity<?> creatTask(@RequestBody @Valid Task toCreate) throws URISyntaxException {
        Task task = taskRepository.save(toCreate);
        URI uri = new URI("/" + task.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTask() {
        logger.warn("Exposing all the task");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping
    ResponseEntity<?> readAllTask(Pageable page) {
        logger.info("Custom page");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(taskRepository.findByDone(state));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id).ifPresent(task -> {
            task.updateFrom(toUpdate);
            taskRepository.save(task);
        });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        taskRepository.findById(id).ifPresent(task -> {
            task.setDone(!task.isDone());
        });
        return ResponseEntity.noContent().build();
    }


}
