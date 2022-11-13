package FXMLFile;

import Application.ChattingApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class SignUpController implements Initializable {

    @FXML
    public TextField userID;
    @FXML
    public PasswordField pass;
    @FXML
    private PasswordField confirmpass;

    
    @FXML
    private ImageView setImage;

    File file;

    boolean setPP;
    @FXML
    private StackPane pc;
    @FXML
    private JFXButton signUp;
    @FXML
    private JFXButton backButton;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setPP = false;
        ChattingApp.iFile = new File("PP_Information.txt");
        ChattingApp.pp = null;
        backButton.setVisible(false);
       
        

    }

    @FXML
    private void userIdcontroller(ActionEvent event) {

    }

    @FXML
    private void passController(ActionEvent event) {

    }

    @FXML
    private void confirmPassController(ActionEvent event) {
    }


    @FXML
    private void setImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");

        fileChooser.getExtensionFilters().addAll(extensionFilterjpg, extensionFilterJPG);
        file = fileChooser.showOpenDialog(null);


        ChattingApp.pp = file.toURI().toString();
        setImage.setImage(new Image(ChattingApp.pp));
        if (!ChattingApp.pp.isEmpty()) {
            setPP = true;
        }
    }

    @FXML
    private void singUpAction(ActionEvent event) throws IOException {
        
         System.out.println("signup clicked");
        if (userID.getText().isEmpty() || pass.getText().isEmpty() || confirmpass.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to fill up all the field ");
        } else {
            if (setPP == false) {
                JOptionPane.showMessageDialog(null, "Please choose a profile picture");
            } else {

                if (pass.getText().equals(confirmpass.getText())) {

                    ChattingApp.nc.write("signup#" + userID.getText() + "#" + pass.getText());

                    FileWriter fw = new FileWriter(ChattingApp.iFile, true);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(userID.getText() + " " + ChattingApp.pp);
                    fw.close();
                    pw.close();

                } else {
                    JOptionPane.showMessageDialog(null, "Password doesn't match");
                }

            }
        }
        ChattingApp.username = userID.getText();

        Parent root = FXMLLoader.load(getClass().getResource("/FXMLFile/MessagingSecne.fxml"));
        Scene scene = signUp.getScene();
        root.translateXProperty().set(scene.getHeight());
        pc.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    

    @FXML
    private void backButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLFile/FXMLDocument.fxml"));
        Scene scene = signUp.getScene();
        root.translateXProperty().set(scene.getHeight());
        pc.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

}
