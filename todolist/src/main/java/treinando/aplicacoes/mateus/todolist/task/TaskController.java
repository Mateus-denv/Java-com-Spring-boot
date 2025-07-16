package treinando.aplicacoes.mateus.todolist.task;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
   
    @PostMapping("/")
    public TaskModel create (@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("Chegou no controle");
        var IdUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)IdUser);
        var task = this.taskRepository.save(taskModel);
        return task;
    } 
}
