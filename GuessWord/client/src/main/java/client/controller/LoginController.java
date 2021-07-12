package client.controller;

import javafx.scene.Node;
import domain.Player;
import service.IObserver;
import service.IServices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;


public class LoginController implements Serializable {

    private IServices server;
    private StartController startController;
    private GameController gameController;

    public void setServer(IServices server) {

        this.server = server;
    }

    public void setObserver(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    public void handleLogin(ActionEvent actionEvent) {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        Player player = new Player(username, password);

        if (username == null)
            MessageAlert.showErrorMessage(null, "Empty Username!\n");
        else {
            if (password == null)
                MessageAlert.showErrorMessage(null, "Empty Password!\n");
            else {
                try {
                    Player login = server.getUser(player);
                    server.login(login, gameController);

                    Stage stage = new Stage();
                    stage.setTitle(login.getUsername());

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/views/StartView.fxml"));
                    AnchorPane layout = loader.load();
                    stage.setScene(new Scene(layout));

                    startController = loader.getController();
                    startController.setServer(server);
                    startController.setPlayer(login);

                    stage.show();
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                }
                catch (Exception e) {
                    MessageAlert.showErrorMessage(null, "Username or Password invalid!\n");
                }
            }
        }
    }
}