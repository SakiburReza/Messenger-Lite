package FXMLFile;

import Application.ChattingApp;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Sakibur Reza
 */
public class MessagingSceneController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView day;
    @FXML
    private Label whatTodo;
    @FXML
    private TableView<ActiveList> activeListTable;
    @FXML
    private TableColumn<ActiveList, String> activeListColumn;
    @FXML
    private Pane typingMessagePane;
    @FXML
    private TableView<MessageList> showMessageTable;
    @FXML
    private TableColumn<MessageList, String> colUsername;
    @FXML
    private TableColumn<MessageList, String> colMessage;

    @FXML
    private JFXTextField usernameText;
    @FXML
    private JFXTextField messageText;
    @FXML
    private Label labelUser;

    @FXML
    private Label fileLabel;
    @FXML
    private ImageView userImage;

    File attachFile = null;
    File getFile;

    @FXML
    private TableView<GroupList> groupsTable;
    @FXML
    private TableColumn<GroupList, String> groups;
    @FXML
    private JFXTextField groupNameField;
    @FXML
    private JFXTextField addMemberField;
    @FXML
    private TableView<MemberList> groupMembers;
    @FXML
    private TableColumn<MemberList, String> members;
    @FXML
    private Pane groupMessagePane;
    @FXML
    private JFXButton searchGroup;
    @FXML
    private JFXTextField getGroupMessage;
    @FXML
    private JFXTextField searchGroupName;
    @FXML
    private JFXButton gSend;
    @FXML
    private JFXButton gSeeTheMember;
    @FXML
    private JFXButton gCreateGroup;
    @FXML
    private TableView<MessageList1> showMessageTable1;
    @FXML
    private TableColumn<MessageList1, String> colUsername1;
    @FXML
    private TableColumn<MessageList1, String> colMessage1;

    private HashMap<String, String> clientmap = new HashMap<>();
    ObservableList<MessageList> messageList = FXCollections.observableArrayList();
    ObservableList<MessageList1> messageList1 = FXCollections.observableArrayList();
    Thread readThread;
    boolean attachFiLe;
    File addDayfile = null;
    private List<String> dayList = new ArrayList<String>();

    File folder = new File("Day\\");
    File[] listOfFiles = folder.listFiles();
    Stage stage;

    public MessagingSceneController() {
        this.readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String str = (String) ChattingApp.nc.read();
                        System.out.println(str);
                        String[] split = str.split("#");
                        switch (split[0]) {
                            case "activelist":
                                activeList();
                                break;
                            case "C":
                                messageListUpdate(split[1], split[2]);
                                System.out.println(split[1] + " " + split[2]);
                                break;
                            case "getFile": {

                                getFile(split[2], split[3]);

                                messageListUpdate(split[1], "(Received) " + split[2]);

                            }
                            case "G":
                                if (split[1].equals("searchGroup")) {
                                    searchGroup();
                                } else if (split[1].equals("members")) {
                                    members();
                                } else if (split[1].equals("message")) {
                                    messageListUpdate(split[3] + " sent in " + split[2], split[4]);
                                    System.out.println(split[2] + " " + split[3] + " " + split[4]);
                                }
                                break;
                            case "logout":
                                ChattingApp.nc.closeConnection();
                                clientmap.remove(ChattingApp.username);
                                System.exit(1);
                                break;
                            case "viewDay":
                                System.out.println("It is in view day");
                                ChattingApp.nc.write("getDay#" + ChattingApp.username + "#" + addDayfile.getName()
                                        + "#" + getFileExtension(addDayfile));

                                sendFile(addDayfile);

                                break;
                            case "receiveDay":
                                System.out.println("It is in receive day");
                                receiveDay(split[1], split[3]);

                        }
                    } catch (IOException ex) {

                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        readThread.start();
        day.setVisible(true);
        whatTodo.setVisible(true);
        activeListTable.setVisible(false);
        typingMessagePane.setVisible(false);
        labelUser.setText(ChattingApp.username);

        userImage.setImage(new Image(ChattingApp.pp));
        groupMessagePane.setVisible(false);
        attachFiLe = false;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    private void activeList() {
        clientmap = (HashMap<String, String>) ChattingApp.nc.read();

        ObservableList<ActiveList> activeList = FXCollections.observableArrayList();

        for (String key : clientmap.keySet()) {
            System.out.println(key);
            activeList.add(new ActiveList(key));
        }
        activeListTable.setItems(activeList);
        activeListColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void showConfirmationMessage(String message) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);

    }

    private void messageListUpdate(String username, String message) {

        messageList.add(new MessageList(username, message));
        showMessageTable.setItems(messageList);
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));

    }

    private void messageListUpdate1(String username, String message) {

        messageList1.add(new MessageList1(username, message));
        showMessageTable1.setItems(messageList1);
        colUsername1.setCellValueFactory(new PropertyValueFactory<>("username"));
        colMessage1.setCellValueFactory(new PropertyValueFactory<>("message"));

    }

    @FXML
    private void attachFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extensionFiltertxt = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().addAll(extensionFilterjpg, extensionFilterJPG, extensionFiltertxt);
        attachFile = fileChooser.showOpenDialog(null);

        System.out.println(attachFile.getName() + " " + attachFile.getAbsolutePath());
        fileLabel.setText(attachFile.getName());
        attachFiLe = true;

    }

    private void sendFile(File file) throws IOException {

        System.out.println("It is in sendFile");
        byte[] contents = Files.readAllBytes(file.toPath());
        ChattingApp.nc.write(contents);

    }

    private void getFile(String fileName, String extension) throws IOException {

        System.out.println(fileName + " " + extension);

        getFile = new File("GetFile\\" + fileName + "." + extension);
        byte[] contents = (byte[]) ChattingApp.nc.read();
        Files.write(getFile.toPath(), contents);

    }

    private String getFileExtension(File file) {
        if (file.getName().indexOf(".") == -1) {
            return "";
        } else {
            return file.getName().substring(file.getName().length() - 3, file.getName().length());
        }
    }

    private void searchGroup() {

        List<String> group = (List<String>) ChattingApp.nc.read();
        ObservableList<GroupList> groupList = FXCollections.observableArrayList();
        for (int i = 0; i < group.size(); i++) {
            groupList.add(new GroupList(group.get(i)));
        }
        groupsTable.setItems(groupList);
        groups.setCellValueFactory(new PropertyValueFactory<>("groupName"));

    }

    private void members() {

        List<String> mem = (List<String>) ChattingApp.nc.read();
        ObservableList<MemberList> memberList = FXCollections.observableArrayList();
        for (int i = 0; i < mem.size(); i++) {
            memberList.add(new MemberList(mem.get(i)));
        }
        groupMembers.setItems(memberList);
        members.setCellValueFactory(new PropertyValueFactory<>("member"));
    }

    private void receiveDay(String fileName, String extension) throws IOException {
        System.out.println("eioseoooooooo");

        getFile = new File("Day\\" + fileName + "." + extension);
        byte[] contents = (byte[]) ChattingApp.nc.read();
        Files.write(getFile.toPath(), contents);

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println("File Name: " + file.getName());
                dayList.add(file.getName());
            }
        }
        stage = new Stage();
        VBox content = new VBox(5);
        ScrollPane scroller = new ScrollPane(content);
        ImageView[] images = new ImageView[3];
        ImageIcon[] icon = new ImageIcon[3];
        AnchorPane anchorPane = new AnchorPane();

        for (int i = 0; i < dayList.size(); i++) {
            File file = new File(dayList.get(i));
            images[i] = new ImageView(new Image(file.toURI().toString()));
            icon[i] = new ImageIcon(file.toURI().toString());

            Label lb = new Label(file.getName());
            lb.setFont(new javafx.scene.text.Font("Algerian", 14));
            content.getChildren().add(images[i]);
            content.getChildren().add(lb);
            scroller.setContent(content);
        }
        content.getChildren().add(anchorPane);
        Scene scene = new Scene(new BorderPane(scroller, null, null, null, null), 400, 400);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void groupClicked(MouseEvent event) {
        whatTodo.setText("Group Chatting");
        day.setVisible(false);
        activeListTable.setVisible(false);
        typingMessagePane.setVisible(false);
        groupMessagePane.setVisible(true);

        groupNameField.setVisible(false);
        addMemberField.setVisible(false);
        groupMembers.setVisible(false);
        groupsTable.setVisible(false);

        getGroupMessage.setVisible(true);
        gSend.setVisible(true);
        gSeeTheMember.setVisible(true);
    }

    @FXML
    private void gMessageSend(ActionEvent event) {
        if (searchGroup.getText().isEmpty() || getGroupMessage.getText().isEmpty()) {
            showConfirmationMessage("Please fill up the required field");
        } else {
            ChattingApp.nc.write("G#message#" + searchGroupName.getText() + "#" + ChattingApp.username + "#" + getGroupMessage.getText());
            messageListUpdate1(ChattingApp.username + " sent in " + searchGroupName.getText(), getGroupMessage.getText());
        }
    }

    @FXML
    private void seeTheMembers(ActionEvent event) {
        System.out.println(searchGroup.getText() + "lklleldl");
        groupMembers.setVisible(true);
        groupNameField.setVisible(false);
        addMemberField.setVisible(false);
        groupsTable.setVisible(false);
        if (searchGroup.getText().isEmpty()) {
            showConfirmationMessage("Please enter group name");
        } else {
            System.out.println(searchGroup.getText());
            ChattingApp.nc.write("G#members#" + searchGroupName.getText());
            searchGroupName.clear();
        }
    }

    @FXML
    private void addMemberFieldAction(ActionEvent event) {

        if (groupNameField.getText().isEmpty() && addMemberField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill up the fields.");
        } else {
            ChattingApp.nc.write("G#" + "createGroup#" + groupNameField.getText() + "#" + addMemberField.getText());
            addMemberField.clear();
        }

    }

    @FXML
    private void createGroupAction(ActionEvent event) {
        groupNameField.setVisible(true);
        addMemberField.setVisible(true);
        groupsTable.setVisible(false);
        groupMembers.setVisible(false);

        addMemberField.clear();
        groupNameField.clear();
    }

    @FXML
    private void searchGroupAction(ActionEvent event) {
        gSend.setVisible(false);
        gCreateGroup.setVisible(false);
        gSeeTheMember.setVisible(false);
        groupsTable.setVisible(true);
        getGroupMessage.setVisible(false);
        groupMembers.setVisible(false);
        searchGroupName.clear();

        ChattingApp.nc.write("G#searchGroup");
    }

    @FXML
    private void groupNameAction(ActionEvent event) {

        gSend.setVisible(true);
        gCreateGroup.setVisible(true);
        gSeeTheMember.setVisible(true);
        groupsTable.setVisible(false);
        getGroupMessage.setVisible(true);
        groupMembers.setVisible(false);
    }

    @FXML
    private void typeMessageClicked(MouseEvent event) {
        whatTodo.setText("Enjoy chatting");
        day.setVisible(false);
        activeListTable.setVisible(false);
        typingMessagePane.setVisible(true);
        groupMessagePane.setVisible(false);

    }

    @FXML
    private void showActiveListClicked(MouseEvent event) {
        whatTodo.setText("Active Users:");
        day.setVisible(false);
        activeListTable.setVisible(true);
        typingMessagePane.setVisible(false);
        groupMessagePane.setVisible(false);

        ChattingApp.nc.write("activelist#");
    }

    @FXML
    private void logOutClicked(MouseEvent event) {
        ChattingApp.nc.write("logout#");

        ImageIcon im = new ImageIcon("Icon\\logout.jpg");
        JOptionPane.showMessageDialog(null, "Successfully logged out", "Log out", JOptionPane.CANCEL_OPTION, im);
        System.out.println("iiiiii");

    }

    @FXML
    private void addDayAction(ActionEvent event) throws IOException {

        whatTodo.setText("My Day");
        day.setVisible(true);
        activeListTable.setVisible(false);
        typingMessagePane.setVisible(false);
        groupMessagePane.setVisible(false);

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extensionFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");

        fileChooser.getExtensionFilters().addAll(extensionFilterjpg, extensionFilterJPG, extensionFilterPNG, extensionFilterpng);
        addDayfile = fileChooser.showOpenDialog(null);

        System.out.println(addDayfile.getAbsolutePath());
        day.setImage(new Image(addDayfile.toURI().toString()));

    }

    @FXML
    private void messageSendAction(ActionEvent event) throws IOException {

        if (usernameText.getText().isEmpty()) {
            showConfirmationMessage("Please enter a username");
        } else if (messageText.getText().isEmpty() && !attachFile.exists()) {
            showConfirmationMessage("Please enter your message or attach file");
        } else {
            if (attachFiLe && !messageText.getText().isEmpty()) {
                ChattingApp.nc.write("C#" + ChattingApp.username + "#"
                        + usernameText.getText() + "#" + messageText.getText());
                messageListUpdate1(usernameText.getText(), messageText.getText());
                ChattingApp.nc.write("sendingFile#" + ChattingApp.username + "#" + usernameText.getText()
                        + "#" + attachFile.getName() + "#" + getFileExtension(attachFile));
                messageListUpdate1(usernameText.getText(), "(sent) " + attachFile.getName());
                sendFile(attachFile);

            } else if (!messageText.getText().isEmpty()) {

                System.out.println("1111");
                messageListUpdate1(usernameText.getText(), messageText.getText());
                ChattingApp.nc.write("C#" + ChattingApp.username + "#" + usernameText.getText() + "#" + messageText.getText());

            } else if (attachFiLe) {
                messageListUpdate1(usernameText.getText(), "(sent) " + attachFile.getName());
                ChattingApp.nc.write("sendingFile#" + ChattingApp.username + "#" + usernameText.getText()
                        + "#" + attachFile.getName() + "#" + getFileExtension(attachFile));

                sendFile(attachFile);

            }

        }

        usernameText.clear();
        messageText.clear();
        attachFiLe = false;
        fileLabel = null;
    }

    @FXML
    private void groupMessagePaneClicked(MouseEvent event) {
        groupMembers.setVisible(false);
        groupsTable.setVisible(false);

        getGroupMessage.clear();
        searchGroupName.clear();
        groupNameField.clear();
        searchGroupName.setVisible(true);
        getGroupMessage.setVisible(true);
        gSend.setVisible(true);
        gCreateGroup.setVisible(true);
        gSeeTheMember.setVisible(true);

    }

    @FXML
    private void typingMessagePaneClicked(MouseEvent event) {
        usernameText.clear();
        messageText.clear();
    }

    @FXML
    private void searchGroupNameClicked(MouseEvent event) {
        gSend.setVisible(true);
        gCreateGroup.setVisible(true);
        gSeeTheMember.setVisible(true);
        groupsTable.setVisible(false);
        getGroupMessage.setVisible(true);
        groupMembers.setVisible(false);

    }

    @FXML
    private void viewDayAction(ActionEvent event) {
        typingMessagePane.setVisible(false);
        groupMessagePane.setVisible(false);
        activeListTable.setVisible(false);
        day.setVisible(true);
        day.setImage(new Image(addDayfile.toURI().toString()));

        ChattingApp.nc.write("viewDay#");
    }

    @FXML
    private void userNameTclicked(MouseEvent event) {
        fileLabel.setText(" ");
        messageText.clear();
    }

}
