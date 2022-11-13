/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXMLFile;

import Application.ChattingApp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Sakibur Reza
 */
public class LogInController implements Initializable {

    @FXML
    private StackPane parentContainer;
    @FXML
    private AnchorPane signInAnchorPane;
    @FXML
    private TextField userName;
    @FXML
    private RadioButton rememberMe;
    @FXML
    private Button signIn;
    @FXML
    private Button forgotPassword;
    @FXML
    private PasswordField password;
    @FXML
    private Button createAccount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ChattingApp.iFile = new File("PP_Information.txt");
        ChattingApp.pp = null;
        forgotPassword.setVisible(false);

    }

    @FXML
    private void userNameAction(ActionEvent event) {
        ChattingApp.nc.write("seekingPass#" + userName.getText());
        String str = (String) ChattingApp.nc.read();
        System.out.println(str);
        if (str.equals("null")) {
        } else {
            password.setText(str);
            rememberMe.setVisible(false);
        }
    }

    @FXML
    private void rememberMeAction(ActionEvent event) {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fulfill the field");
        } else {
            ChattingApp.nc.write("rem#" + (String) userName.getText() + "#" + (String) password.getText());
        }
    }

    @FXML
    private void signInAction(ActionEvent event) throws FileNotFoundException, IOException {
        System.out.println(userName.getText() + " " + password.getText());
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fulfill the field");
        } else {
            ChattingApp.username = userName.getText();

            ChattingApp.nc.write("login#" + userName.getText() + "#" + password.getText());
            if (ChattingApp.nc.read().equals("true")) {

                Scanner scn = new Scanner(ChattingApp.iFile);
                String[] str = new String[2];
                while (scn.hasNextLine()) {
                    str[0] = scn.next();
                    str[1] = scn.next();

                    if (str[0].equals(userName.getText())) {
                        ChattingApp.pp = str[1];
                        break;
                    }
                }

                Parent root = FXMLLoader.load(getClass().getResource("/FXMLFile/MessagingScene.fxml"));
                Scene scene = signIn.getScene();
                root.translateXProperty().set(scene.getHeight());
                parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            } else {
                JOptionPane.showMessageDialog(null, "Please try again later");
            }
        }

    }

    @FXML
    private void forgotPasswordAction(ActionEvent event) {
    }

    @FXML
    private void mouseClickPass(MouseEvent event) {
        System.out.println("Checkingdddddd");
//        ChattingApp.nc.write("seekingPass#" + userName.getText());
//        String str = (String) ChattingApp.nc.read();
//        System.out.println(str);
//        if (str.equals("null")) {
//        } else {
//            password.setText(str);
//        }
//        System.out.println("kiiiiiiikkkkkkkkk");
    }

   

    @FXML
    private void createAccountAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLFile/SignUp.fxml"));
        Scene scene = createAccount.getScene();
        root.translateYProperty().set(scene.getHeight());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

    }

}
