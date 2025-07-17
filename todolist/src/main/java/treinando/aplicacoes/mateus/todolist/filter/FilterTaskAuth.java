package treinando.aplicacoes.mateus.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import treinando.aplicacoes.mateus.todolist.user.IUserRepository;

@Component
public class FilterTaskAuth extends  OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        // Verifico a rota
        var servletpath = request.getServletPath();
        if (servletpath.startsWith("/tasks/")){
            // Pegar a autenticação (usuario, senha)
            var authorization = request.getHeader("Authorization");
            System.out.println("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim(); // <--- autenticação codificada SAIDA: bWF0ZXVzZGVqZXN1czoxMjM0NQ==

            // Entendi bem pouco
            byte[] authDecode = Base64.getDecoder().decode(authEncoded); // <--- vai descodificar e criar um array de bytes SAIDA: [B@f49f99d494]

            var authString = new String(authDecode);// <--- Transforma em uma String SAIDA mateusdejesus:12345

            String[] credentials =  authString.split(":"); // <--- Dividir separando por : transformando em um array SAIDA ["mateusdejesus", 12345]
            String username = credentials[0].trim(); // Remove espaços
            String password = credentials[1];
            System.out.println(credentials[0]);
            System.out.println(credentials[1]);

            // Validando se o Usuario existe
            System.out.println("Procurando usuário: '" + username + "'");
            System.out.println("Tamanho do username: " + username.length());

            var user = this.userRepository.findByUsernome(username);
            
            System.out.println("Usuário encontrado: " + user);


            if (user == null){
                response.sendError(401);
            }else{

                // Validar senha está correta 
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()); // <--- Recebe a senha daqui e a senha cadastrada e retorna um resultado
                // verify(a senha q deve ser um array de caracteres, e a senha cadastrada)
                // transformando a senha em um array de caractes - userpassword.toCharArray() -

                if (passwordVerify.verified){
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);

                }else{
                    response.sendError(401);
                }
                // segue viagem


                }
            } else{
                filterChain.doFilter(request, response);
        }
    }
        
}
