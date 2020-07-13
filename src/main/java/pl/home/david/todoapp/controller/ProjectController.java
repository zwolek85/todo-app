package pl.home.david.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.home.david.todoapp.logic.ProjectService;
import pl.home.david.todoapp.model.Project;
import pl.home.david.todoapp.model.ProjectStep;
import pl.home.david.todoapp.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectController {

    private ProjectService projectService;

    @Autowired
    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current,
                      BindingResult bindingResult,
                      Model model) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        projectService.save(current.toProject());
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodales projekt");
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.readAll();
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {

        try {
            projectService.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grupe");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Bład podczas tworzenia grupy");
        }
        return "projects";
    }
}
