package com.server;

import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

public class RegistrationHandler implements HttpHandler {
    
    UserAuthenticator userAuthenticator;

    public RegistrationHandler(UserAuthenticator userAuthenticator){

        this.userAuthenticator = userAuthenticator;

    }
    @Override
    public void handle(HttpExchange t) throws IOException{

        if(t.getRequestMethod().equalsIgnoreCase("POST")){
            handlePost(t);
        }
        else{
            OutputStream outputStream = t.getResponseBody();
            String text;
            text = "Not Supported";
            byte [] bytes = text.getBytes("UTF-8");
            t.sendResponseHeaders(400, bytes.length);
            outputStream.write(text.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }

    }

    private void handlePost(HttpExchange httpExchange) throws IOException{

        Headers headers = httpExchange.getRequestHeaders();
        String contentType = "";
        String response = "";
        int code = 200;
        JSONObject obj = null;

        try{
            if(headers.containsKey("Content-Type")){
                contentType = headers.get("Content-Type").get(0);
                System.out.println("Content-Type is available");
            }else{
                System.out.println("No content type");
                code = 411;
                response = "No content type in request";
            }
            if(contentType.equalsIgnoreCase("application/json")) {
                System.out.println("Content-type is application/json");
                InputStream inputStream = httpExchange.getRequestBody();
                String newUser = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));               
                inputStream.close();
            

                if(newUser == null || newUser.length() == 0){
                    code = 412;
                    response ="no user credentials";
                }
                else{
                    try{
                        obj = new JSONObject(newUser);
                    }catch(JSONException e){
                        System.out.println("json parse error, faulty user json");
                    }
                    if(obj.getString("username").length() == 0  || obj.getString("password").length() == 0){
                        code = 413;
                        response ="no proper user credentials";
                    }else{
                        System.out.println("registering user " + obj.getString("username") + " " + obj.getString("password"));
                        Boolean checkUser = userAuthenticator.addUser(obj.getString("username"), obj.getString("password"), obj.getString("email"));
                        if(checkUser == false){
                            code = 405;
                            response ="user already exist";
                        }else{

                            code = 200;
                            response = "User registered";
                            System.out.println("Registration successful, writing response");
                        }
                    }   
                }
                    
                    byte[] bytes = response.getBytes("UTF-8");
                    httpExchange.sendResponseHeaders(code, bytes.length);
                    OutputStream outputStream = httpExchange.getResponseBody();
                    outputStream.write(bytes);
                    outputStream.close();
            }
            else{
                code = 407;
                response = "content type is not application/json";
            }

        }catch (Exception e){
            System.out.println(e.getStackTrace());
            code = 500;
            response = "Internal server error";
        }
        if (code >= 400) {
            byte[] bytes = response.getBytes("UTF-8");
            httpExchange.sendResponseHeaders(code, bytes.length);
            OutputStream stream = httpExchange.getResponseBody();
            stream.write(response.getBytes());
            stream.close();
        }
    }   
}
