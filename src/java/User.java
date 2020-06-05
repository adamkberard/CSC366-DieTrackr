import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private DBConnect dbConnect = new DBConnect();
    private String username = null;
    private String role = "player";
    private int uid = -1;
    
    private String choice;

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public int getUid() {return uid;}
    public void setUid(int uid) {this.uid = uid;}
    public String getChoice() {return choice;}
    public void setChoice(String choice) {this.choice = choice;}
    
    
    public User(){}
    public User(String aUsername, String aRole){
        username = aUsername;
        role = aRole;
    }
    public User(String aUsername, String aRole, int aUid){
        username = aUsername;
        role = aRole;
        uid = aUid;
    }
    
    public List<String> getHomepageChoices(){
        List<String> returnable = new ArrayList<>();
        returnable.add("Start Game");
        returnable.add("End Game");
        returnable.add("See Passed Games");
        returnable.add("Create Game");
        return returnable;
    }
    
    public List<User> getUsers() throws SQLException{
        Connection con = dbConnect.getConnection();
        String niceStr;

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select die_user.username, die_user.role from die_user order by die_user.username");

        //get user data from database
        ResultSet result = ps.executeQuery();

        List<User> userList = new ArrayList<>();
        while (result.next()) {
            userList.add(new User(username, role));
        }
        result.close();
        con.close();
        
        return userList;
    }
    
    public String transition() {

        switch (choice) {
            case "Start Game":
                return "startGame";
            case "Create Team":
                return "createTeam";
            case "End Game":
                return "endGame";
            case "See Passed Games":
                return "seePassedGames";
            default:
                return null;
        }
    }
}
