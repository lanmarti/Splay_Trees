package semisplay;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

public class AboutPopup extends Popup {

    private static AboutPopup popup;

    private AboutPopup() {

        final Label hello
                = new Label("  Toppie\n\n"
                            + "Interacting with splay trees...\n\n"
                            + "Author: Bert Gijsbers\n"
                            + "University of Ghent\n"
                            + "Belgium\n\n\n");
        hello.setAlignment(Pos.CENTER);
        hello.setStyle("-fx-font-size: 16; ");

        Button ok = new Button("Thanks!");
        ok.setStyle("-fx-font-size: 1.2em; ");

        HBox hbox = new HBox();
        hbox.getChildren().add(ok);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);

        VBox vbox = new VBox();
        vbox.getChildren().add(hello);
        vbox.getChildren().add(hbox);
        vbox.setPadding(new Insets(20));

        vbox.setStyle("-fx-background-color: #FFBBCC");

        this.setHideOnEscape(true);
        this.getContent().addAll(vbox);

        ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                popup.hide();
            }
        });
    }

    public static void showAbout(Window window) {
        if (popup == null) {
            popup = new AboutPopup();
        }
        if (popup.isShowing()) {
            popup.hide();
        } else {
            popup.show(window);
        }
    }
}
