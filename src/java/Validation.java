
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
    
    public static String validateUsername(String username, int id) throws SQLException{
        Connection con = dbConnect.getConnection();
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        PreparedStatement ps = con.prepareStatement(
                        "SElECT die_user.id FROM user WHERE die_user.username = ? AND die_user.id != ?");
        ps.setString(1, username);
        ps.setInt(2, id);
        
        ResultSet result = ps.executeQuery();

        if(result.next()) {
            result.close();
            con.close();
            return "Username is already taken.";
        }
        
        return "valid";
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
