
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class Register{

    private String username;
    private String password;
    private String email;
    private String role;
    private String passwordErrorMessage;
    private String usernameErrorMessage;
    
    private DBConnect dbConnect = new DBConnect();  
    private UIInput loginUI = new UIInput();

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getPasswordErrorMessage() {return passwordErrorMessage;}
    public String getUsernameErrorMessage() {return usernameErrorMessage;}
    public UIInput getLoginUI() {return loginUI;}
    public void setLoginUI(UIInput loginUI) {this.loginUI = loginUI;}
    
    public void validateUsername(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException{
        
        String returnable = Validation.validateUsername(value.toString());
        
        if(!(returnable.equals("valid"))){
            usernameErrorMessage = returnable;
            FacesMessage errorMessage = new FacesMessage(usernameErrorMessage);
            throw new ValidatorException(errorMessage);
        }       
    }
    
    public void validatePassword(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        String returnable = Validation.validatePassword(value.toString());
        
        if(!(returnable.equals("valid"))){
            passwordErrorMessage = returnable;
            FacesMessage errorMessage = new FacesMessage(passwordErrorMessage);
            throw new ValidatorException(errorMessage);
        }
        password = value.toString();
    }
    
    public void validatePasswordTwo(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        
        if(!(value.toString().equals(password))){
            passwordErrorMessage = "Passwords do not match.";
            FacesMessage errorMessage = new FacesMessage(passwordErrorMessage);
            throw new ValidatorException(errorMessage);
        }
    }

    public String go() throws ValidatorException, SQLException {
        Connection con = dbConnect.getConnection();
        role = "player";

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "INSERT INTO die_user (username, password, role) VALUES (?, ?, ?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, role);
        
        ps.executeUpdate();
        con.close();
        
        username = null;
        password = null;
        email = null;
        role = null;

        return "success";
    }
}
