package com.server;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;


public class UserAuthenticator extends com.sun.net.httpserver.BasicAuthenticator{
    
    
    private MessageDatabase db = null;

    public UserAuthenticator(String realm) {
        super("warning");
        db = MessageDatabase.getInstance();
    }

    @Override
    public boolean checkCredentials(String username, String password) {

        System.out.println("checking user: " + username + " " + password + "\n");

        boolean isValidUser;
        try{
            isValidUser = db.authenticateUser(username, password);
        }catch(SQLException e){
            return false;
        }
        return isValidUser;
        
    
    }

    public boolean addUser(String userName, String password, String email) throws JSONException, SQLException{


        boolean result = db.setUser(new JSONObject().put("username", userName).put("password", password).put("email", email));
        if(!result){
            System.out.println("Cannot register user");
            return false;
        }
        System.out.println(userName + " registered");
        return true;

    }
}
