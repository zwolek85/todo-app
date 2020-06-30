package pl.home.david.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must be not null and empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "task_groups_id")
    private TaskGroup taskGroup;

    public Task() {
    }

    public Task(String description, LocalDateTime deadline, TaskGroup taskGroup) {
        this.description = description;
        this.deadline = deadline;
        if (taskGroup != null) {
            this.taskGroup = taskGroup;
        }
    }

    public Task(@NotBlank(message = "Task's description must be not null and empty") String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getTaskGroup() {
        return taskGroup;
    }

    void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        taskGroup = source.taskGroup;
    }
}
