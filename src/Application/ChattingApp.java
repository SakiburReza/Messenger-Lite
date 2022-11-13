
package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.NetworkUtil;

import javax.swing.*;
import java.io.File;
import java.net.InetAddress;

/**
 *
 * @author Sakibur Reza
 */
public class ChattingApp extends Application {
    public static String username;
    public static NetworkUtil nc;
    public static String pp;                                   //PP=Profile Picture
    public static File iFile;                                  //iFile stores PP of all the users
    @Override
    public void start(Stage stage) throws Exception  {
        InetAddress address= InetAddress.getLocalHost();
        System.out.println(address.getHostAddress());
        nc= new NetworkUtil("localhost", 33445);
        
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLFile/LogIn.fxml"));
       
       
        Scene scene = new Scene(root);
        stage.setTitle("Get log in with Shakib's App");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e->closeProgram());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void closeProgram(){
        int n=JOptionPane.showConfirmDialog(null, "Are you want to exit?","Exit",JOptionPane.YES_NO_OPTION);
        if(n==0){
            nc.closeConnection();
            System.exit(1);
        }
        else if(n==1){}
                
       
                
    }
    
}
