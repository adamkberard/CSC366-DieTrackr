
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
 * @author stanchev
 */
@Named(value = "login")
@SessionScoped
@ManagedBean
public class Login implements Serializable {

    private DBConnect dbConnect = new DBConnect();
    private UIInput loginUI;
    
    private String username;
    private String password;
    
    private User user;
    private Register registration = new Register();
    
    public UIInput getLoginUI() {return loginUI;}
    public void setLoginUI(UIInput loginUI) {this.loginUI = loginUI;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public User getUser() {return user;}
    public Register getRegistration() {return registration;}    
    
    public void Login(){
        username = null;
        password = null;
        user = new User();
        registration = new Register();
    }

    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        System.out.println("Here 1");
        username = loginUI.getLocalValue().toString();
        System.out.println("Here 4");
        password = value.toString();
        
        int uid;
        String role;
        
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select die_user.uid, die_user.role from die_user where die_user.username = ? AND die_user.password = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        System.out.println("Here 2");
        //get customer data from database

        ResultSet result = ps.executeQuery();

        if(result.next()) {
            uid = result.getInt("uid");
            role = result.getString("role");
            result.close();
            con.close();
            System.out.println("Here 3");
            user = new User(username, role, uid);
            return; 
        }
        
        FacesMessage errorMessage = new FacesMessage("Wrong login/password");
        throw new ValidatorException(errorMessage);       
    }

    public String go() {
        username = null;
        password = null;
        registration = null;
        return "success";
    }
     
    public String register() throws ValidatorException, SQLException {
        String temp = registration.register();
        username = null;
        password = null;
        if(temp.equals("success")){
            String temmpUser = registration.getUsername();
            String tempRole = registration.getRole();
            int tempUid = registration.getUid();
            registration = null;
            user = new User(temmpUser, tempRole, tempUid);
            return "success";
        }
        else{
            registration = null;
            return "fail";
        }
    }
    
    public String logout() {
        Util.invalidateUserSession();
        return "logout";
    }

}
