
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author joshdebest
 */
@Named(value = "event")
@SessionScoped
@ManagedBean
public class Event implements Serializable{
    private DBConnect dbConnect = new DBConnect();
    
    private int id;
    private int uid;
    private int gameID;
    private String eventType;
    

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getUid() {return uid;}
    public void setUid(int uid) {this.uid = uid;}
    public int getGameID() {return gameID;}
    public void setGameID(int gameID) {this.gameID = gameID;}
    public String getEventType() {return eventType;}
    public void setEventType(String eventType) {this.eventType = eventType;} 
   
    
    public String addEvent() throws SQLException{
        Connection con = dbConnect.getConnection();
        PreparedStatement ps;
        
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        ps = con.prepareStatement(
                        "INSERT INTO die_event (eventType, uid, gameID) VALUES (?, ?, ?)");
        
        ps.setString(1, eventType);
        ps.setInt(2, uid);
        ps.setInt(3, gameID);
        
        ps.executeUpdate();
        con.close();
        
        return "success";
    }
    
    public String[] getTypes(){
        String[] choices = {"sink", "tink","self sink"};
        return choices;
    }
    
}
