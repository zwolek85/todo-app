package pl.home.david.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks")
class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must be not null and empty")
    private String description;
    private boolean done;

    Task(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

     void setDone(boolean done) {
        this.done = done;
    }
}
