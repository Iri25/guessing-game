package client.controller;

import domain.*;
import service.IObserver;
import service.IServices;

import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameController extends UnicastRemoteObject implements IObserver, Serializable {

    Player player;
    IServices server;
    ObservableList<Round> modelRound = FXCollections.observableArrayList();
    ObservableList<String> modelPlayer = FXCollections.observableArrayList();

    public GameController() throws RemoteException {}

    public void setServer(IServices server) {
        this.server = server;
    }

    public void setPlayer(Player player) throws Exception {
        this.player = player;
    }

    @FXML
    private ComboBox<String> playerComboBox;

    @FXML
    private TableView<Round> matchTableView;
    @FXML
    private TableColumn<Round, String> id;
    @FXML
    private TableColumn<Round, String> username;
    @FXML
    private TableColumn<Round, String> word;
    @FXML
    private TableColumn<Round, String> points;
    @FXML
    private TableColumn<Round, String> place;

    @FXML
    private TextField letterTextField;

    @FXML
    private Label messageLabel;

    private void initPlayerModel() {
        List<String> players = new ArrayList<>();
        for (Player playerSend : server.getOpponents(player)) {
                players.add(playerSend.getUsername());
        }
        modelPlayer.setAll(players);
    }

    private void initializePlayerComboBox() {

        playerComboBox.setItems(modelPlayer);
        if(playerComboBox.getItems().size() == 2)
            messageLabel.setText("Start Game!");

    }

    private void initMatchModel(String username, char letter) {
        if (username.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty player field!\n");
        else {
            Round round = server.searchLetter(username, letter);
            if(round.getPoints() == round.getWord().length())
                messageLabel.setText("You guessed the word!");
            modelRound.setAll(round);
        }
    }

    private void initMatchModel(String username) {
        if (username.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty player field!\n");
        else {
            Round round = server.findPosition(username);
            modelRound.setAll(round);
        }
    }

    private void initializeMatchTable() {
        id.setCellValueFactory(new PropertyValueFactory<Round, String>("IdString"));
        username.setCellValueFactory(new PropertyValueFactory<Round, String>("UsernameString"));
        word.setCellValueFactory(new PropertyValueFactory<Round, String>("WordString"));
        points.setCellValueFactory(new PropertyValueFactory<Round, String>("PointsString"));
        place.setCellValueFactory(new PropertyValueFactory<Round, String>("PlaceString"));

        matchTableView.setItems(modelRound);
    }

    @FXML
    public void handleSelect(ActionEvent actionEvent) {
        String username = playerComboBox.getValue();

        initMatchModel(username);
        initializeMatchTable();
    }

    @FXML
    public void handleGuess(ActionEvent actionEvent) throws Exception {
        String username = playerComboBox.getValue();
        char letter = letterTextField.getText().charAt(0);

        initMatchModel(username, letter);
        initializeMatchTable();
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) throws Exception {

        Stage stage = new Stage();
        stage.setTitle("Login");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LogoutView.fxml"));

        AnchorPane layout = loader.load();
        stage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setServer(server);

        stage.show();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void loggedIn(Player player) throws RemoteException {
        System.out.println("intra aici");
        System.out.println(server);
        initPlayerModel();
        initializePlayerComboBox();
    }

    @Override
    public void loggedOut(Player player) {
        try {
            server.logout(player, this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newGame(Round round) throws RemoteException {
        try{
            matchTableView.getItems().add(round);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}