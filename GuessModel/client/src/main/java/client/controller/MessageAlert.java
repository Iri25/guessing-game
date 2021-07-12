package client.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {

    static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message = new Alert(type);

        message.setTitle("Informative Alert");
        message.initOwner(owner);
        message.setContentText(text);
        message.setHeaderText(header);
        message.showAndWait();
    }

    static void showErrorMessage(Stage owner, String text){
        Alert message = new Alert(Alert.AlertType.ERROR);

        message.setTitle("Error Alert");
        message.initOwner(owner);
        message.setContentText(text);
        message.showAndWait();
    }
}
