package com.example.team_veritas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class ScController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("dashboardPage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("loginPage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    public void switchToSignup(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("signupPage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    public void switchToProfile(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("profilePage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    public void switchToBank(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("bankPage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    public void switchToDonor(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("donorPage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    public void switchToDonate(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ScController.class.getResource("donatePage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}

