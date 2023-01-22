package espol.poo.talleru6;

import espol.poo.modelo.Categoria;
import espol.poo.modelo.Producto;
import espol.poo.modelo.Utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PrimaryController {

    @FXML
    private FlowPane fpProductos;
    @FXML
    private VBox vbSeccionCrear;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox cmbCategoria;
    @FXML
    private Label lblEstado;

    /**
     * Initializes the controller class.
     */
    public void initialize() {
        //esconder la seccion de crear producto
        vbSeccionCrear.setVisible(false);
        //llenar el panel central
        llenarPanelCentral();

    }

    /**
     * COMPLETAR muestra el VBox del formulario y llena el combobox con los
     * valores de categoria
     */
    @FXML
    private void mostrarSeccion() {
        vbSeccionCrear.setVisible(true);
        cmbCategoria.getItems().addAll(Categoria.values());

    }

    /**
     * COMPLETAR metodo que llena el FlowPane con la informacion de los
     * productos la imagen del archivo es el id con extension jpg, si no hay
     * archivo mostrar imagen x. Los archivos se encuentran en la carpeta files,
     * a nivel del proyecto
     */
    private void llenarPanelCentral() {
        for (Producto producto : Producto.cargarProductos("files/productos.txt")) {
            VBox panelProducto = new VBox();
            ImageView img = new ImageView();
            Label lbId = new Label();
            lbId.setText(producto.getId() + "");
            Label lbNombre = new Label();
            lbNombre.setText(producto.getNombre());
            try {
                FileInputStream is = new FileInputStream("files/" + producto.getId() + ".jpg");
                Image imagen = new Image(is);
                img.setImage(imagen);
            } catch (FileNotFoundException ex) {
                try {
                    FileInputStream is = new FileInputStream("files/x.jpg");
                    Image imagen = new Image(is);
                    img.setImage(imagen);
                } catch (FileNotFoundException ex1) {
                    ex1.printStackTrace();
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
            img.setFitWidth(110);
            img.setFitHeight(120);
            panelProducto.getChildren().add(img);
            panelProducto.getChildren().add(lbId);
            panelProducto.getChildren().add(lbNombre);
            fpProductos.getChildren().add(panelProducto);

            img.setOnMouseClicked(ev -> System.out.println(producto));
        }

    }

    /**
     * COMPLETAR metodo asociado al boton Guardar verifica que se haya
     * completado el formulario para guardar el producto
     *
     */
    @FXML
    private void guardarProducto() {
        //llamar al metodo guardar producto y presentar los dialogos correspondientes
        if (txtNombre.getText().equals("")) {
            mostrarAlerta(WARNING, "Ingrese el nombre del producto");
        }
        if (txtID.getText().equals("")) {
            mostrarAlerta(WARNING, "ingrese el ID del prodcuto");
        } else {
            try {
                String categoria = (String) cmbCategoria.getValue();
                Producto.guardarProducto("files/productos.txt", Integer.parseInt(txtID.getText()), txtNombre.getText(), Categoria.valueOf(categoria));
                if (!(txtNombre.getText().equals("") | txtID.getText().equals(""))) {
                    mostrarAlerta(INFORMATION, "Producto guardado exitosamente");
                    vbSeccionCrear.setVisible(false);
                    llenarPanelCentral();
                }
            } catch (NullPointerException e) {
                mostrarAlerta(ERROR, "Seleccione una categoria");
            } catch (IOException ex) {
                mostrarAlerta(ERROR, "Error al guardar el producto");
            }
        }

        txtID.clear();
        txtNombre.clear();
        
    }

    /**
     * COMPLETAR metodo asociado al boton Sincronizar Repositorio mediante un
     * hilo llama al metodo sincronizar de Utils y muestra en el Label el estado
     * de la transacción
     *
     */
    @FXML
    private void sincRepositorio() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Utils.sincronizar();
            }
        });

    }

    @FXML
    private void cancelarCrear() {
        vbSeccionCrear.setVisible(false);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);

        alert.setTitle("Resultado de operacion");
        alert.setHeaderText("Notificacion");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
