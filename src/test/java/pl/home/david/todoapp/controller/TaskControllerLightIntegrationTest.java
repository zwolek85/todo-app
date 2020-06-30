package pl.home.david.todoapp.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.home.david.todoapp.model.Task;
import pl.home.david.todoapp.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnGivenTask() throws Exception {
        // given
        String foo = "foo";
        when(taskRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Task(foo, LocalDateTime.now())));
        //when + then
        mockMvc.perform(get("/tasks/123"))
                .andDo(print())
                .andExpect(content().string(CoreMatchers.containsString(foo)));

    }
}
