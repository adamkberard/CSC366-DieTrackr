
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
    
    private static int updatingT1Score;
    private static int updatingT2Score;
    
    private String teamOneErrorMessage;
    private String teamTwoErrorMessage;
    private String teamOneScoreError;
    private String teamTWOScoreError;
    
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
    public String getTeamOneScoreError() {return teamOneScoreError;}
    public String getTeamTWOScoreError() {return teamTWOScoreError;}
    public void setTeamTWOScoreError(String teamTWOScoreError) {this.teamTWOScoreError = teamTWOScoreError;}
    


    
    public Game(){}
    
    public Game(String team_1, String team_2, boolean t1c, boolean t2c, int game_id){
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.t_1_confirmed = t1c;
        this.t_2_confirmed = t2c;
        this.id = game_id;
    }
    
    public Game(String team_1, String team_2, int t1s, int t2s, int game_id){
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.t_1_score = t1s;
        this.t_2_score = t2s;
        this.t_1_confirmed = true;
        this.t_2_confirmed = true;
        this.id = game_id;
    }
            
    public void validateTeamOneScore(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        updatingT1Score = Integer.parseInt(value.toString());
    }
    
    public void validateTeamTwoScore(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        updatingT2Score = Integer.parseInt(value.toString());
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
        
        System.out.println(ps);
        
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
        System.out.println("AT END");
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        
        ps = con.prepareStatement("UPDATE die_game SET status = 'Done', team_1_score = ?, team_2_score = ? WHERE die_game.id = ?");
        ps.setInt(1, updatingT1Score);
        ps.setInt(2, updatingT2Score);
        ps.setInt(3, id);
        System.out.println("THIS HERE 5");
        System.out.println(ps);
        ps.executeUpdate();
        
        con.close();
        return "done";
    }
    
    public String updateGame() throws SQLException{
        System.out.println("AT END");
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        
        ps = con.prepareStatement("UPDATE die_game SET team_1_score = ?, team_2_score = ? WHERE die_game.id = ?");
        ps.setInt(1, updatingT1Score);
        ps.setInt(2, updatingT2Score);
        ps.setInt(3, id);
        System.out.println("THIS HERE 5");
        System.out.println(ps);
        ps.executeUpdate();
        
        con.close();
        return "pause";
    }
    
    public List<Game> getAllGames() throws SQLException{
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
        
        List<Game> games = new ArrayList<>();
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM die_game WHERE (die_game.team_1 IN (SElECT die_team.name FROM die_team WHERE (die_team.player_1 = ? AND die_team.p_1_confirmed) OR (die_team.player_2 = ? AND die_team.p_2_confirmed))) OR (die_game.team_2 IN (SElECT die_team.name FROM die_team WHERE (die_team.player_1 = ? AND die_team.p_1_confirmed) OR (die_team.player_2 = ? AND die_team.p_2_confirmed))) AND die_game.status = 'Done'");
                
        ps.setInt(1, login.getUser().getUid());
        ps.setInt(2, login.getUser().getUid());
        ps.setInt(3, login.getUser().getUid());
        ps.setInt(4, login.getUser().getUid());
        
        ResultSet result = ps.executeQuery();
        
        String team_1, team_2;
        int t1s, t2s, game_id;
        
        while(result.next()) {
            team_1 = result.getString("team_1");
            team_2 = result.getString("team_2");
            t1s = result.getInt("team_1_score");
            t2s = result.getInt("team_2_score");
            game_id = result.getInt("id");
            
            games.add(new Game(team_1, team_2, t1s, t2s, game_id));        
        }
        result.close();
        con.close();
        return games;
    }
}