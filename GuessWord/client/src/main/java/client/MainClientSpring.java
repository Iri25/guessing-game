package client;

import client.controller.GameController;
import client.controller.LoginController;
import client.controller.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.IServices;

import java.io.IOException;

import static javafx.application.Application.launch;

public class MainClientSpring extends Application {

    private IServices server;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            System.out.println("Start client");

            server = (IServices) factory.getBean("serv");
            System.out.println("Obtained a reference to remote project server");

        }
        catch (Exception exception) {
            System.err.println("Guess Word initialization exception:" + exception);
            exception.printStackTrace();
        }

        Stage stage = new Stage();
        try {
            initWindow(stage);
            stage.setTitle("Login");
            stage.show();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void initWindow(Stage primaryStage) throws Exception {

        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(getClass().getClassLoader().getResource("views/LoginView.fxml"));
        AnchorPane layout1 = loader1.load();
        primaryStage.setScene(new Scene(layout1));
        LoginController loginController = loader1.getController();
        loginController.setServer(server);

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getClassLoader().getResource("views/StartView.fxml"));
        AnchorPane layout2 = loader2.load();
        StartController startController = loader2.getController();
        startController.setServer(server);

        FXMLLoader loader3 = new FXMLLoader();
        loader3.setLocation(getClass().getClassLoader().getResource("views/GameView.fxml"));
        AnchorPane layout3 = loader3.load();
        GameController gameController = loader3.getController();
        gameController.setServer(server);

        startController.setObserver(gameController);
        loginController.setObserver(gameController);
    }

    public static void main(String[] args) {

        launch(args);
    }
}
