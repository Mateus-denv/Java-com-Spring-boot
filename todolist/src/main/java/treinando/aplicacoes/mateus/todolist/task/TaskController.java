package treinando.aplicacoes.mateus.todolist.task;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
   
    @PostMapping("/")
    public ResponseEntity create (@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("Chegou no controle");
        var IdUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)IdUser);
        
        var correntDate = LocalDateTime.now();
        // 10/12/2025 - corrent
        // 10/11/2025 - starAt
        // Error!
        if(correntDate.isAfter(taskModel.getStartAt()) || correntDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
            body("A data deve ser mais que a data atual");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    } 

    
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/")
    public List<TaskModel> list (HttpServletRequest request){
        var IdUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID)IdUser);

        return tasks;
    }

    @PutMapping("/{id}")
    public TaskModel updade (@RequestBody TaskModel taskmModel ,@PathVariable UUID id,HttpServletRequest request){
        
        var IdUser = request.getAttribute("idUser");
        taskmModel.setIdUser((UUID)IdUser);
        taskmModel.setIdUser(id);
        return this.taskRepository.save(taskmModel);
    }
}
