package udemy.tareas.presentacion;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import udemy.tareas.TareasApplication;

import java.io.IOException;

public class SistemaTareasFx extends Application {
    /* Instancia del contexto de la aplicación de Spring. Este contexto es el núcleo de Spring y gestiona los
        componentes y servicios de la aplicación, como los Beans. */
    private ConfigurableApplicationContext applicationContext;

    /* Este metodo se ejecuta antes del metodo start() de JavaFX. Aquí es donde inicias el contexto de Spring
        utilizando SpringApplicationBuilder. Esto permite que JavaFX y Spring se comuniquen y compartan
        el ciclo de vida.*/
    @Override
    public void init() {
        /* new SpringApplicationBuilder(TareasApplication.class).run():
            -SpringApplicationBuilder construye un contexto de aplicación de Spring con tu clase TareasApplication.
            -TareasApplication.class es tu clase principal de Spring Boot, donde está la anotación
                @SpringBootApplication, lo que permite a Spring realizar tareas de escaneo y configuración automática
                de componentes (controladores, repositorios, etc.).
            -.run() inicializa el contexto de Spring, lo que permite que Spring maneje la creación y gestión de los
                beans (componentes como IndexControlador, repositorios, servicios, etc.).
        */
        this.applicationContext = new SpringApplicationBuilder(TareasApplication.class).run();

    }

    @Override
    public void start(Stage stage) throws IOException {
        //Utiliza el FXMLLoader para cargar la vista desde el archivo FXML  index.fxml.
        FXMLLoader loader = new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));

        // El controller factory se utiliza para que Spring gestione los controladores (en lugar de JavaFX).
        loader.setControllerFactory(applicationContext::getBean);

        //Se crea una escena con la vista cargada desde el archivo FXML.
        Scene escena = new Scene(loader.load());

        //Se muestra la ventana de la aplicación.
        stage.setScene(escena);
        stage.show();
    }

    //Metodo que se llama al cerrar la aplicacion
    @Override
    public void stop() {
        //Este metodo se ejecuta cuando cierras la aplicación.
        //Se cierra el contexto de Spring para liberar recursos.
        applicationContext.close();

        //Cierra la aplicación JavaFX.
        Platform.exit();

    }
}
