
package FXMLFile;

/**
 *
 * @author Sakibur Reza
 */
public class MessageList1 {
      private String username,message;

    public MessageList1(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
      
}
