package udemy.tareas;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import udemy.tareas.presentacion.SistemaTareasFx;

@SpringBootApplication
public class TareasApplication {

    public static void main(String[] args) {
        //SpringApplication.run(TareasApplication.class, args);

        // En lugar de iniciar Spring Boot con SpringApplication.run, se lanza JavaFX
        // SistemaTareasFx es la clase principal de JavaFX que se ejecutará.
        Application.launch(SistemaTareasFx.class, args);
    }

}
