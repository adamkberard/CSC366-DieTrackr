import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.el.ELContext;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;


@Named(value = "user")
@SessionScoped
@ManagedBean
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
    public boolean isAdmin(){return role.equals("admin");}
    public boolean isPlayer(){return role.equals("player");}
    
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
        returnable.add("View All Teams");
        returnable.add("View Team Requests");
        returnable.add("Create Team");
        returnable.add("End Game");
        returnable.add("See Passed Games");
        returnable.add("Create Game");
        return returnable;
    }
    
    public List<Team> getRequests() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement(
                        "SElECT * FROM die_team WHERE ((die_team.player_1 = ? AND NOT die_team.p_1_confirmed) OR"
                                                   + " (die_team.player_2 = ? AND NOT die_team.p_2_confirmed))");
        
        ps.setInt(1, login.getUser().getUid());
        ps.setInt(2, login.getUser().getUid());
        
        ResultSet result = ps.executeQuery();
        
        List<Team> requestedTeams = new ArrayList<>();
        int p1, p2;
        boolean p1c, p2c;
        String teamName;
        
        while(result.next()) {
            p1 = result.getInt("player_1");
            p2 = result.getInt("player_2");
            p1c = result.getBoolean("p_1_confirmed");
            p2c = result.getBoolean("p_2_confirmed");
            teamName = result.getString("name");
            requestedTeams.add(new Team(p1, p2, p1c, p2c, teamName));
        }
        result.close();
        con.close();
        return requestedTeams;
    }
    
    public static User getUserFromId(int uid) throws SQLException{
        DBConnect newDBConnect = new DBConnect();
        Connection con = newDBConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement(
                        "SElECT * FROM die_user WHERE die_user.uid = ?");
        ps.setInt(1, uid);
        
        ResultSet result = ps.executeQuery();
        
        if(result.next()) {
            String tempUsername = result.getString("username");
            String tempRole = result.getString("role");
            int tempUid = result.getInt("uid");
            result.close();
            con.close();
            return new User(tempUsername, tempRole, tempUid);
        }
        return null;
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
            case "View Team Requests":
                return "viewTeamRequests";
            case "End Game":
                return "endGame";
            case "See Passed Games":
                return "seePassedGames";
            case "See Pending Invites":
                return "seePendingInvites";
            case "View All Teams":
                return "viewAllTeams";
            default:
                return null;
        }
    }
}
