package udemy.tareas.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udemy.tareas.modelo.Tarea;
import udemy.tareas.servicio.TareaServicio;


import java.net.URL;
import java.util.ResourceBundle;

//Anotada con @Component para que Spring pueda gestionarla y permitir inyecciones de dependencias.
@Component
public class IndexControlador implements Initializable {
    //Logger para imprimir mensajes en la consola
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    //Inyección de dependencias
    @Autowired
    private TareaServicio tareaServicio;
    // Referencias a los componentes definidos en el archivo FXML.
    // La anotación @FXML conecta las variables con los elementos de la interfaz gráfica.
    @FXML
    private TableView<Tarea> tablaTareas;
    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;
    @FXML
    private TableColumn<Tarea, String> nombreTareaColumna;
    @FXML
    private TableColumn<Tarea, String> responsableTareaColumna;
    @FXML
    private TableColumn<Tarea, String> estadoTareaColumna;
    @FXML
    private TextField tfNombreTarea;
    @FXML
    private TextField tfResponsable;
    @FXML
    private TextField tfEstado;

    // Variable que guarda el ID de la tarea seleccionada para modificarla.
    private Integer idInterno;

    /* ObservableList para la tabla de tareas. Esta lista "observa" los cambios, de modo que cualquier
        modificación (agregar/eliminar) se refleja en la tabla. */
    private final ObservableList<Tarea> tareaList = FXCollections.observableArrayList();

    // Este metodo se ejecuta automáticamente cuando se inicializa la vista.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaTareas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Configura las columnas de la tabla.
        configurarColumnas();
        listarTareas();
    }

    // Metodo que se llama cuando se presiona el botón para agregar una nueva tarea.
    public void agregarTarea() {
        // Valida que el nombre no esté vacío.
        if (tfNombreTarea.getText().isEmpty()) {
            mostrarMensaje("Error de validación", "Debe proporcionar un nombre para la tarea.");
            tfNombreTarea.requestFocus();  // Coloca el cursor en el campo de texto.
        } else {
            var tarea = new Tarea();

            //Llena la tarea con los datos del formulario.
            recolectarDatosFormulario(tarea);

            //Establece el ID como null, ya que es una nueva tarea y el ID será generado automáticamente.
            tarea.setIdTarea(null);

            // Guarda la tarea usando el servicio de tareas.
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Información", "Tarea guardada");
            limpiarFormulario();
            listarTareas();
        }
    }

    // Metodo que carga los datos de una tarea en el formulario cuando se selecciona de la tabla.
    public void cargarFormularioTarea() {
        // Obtiene la tarea seleccionada en la tabla.
        var tarea = tablaTareas.getSelectionModel().getSelectedItem();
        /* Tarea podría ser nulo por que si el usuario clica en los títulos de las columnas también se estaría
        llamando al evento onMouseClicker */
        if (tarea != null) {
            // Carga los datos de la tarea en los campos del formulario.
            idInterno = tarea.getIdTarea();
            tfNombreTarea.setText(tarea.getNombreTarea());
            tfResponsable.setText(tarea.getResponsable());
            tfEstado.setText(tarea.getEstado());
        }
    }

    // Metodo para modificar una tarea existente.
    public void modificarTarea() {
        // Valida que se haya seleccionado una tarea para modificar.
        if (idInterno == null) {
            mostrarMensaje("Información", "Debe seleccionar una tarea.");
            return;
        }

        // Valida que el campo de nombre no esté vacío.
        if (tfNombreTarea.getText().isEmpty()) {
            mostrarMensaje("Error de Validación", "Debe proporcionar una tarea.");
            return;
        }

        //Crea una nueva tarea con los datos del formulario.
        var tarea = new Tarea();
        recolectarDatosFormulario(tarea);

        // Guarda la tarea modificada.
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Información", "Tarea modificada.");
        limpiarFormulario();
        listarTareas();
    }

    // Limpia los campos del formulario y resetea la variable idInterno.
    public void limpiarFormulario() {
        idInterno = null;
        tfNombreTarea.clear();
        tfEstado.clear();
        tfResponsable.clear();
    }

    // Llena una tarea con los datos del formulario.
    private void recolectarDatosFormulario(Tarea tarea) {
        if (idInterno != null) {
            tarea.setIdTarea(idInterno);
            tarea.setNombreTarea(tfNombreTarea.getText());
            tarea.setEstado(tfEstado.getText());
            tarea.setResponsable(tfResponsable.getText());
        }
    }

    // Muestra una alerta con un mensaje en pantalla.
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Carga todas las tareas y las muestra en la tabla.
    private void listarTareas() {
        tareaList.clear();  // Limpia la lista actual.
        tareaList.addAll(tareaServicio.listarTareas());  // Agrega las tareas obtenidas desde el servicio.
        tablaTareas.setItems(tareaList);  // Establece la lista en la tabla.
    }

    // Configura las columnas de la tabla para mostrar correctamente los datos de la entidad `Tarea`.
    private void configurarColumnas() {
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableTareaColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estadoTareaColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    // Metodo para eliminar una tarea seleccionada.
    public void eliminarTarea() {
        // Obtiene la tarea seleccionada de la tabla.
        var tarea = tablaTareas.getSelectionModel().getSelectedItem();

        // Valida que se haya seleccionado una tarea.
        if (tarea != null) {
            logger.info("Registro a eliminar: " + tarea);

            // Elimina la tarea usando el servicio de tareas.
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Información", "Tarea eliminada");
            limpiarFormulario();
            listarTareas();
        } else {
            mostrarMensaje("Error", "No hay tareas seleccionadas");
        }
    }
}