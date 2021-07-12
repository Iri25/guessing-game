package client.controller;

import domain.Player;
import domain.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.IServices;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StartController extends UnicastRemoteObject implements Serializable {

    private IServices server;
    private Player player;
    private GameController gameController;

    public StartController() throws RemoteException {
    }

    public void setServer(IServices server) {

        this.server = server;
    }

    public void setObserver(GameController gameController)  {

        this.gameController = gameController;
    }

    public void setPlayer(Player player) {

        this.player = player;
    }

    @FXML
    private TextField wordTextField;

    @FXML
    public void handleStart(ActionEvent actionEvent) throws Exception, IOException {

        String guessWord = wordTextField.getText();
        Word word = new Word(guessWord, player.getUsername());
        server.addWord(word);

        Stage stage = new Stage();
        stage.setTitle(player.getUsername());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/GameView.fxml"));
        AnchorPane layout = loader.load();
        stage.setScene(new Scene(layout));

        gameController = loader.getController();
        gameController.setServer(server);
        gameController.setPlayer(player);

        stage.show();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
