package pl.home.david.todoapp.controller;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.home.david.todoapp.TaskConfigurationProperties;

@RestController
class InfoController {

    private DataSourceProperties dataSource;

    private TaskConfigurationProperties taskProp;

    InfoController(DataSourceProperties dataSource, TaskConfigurationProperties taskProp) {
        this.dataSource = dataSource;
        this.taskProp = taskProp;
    }

    @GetMapping("/info/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/info/prop")
    boolean myProp() {
        return taskProp.getTemplate().isAllowMultipleTaskFromTemplate();
    }
}
