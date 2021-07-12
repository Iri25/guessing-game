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

    IServices server;
    Player player;

    ObservableList<Round> modelRound = FXCollections.observableArrayList();
    ObservableList<String> modelPlayer = FXCollections.observableArrayList();

    public GameController() throws RemoteException {}

    public void setServer(IServices server) {

        this.server = server;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @FXML
    private TextField wordTextField;

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

    @FXML
    public void handleStart(ActionEvent actionEvent) {

        String guessWord = wordTextField.getText();
        if(guessWord.length() < 6)
            MessageAlert.showErrorMessage(null, "Proposal should have at least six characters");
        else {
            Model word = new Model(guessWord, player.getUsername());
            server.addWord(word);
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, null, "Your proposal is hidden. Now your opponent has to guess your proposal.");
        }
    }

    @FXML
    private void initPlayerModel() {
        List<String> players = new ArrayList<>();
        for (Player playerSend : server.getOpponents(player)) {
                players.add(playerSend.getUsername());
        }
        modelPlayer.setAll(players);
    }

    @FXML
    private void initializePlayerComboBox() {

        playerComboBox.setItems(modelPlayer);
    }

    @FXML
    private void initMatchModel(String username) {
        if (username.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty player field!\n");
        else {
            Round round = server.findPosition(username);
            modelRound.setAll(round);
        }
    }

    @FXML
    private void initMatchModel(String username, String letter) {
        if (username.isEmpty() || letter.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty player field!\n");
        else {
            Round round = server.searchProposal(username, letter);
            modelRound.setAll(round);
            if(round.getWord().length() == round.getPoints())
                messageLabel.setText("You guessed the word!");
        }
    }

    @FXML
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
    public void handleGuess(ActionEvent actionEvent) {
        String username = playerComboBox.getValue();
        String letter = letterTextField.getText();

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
    public void addRound(Round round) throws RemoteException {
        try{
            matchTableView.getItems().add(round);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}