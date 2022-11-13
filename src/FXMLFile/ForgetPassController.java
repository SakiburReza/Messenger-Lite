
package FXMLFile;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ForgetPassController implements Initializable {

    @FXML
    public static TextField userID;
    @FXML
    public  static TextField birthday;
    @FXML
    public static PasswordField pass;
    @FXML
    public static PasswordField confirmpass;
    @FXML
    private Button setPass;
    @FXML
    private Button backLogin;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void passController(ActionEvent event) {
    }

    @FXML
    private void confirmPassController(ActionEvent event) {
    }

    @FXML
    private void setPassAction(ActionEvent event) throws IOException {
        
       
        
    }

    @FXML
    private void backLoginAction(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        
         Scene scene=new Scene(root);
         Stage window =(Stage) ((Node)event.getSource()).getScene().getWindow();
         window.setScene(scene);
         window.show();
         
    }
    
}
