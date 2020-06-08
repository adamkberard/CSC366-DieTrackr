import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aberard
 */

@Named(value = "team")
@SessionScoped
@ManagedBean
public class Team implements Serializable {
    private DBConnect dbConnect = new DBConnect();
    
    private String player_one = null;
    private String player_two = null;
    private int player_one_id;
    private int player_two_id;
    private boolean player_one_confirmed = false;
    private boolean player_two_confirmed = false;
    private String name = null;
    
    private String playerOneErrorMessage;
    private String playerTwoErrorMessage;
    private String nameErrorMessage;
    
    public Team(){
        player_one_id = -1;
        player_two_id = -1;
    }
    
    public Team(int p1id, int p2id, boolean p1c, boolean p2c, String aName){
        player_one = null;
        player_two = null;
        player_one_id = p1id;
        player_two_id = p2id;
        player_one_confirmed = p1c;
        player_two_confirmed = p2c;
        name = aName;
    }
    
    public String getPlayer_one() throws SQLException, SQLException, SQLException {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        player_one = login.getUser().getUsername();
        return player_one;
    }
    public void setPlayer_one(String player_one) {this.player_one = player_one;}
    public String getPlayer_two() {return player_two;}
    public void setPlayer_two(String player_two) {this.player_two = player_two;}
    public boolean isPlayer_one_confirmed() {return player_one_confirmed;}
    public void setPlayer_one_confirmed(boolean player_one_confirmed) {this.player_one_confirmed = player_one_confirmed;}
    public boolean isPlayer_two_confirmed() {return player_two_confirmed;}
    public void setPlayer_two_confirmed(boolean player_two_confirmed) {this.player_two_confirmed = player_two_confirmed;}
    public int getPlayer_one_id() {return player_one_id;}
    public void setPlayer_one_id(int player_one_id) {this.player_one_id = player_one_id;}
    public int getPlayer_two_id() {return player_two_id;}
    public void setPlayer_two_id(int player_two_id) {this.player_two_id = player_two_id;}
    public String getPlayerOneErrorMessage() {return playerOneErrorMessage;}
    public void setPlayerOneErrorMessage(String playerOneErrorMessage) {this.playerOneErrorMessage = playerOneErrorMessage;}
    public String getPlayerTwoErrorMessage() {return playerTwoErrorMessage;}
    public void setPlayerTwoErrorMessage(String playerTwoErrorMessage) {this.playerTwoErrorMessage = playerTwoErrorMessage;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getNameErrorMessage() {return nameErrorMessage;}
    public void setNameErrorMessage(String nameErrorMessage) {this.nameErrorMessage = nameErrorMessage;}
    
    
    public void validatePlayerOne(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        System.out.println("P1 Valid");
        player_one = value.toString();
        int temp = Validation.userExists(player_one);
        
        if(temp == -1){
            playerOneErrorMessage = "User does not exist.";
            FacesMessage errorMessage = new FacesMessage(playerOneErrorMessage);
            throw new ValidatorException(errorMessage);
        }
        player_one_id = temp;
    }
    
    public void validateName(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        name = value.toString();
        if(!Validation.teamNameAvailable(player_one)){
            nameErrorMessage = "Team name already taken.";
            FacesMessage errorMessage = new FacesMessage(nameErrorMessage);
            throw new ValidatorException(errorMessage);
        }
    }
    
    public void validatePlayerTwo(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        System.out.println("P2 Valid");
        player_two = value.toString();
        int temp = Validation.userExists(player_two);
        
        if(temp == -1){
            playerTwoErrorMessage = "User does not exist.";
            FacesMessage errorMessage = new FacesMessage(playerTwoErrorMessage);
            throw new ValidatorException(errorMessage);
        }
        
        if(player_two.equals(player_one)){
            playerTwoErrorMessage = "Teams must have two different players.";
            FacesMessage errorMessage = new FacesMessage(playerTwoErrorMessage);
            throw new ValidatorException(errorMessage);
        }
        
        player_two_id = temp;
    }
    
    public List<Team> getAllTeams() throws SQLException{
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        List<Team> teams = new ArrayList<>();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        ps = con.prepareStatement(
                    "SELECT * FROM die_team WHERE p_1_confirmed AND p_2_confirmed");

        ResultSet result = ps.executeQuery();
        
        int tempOneId, tempTwoId;
        String tempName;
        while(result.next()){    
            tempOneId = result.getInt("player_1");
            tempTwoId = result.getInt("player_2");
            tempName = result.getString("name");
            teams.add(new Team(tempOneId, tempTwoId, true, true, tempName));
        }
        con.close();
        
        return teams;
    }
    
    public String confirmRequest() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        if(player_one_id == login.getUser().getUid()){
            ps = con.prepareStatement(
                        "UPDATE die_team SET p_1_confirmed = TRUE WHERE player_1 = ? AND player_2 = ?");
        }
        else{
            ps = con.prepareStatement(
                        "UPDATE die_team SET p_2_confirmed = TRUE WHERE player_1 = ? AND player_2 = ?");
        }
        
        ps.setInt(1, player_one_id);
        ps.setInt(2, player_two_id);
        System.out.println("CUrrent");
        System.out.println(ps);
        
        ps.executeUpdate();
        con.close();
        return "success";
    }
    
    public String denyPlayer() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        player_one_id = Validation.userExists(player_one);

        ps = con.prepareStatement(
                    "DELETE FROM die_team WHERE player_1 = ? AND player_2 = ?");
        
        ps.setInt(1, player_one_id);
        ps.setInt(2, player_two_id);
        
        ps.executeUpdate();
        con.close();
        return "success";
    }
    
    public String sendRequests() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        player_one_id = Validation.userExists(player_one);

        ps = con.prepareStatement(
                        "INSERT INTO die_team (player_1, player_2, p_1_confirmed, p_2_confirmed, name) VALUES (?, ?, ?, ?, ?)");
        
        ps.setInt(1, player_one_id);
        ps.setInt(2, player_two_id);
        player_one_confirmed = login.getUser().isPlayer();
        player_two_confirmed = false;
        ps.setBoolean(3, player_one_confirmed);
        ps.setBoolean(4, player_two_confirmed);
        ps.setString(5, name);
        
        System.out.println(ps);
        
        ps.executeUpdate();
        con.close();

        return "success";
    }
    
}