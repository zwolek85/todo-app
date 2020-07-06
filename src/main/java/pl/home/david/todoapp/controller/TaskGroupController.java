package pl.home.david.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.home.david.todoapp.logic.TaskGroupService;
import pl.home.david.todoapp.model.TaskRepository;
import pl.home.david.todoapp.model.projection.GroupReadModel;
import pl.home.david.todoapp.model.projection.GroupWriteModel;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskRepository taskRepository;
    private TaskGroupService taskGroupService;

    @Autowired
    TaskGroupController(final TaskRepository taskRepository, TaskGroupService taskGroupService) {
        this.taskRepository = taskRepository;
        this.taskGroupService = taskGroupService;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) throws URISyntaxException {
        GroupReadModel groupReadModel = taskGroupService.createGroup(toCreate);
        URI uri = new URI("/" + groupReadModel.getId());
        return ResponseEntity.created(uri).body(groupReadModel);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        return ResponseEntity.ok(taskGroupService.readAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> readAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByTaskGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchGroup(@PathVariable int id) {
        taskGroupService.toggledGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    ResponseEntity<?> handlerIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalStateException.class})
    ResponseEntity<String> handlerIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
