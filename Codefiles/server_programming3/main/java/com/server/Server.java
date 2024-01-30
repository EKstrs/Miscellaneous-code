package com.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Server implements HttpHandler {

    ArrayList<WarningMessage> messages = new ArrayList<>();
    WarningMessage wmess = new WarningMessage();
    JSONObject query;

    private Server() {
    }
    @Override
    public void handle(HttpExchange t) throws IOException {

    System.out.println("Request handled in thread " + Thread.currentThread().getId());
    //implement GET and POST handling 
       if(t.getRequestMethod().equalsIgnoreCase("POST")){
            handleRequestPOST(t);
       }else if(t.getRequestMethod().equalsIgnoreCase("GET")){
            handleRequestGET(t);
       }else{
            handleRequestOTHER(t);
       }
    }


    private void handleRequestPOST(HttpExchange httpExchange) throws IOException{
        WarningMessage wmess = new WarningMessage();
        Headers headers = httpExchange.getRequestHeaders();
        String contentType = "";
        String response = "";
        String message = "";
        int code = 200;
        JSONObject obj = null;
        query = null;

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
                message = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                inputStream.close();
            }
            if(message == null || message.length() == 0){
                code = 412;
                response ="no message";
            }
            else{
                try{
                    obj = new JSONObject(message);
                }catch(JSONException e){
                    System.out.println("json parse error, faulty message json");
                    httpExchange.sendResponseHeaders(400, -1);
                }
            }
 

            if(obj.has("query")){
                query = obj;
                handleRequestGET(httpExchange);
            }
            
            try{
                String aika = obj.getString("sent");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                ZonedDateTime now = ZonedDateTime.parse(aika, formatter);
                String dateText = now.format(formatter);
                OffsetDateTime offsetdt = OffsetDateTime.parse(dateText);
                LocalDateTime ldt = offsetdt.toLocalDateTime();
                wmess.setSent(ldt);
                try{
                    wmess.setNick(obj.getString("nickname"));
                    wmess.setLatitude(obj.getDouble("latitude"));
                    wmess.setLongitude(obj.getDouble("longitude"));
                    wmess.setDangertype(obj.getString("dangertype"));
                    try{
                        wmess.setAreacode(obj.getString("areacode"));
                        wmess.setPhonenumber(obj.getString("phonenumber"));
                    }catch(JSONException e){
                        System.out.println("No areacode or phonenumber");
                    }
                    String testi = wmess.getDangertype();
                    if(!testi.equals("Reindeer") && !testi.equals("Moose") && !testi.equals("Deer") && !testi.equals("Other")){
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                    try{
                        wmess.setWeather(obj.getString("weather"));
                    }catch(JSONException e){
                        System.out.println("No weather");
                    }
            
                    messages.add(wmess);
                }
                catch(JSONException e){
                    e.printStackTrace();
                    httpExchange.sendResponseHeaders(400, -1);
                }
                try{
                    MessageDatabase db = MessageDatabase.getInstance();
                    db.addMessage(wmess);
                }catch(SQLException e){
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, -1);
            
            }
            catch(DateTimeParseException e){
                e.printStackTrace();
                httpExchange.sendResponseHeaders(400, -1);
            }
    
            
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            code = 500;
            response = "Internal server error";
        }
    }





    private void handleRequestGET(HttpExchange httpExchange) throws IOException{
        int code = 200;

        if(messages == null){
            code = 204;
            httpExchange.sendResponseHeaders(code, -1);
        }
        else{ 
            JSONArray responseMessages = new JSONArray();
            MessageDatabase db = MessageDatabase.getInstance();
            try{
                responseMessages = db.getMessage(query);
            }
            catch(SQLException e){

            }
            String text = responseMessages.toString();
            byte [] bytes = text.getBytes("UTF-8");
            httpExchange.sendResponseHeaders(200, bytes.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }
        
    }

    private void handleRequestOTHER(HttpExchange httpExchange) throws IOException{
        OutputStream outputStream = httpExchange.getResponseBody();
        String text;
        text = "Not Supported";
        byte [] bytes = text.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(400, bytes.length);
        outputStream.write(text.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    } 

    private static SSLContext serverSSLContext(String file, String pw) throws Exception{

        char[] passphrase = pw.toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(file), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return ssl;
        
    }
    
    long dateAsInt(LocalDateTime sent){
        return sent.toInstant(ZoneOffset.UTC).toEpochMilli();
    }


    public static void main(String[] args) throws Exception {
        //create the https server to port 8001 with default logger
        try{
            HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);
            UserAuthenticator userAuthenticator = new UserAuthenticator("warning");
            SSLContext sslContext = serverSSLContext(args[0], args[1]);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext){
                public void configure(HttpsParameters params){
                    InetSocketAddress remote = params.getClientAddress();
                    SSLContext c = getSSLContext();
                    SSLParameters sslparams = c.getDefaultSSLParameters();
                    params.setSSLParameters(sslparams);
                }
            });
            //create context that defines path for the resource, in this case a "help"
            HttpContext httpContext = server.createContext("/warning", new Server());
            httpContext.setAuthenticator(userAuthenticator);
            server.createContext("/registration", new RegistrationHandler(userAuthenticator));
            // creates a default executor
            int numThreads = 10;
            Executor executor = Executors.newFixedThreadPool(numThreads);
            server.setExecutor(executor); 
            server.start(); 
            }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

