
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aberard
 */
public class Validation {
    
    private static DBConnect dbConnect = new DBConnect();
    
    
    public static String validateUsername(String username) throws SQLException{
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        PreparedStatement ps = con.prepareStatement(
                        "SElECT die_user.uid FROM die_user WHERE die_user.username = ?");
        ps.setString(1, username);
        
        ResultSet result = ps.executeQuery();

        if(result.next()) {
            result.close();
            con.close();
            return "Username is already taken.";
        }
        
        return "valid";
    }
    
    public static boolean teamNameAvailable(String teamName) throws SQLException{
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement(
                        "SElECT * FROM die_team WHERE die_team.name = ?");
        ps.setString(1, teamName);
        
        ResultSet result = ps.executeQuery();
        
        return !result.next();
    }
    
    public static int userExists(String username) throws SQLException{
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement(
                        "SElECT die_user.uid FROM die_user WHERE die_user.username = ?");
        ps.setString(1, username);
        
        ResultSet result = ps.executeQuery();
        
        if(result.next()) {
            int returnable = result.getInt("uid");
            result.close();
            con.close();
            return returnable;
        }
        return -1;
    }
    
    public static boolean teamExists(String team) throws SQLException{
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement(
                        "SElECT * FROM die_team WHERE die_team.name = ?");
        ps.setString(1, team);
        
        System.out.println("HERE 4352");
        System.out.println(ps);
        
        ResultSet result = ps.executeQuery();
        
        if(result.next()) {
            result.close();
            con.close();
            return true;
        }
        return false;
    }
    
    public static String validatePassword(String password) {
        if(password.length() < 6){
            return "Password must be six characters long,";
        }
        if(!(Validation.containsDigit(password))){
            return "Password must contain at least one number.";
        }
        return "valid";
    }
        
    public static boolean containsDigit(final String aString){
        return aString != null && !aString.isEmpty() && aString.chars().anyMatch(Character::isDigit);
    }
    
    public static Calendar getCalObj(String date_string){
        String[] parts = date_string.split("/");
        
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        
        Calendar date = Calendar.getInstance();
        date.set(year, month - 1, day);
        return date;
    }
}
