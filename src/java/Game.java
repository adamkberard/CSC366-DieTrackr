
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
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
@Named(value = "game")
@SessionScoped
@ManagedBean
public class Game implements Serializable{
    private DBConnect dbConnect = new DBConnect();
    
    private int id;
    private String team_1;
    private String team_2;
    private boolean t_1_confirmed = false;
    private boolean t_2_confirmed = false;
    private int t_1_score = 0;
    private int t_2_score = 0;
    private String status;
    
    private String teamOneErrorMessage;
    private String teamTwoErrorMessage;
    

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getTeam_1() {return team_1;}
    public void setTeam_1(String team_1) {this.team_1 = team_1;}
    public String getTeam_2() {return team_2;}
    public void setTeam_2(String team_2) {this.team_2 = team_2;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getTeamOneErrorMessage() {return teamOneErrorMessage;}
    public void setTeamOneErrorMessage(String teamOneErrorMessage) {this.teamOneErrorMessage = teamOneErrorMessage;}
    public String getTeamTwoErrorMessage() {return teamTwoErrorMessage;}
    public void setTeamTwoErrorMessage(String teamTwoErrorMessage) {this.teamTwoErrorMessage = teamTwoErrorMessage;}
    public boolean isT_1_confirmed() {return t_1_confirmed;}
    public void setT_1_confirmed(boolean t_1_confirmed) {this.t_1_confirmed = t_1_confirmed;}
    public boolean isT_2_confirmed() {return t_2_confirmed;}
    public void setT_2_confirmed(boolean t_2_confirmed) {this.t_2_confirmed = t_2_confirmed;}
    public int getT_1_score() {return t_1_score;}
    public void setT_1_score(int t_1_score) {this.t_1_score = t_1_score;}
    public int getT_2_score() {return t_2_score;}
    public void setT_2_score(int t_2_score) {this.t_2_score = t_2_score;}
    
    public Game(){}
    
    public Game(String team_1, String team_2, boolean t1c, boolean t2c, int game_id){
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.t_1_confirmed = t1c;
        this.t_2_confirmed = t2c;
        this.id = game_id;
    }
    
    public void validateTeamOne(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{

        team_1 = value.toString();
        boolean temp = Validation.teamExists(team_1);
        
        if(!temp){
            teamOneErrorMessage = "Team does not exist. Capitalization matters";
            FacesMessage errorMessage = new FacesMessage(teamOneErrorMessage);
            throw new ValidatorException(errorMessage);
        }
    }
    
    public void validateTeamTwo(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        team_2 = value.toString();
        boolean temp = Validation.teamExists(team_2);
        
        if(!temp){
            teamTwoErrorMessage = "Team does not exist. Capitalization matters";
            FacesMessage errorMessage = new FacesMessage(teamTwoErrorMessage);
            throw new ValidatorException(errorMessage);
        }
        
        if(team_2.equals(team_1)){
            teamTwoErrorMessage = "Teams must be different.";
            FacesMessage errorMessage = new FacesMessage(teamTwoErrorMessage);
            throw new ValidatorException(errorMessage);
        }
    }
            
    public void validateTeamOneScore(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        team_2 = value.toString();
        boolean temp = Validation.teamExists(team_2);
        
        if(!temp){
            teamTwoErrorMessage = "Team does not exist. Capitalization matters";
            FacesMessage errorMessage = new FacesMessage(teamTwoErrorMessage);
            throw new ValidatorException(errorMessage);
        }
        
        if(team_2.equals(team_1)){
            teamTwoErrorMessage = "Teams must be different.";
            FacesMessage errorMessage = new FacesMessage(teamTwoErrorMessage);
            throw new ValidatorException(errorMessage);
        }
    }
    
    public String startGame() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        ps = con.prepareStatement(
                        "INSERT INTO die_game (team_1, team_2, t_1_confirmed, t_2_confirmed, status) VALUES (?, ?, ?, ?, ?)");
        
        ps.setString(1, team_1);
        ps.setString(2, team_2);
        t_1_confirmed = login.getUser().isPlayer();
        t_2_confirmed = false;
        ps.setBoolean(3, t_1_confirmed);
        ps.setBoolean(4, t_2_confirmed);
        ps.setString(5, "Pending");
        
        ps.executeUpdate();
        con.close();
        
        return "start";
    }
    
    public String confirmRequest() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        System.out.println("HERE IS STUPID");
        
        if(Team.userInTeam(login.getUser().getUid(), team_1)){
            ps = con.prepareStatement(
                        "UPDATE die_game SET t_1_confirmed = TRUE WHERE id = ?");
        }
        else{
            ps = con.prepareStatement(
                        "UPDATE die_game SET t_2_confirmed = TRUE WHERE id = ?");
        }
        
        ps.setInt(1, id);
        
        ps.executeUpdate();
        
        // Start game if both have confirmed
        ps = con.prepareStatement("SELECT * FROM die_game WHERE die_game.id = ?");
        ps.setInt(1, id);
        
        ResultSet result = ps.executeQuery();
        
        result.next();
        
        if(result.getBoolean("t_1_confirmed") && result.getBoolean("t_2_confirmed")){
            ps = con.prepareStatement("UPDATE die_game SET status = 'In Progress' WHERE die_game.id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        
        con.close();
        return "accept";
    }
    
    public String denyRequest() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        ps = con.prepareStatement(
                    "DELETE FROM die_game WHERE die_game.id = ?");
        
        ps.setInt(1, id);
        
        ps.executeUpdate();
        con.close();
        return "accept";
    }
    
    public String endGame() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        
        ps = con.prepareStatement("UPDATE die_game SET status = 'Done', team_1_score = ?, team_2_score =? WHERE die_game.id = ?");
        ps.setInt(1, t_1_score);
        ps.setInt(2, t_2_score);
        ps.setInt(3, id);
        ps.executeUpdate();
        
        con.close();
        return "done";
    }
    
    public String updateGame() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        
        ps = con.prepareStatement("UPDATE die_game SET status = 'Paused', team_1_score = ?, team_2_score = ? WHERE die_game.id = ?");
        ps.setInt(1, t_1_score);
        ps.setInt(2, t_2_score);
        ps.setInt(3, id);
        ps.executeUpdate();
        
        con.close();
        return "pause";
    }
    
    
}
