
package semisplay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Use toppie to manipulate semi-splay trees (with variants).
 * The GUI is specified by a FXML file.
 * You can modify/design that one with SceneBuilder2.0.
 */
public class Toppie extends Application {

    public static Stage theStage;

    @Override
    public void start(Stage stage) throws Exception {
        theStage = stage;
        Parent root = FXMLLoader.load(
                getClass().getResource("ToppieFXML.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Toppie");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }

}
