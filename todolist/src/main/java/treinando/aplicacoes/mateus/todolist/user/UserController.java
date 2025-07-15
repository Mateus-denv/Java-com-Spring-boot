package treinando.aplicacoes.mateus.todolist.user;



import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsernome(userModel.getUsernome());

        if (user != null){ // Se for diferente de null não é possivel cadastrar
            // Retornamos a mensagem de erro
            // Retornamos o status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja existe");

        }

        var passwordHashered =  BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashered);
        
       var  userCreated = this.userRepository.save(userModel);
       return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
    
}
