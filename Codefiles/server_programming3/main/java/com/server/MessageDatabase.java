package com.server;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.codec.digest.Crypt;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageDatabase {
    
    private String hashedPassword = null;
    SecureRandom random = new SecureRandom();
    Connection dbConnection = null;
    private static MessageDatabase dbInstance = null;
    

    public static synchronized MessageDatabase getInstance() {
		if (null == dbInstance) {
			dbInstance = new MessageDatabase();
		}
        return dbInstance;
    }
    
    public MessageDatabase(){
    
       try{
            open();
       }
       catch(SQLException e){
        
       }
    }


    public void open() throws SQLException{

        String dbName = "db";
        String database = "jdbc:sqlite:" + dbName;
        dbConnection = DriverManager.getConnection(database);

        if(null != dbConnection){
            initializeDatabase();
        }
    }


    private boolean initializeDatabase() throws SQLException{

        if(null != dbConnection){
            String createUserTable = "create table users (username varchar(50) NOT NULL, password varchar(50) NOT NULL, email varchar(50), primary key(username))";
            Statement createStatement = dbConnection.createStatement();
            createStatement.executeUpdate(createUserTable);
            createStatement.close();

            String createMessageTable = "create table messages (nickname varchar(50) NOT NULL, latitude DOUBLE, longitude DOUBLE, sent integer, dangertype varchar(50), areacode varchar(50), phonenumber varchar(50), weather integer)";
            Statement createStatement2 = dbConnection.createStatement();
            createStatement2.executeUpdate(createMessageTable);
            createStatement2.close();
        }
         return false;
    }


    public boolean authenticateUser(String givenUserName, String givenPassword) throws SQLException {

        Statement queryStatement = null;
        ResultSet rs;

        String getMessagesString = "select username, password from users where username = '" + givenUserName + "'";
        System.out.println(givenUserName);


        queryStatement = dbConnection.createStatement();
		rs = queryStatement.executeQuery(getMessagesString);

        if(rs.next() == false){

            System.out.println("cannot find such user");
            return false;

        }else{

            String pass = rs.getString("password");

            if(pass.equals(Crypt.crypt(givenPassword, pass))){

                return true;

            }else{

                return false;
            }
        }
    }

    public void closeDB() throws SQLException {
		if (null != dbConnection) {
			dbConnection.close();
            System.out.println("closing db connection");
			dbConnection = null;
		}
    }

    public boolean setUser(JSONObject user) throws SQLException {

        if(checkIfUserExists(user.getString("username"))){
            return false;
        }
		byte bytes[] = new byte[13];
        random.nextBytes(bytes); 
        String saltBytes = new String(Base64.getEncoder().encode(bytes));
        String salt = "$6$" + saltBytes; 
        hashedPassword = Crypt.crypt(user.getString("password"), salt);
		String setUserString = "insert into users " +
					"VALUES('" + user.getString("username") + "','" + hashedPassword + "','" + user.getString("email") + "')"; 
		Statement createStatement;
		createStatement = dbConnection.createStatement();
		createStatement.executeUpdate(setUserString);
		createStatement.close();

        return true;
    }


    public boolean addMessage(WarningMessage wmess) throws SQLException {

		String setwmessString = "insert into messages " +
					"VALUES('" + wmess.getNickname() + "','" + wmess.getLatitude() + "','" + wmess.getLongitude() +  "','" + wmess.dateAsInt() + "','" + wmess.getDangertype() +  "','" + wmess.getAreacode() + "','" + wmess.getPhonenumber() +"','" + wmess.getWeather() +"')";            
		Statement createStatement;
		createStatement = dbConnection.createStatement();
		createStatement.executeUpdate(setwmessString);
		createStatement.close();

        return true;
    }

    public boolean checkIfUserExists(String givenUserName) throws SQLException{

        Statement queryStatement = null;
        ResultSet rs;

        String checkUser = "select username from users where username = '" + givenUserName + "'";
        System.out.println("checking user");

        
        queryStatement = dbConnection.createStatement();
		rs = queryStatement.executeQuery(checkUser);
        
        if(rs.next()){
            System.out.println("user exists");
            return true;
        }else{
            return false;
        }
    }
    public JSONArray getMessage(JSONObject query) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        ResultSet resultSet = null;
        try {
            Statement statement = dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM messages");

            if (query != null && query.has("query")) {
                if (query.getString("query").equals("user")) {
                    String user = query.getString("nickname");
                    resultSet = statement.executeQuery("SELECT * FROM messages WHERE nickname = '" + user + "'");
                }
                else if (query.getString("query").equals("time")) {
                    String aika = query.getString("timestart");
                    String aika2 = query.getString("timeend");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"); 
                    ZonedDateTime now = ZonedDateTime.parse(aika, formatter);
                    ZonedDateTime now2 = ZonedDateTime.parse(aika2, formatter);
                    String dateText = now.format(formatter);
                    String dateText2 = now2.format(formatter);
                    OffsetDateTime offsetdt = OffsetDateTime.parse(dateText);
                    OffsetDateTime offsetdt2 = OffsetDateTime.parse(dateText2);
                    LocalDateTime ldt = offsetdt.toLocalDateTime();
                    LocalDateTime ldt2 = offsetdt2.toLocalDateTime();

                    Long start = dateAsInt(ldt);
                    Long end = dateAsInt(ldt2);
                    resultSet = statement.executeQuery("SELECT * FROM messages WHERE sent BETWEEN " + start + " AND " + end);
                }
                else if(query.getString("query").equals("location")){
                    Double uplongitude = query.getDouble("uplongitude");
                    Double uplatitude = query.getDouble("uplatitude");
                    Double downlongitude = query.getDouble("downlongitude");
                    Double downlatitude = query.getDouble("downlatitude");

                    resultSet = statement.executeQuery("SELECT * FROM messages WHERE latitude BETWEEN " + downlatitude + " AND " + uplatitude + " AND longitude BETWEEN " + uplongitude + " AND " + downlongitude);
                }
                else {
                    resultSet = statement.executeQuery("SELECT * FROM messages");
                }
            } else {
                resultSet = statement.executeQuery("SELECT * FROM messages");
    
            
        }
        }catch (SQLException e) {
            
        }
        while (resultSet.next()) {
            JSONObject message = new JSONObject();
            message.put("nickname", resultSet.getString("nickname"));
            message.put("latitude", resultSet.getDouble("latitude"));
            message.put("longitude", resultSet.getDouble("longitude"));
            message.put("sent", setSent(resultSet.getLong("sent")) + "Z");
            message.put("dangertype", resultSet.getString("dangertype"));
            if(!resultSet.getString("areacode").equals("null")){
                message.put("areacode", resultSet.getString("areacode"));
                message.put("phonenumber", resultSet.getString("phonenumber"));
            } 
            if(!resultSet.getString("weather").equals("null")){
                WeatherServer wserver = new WeatherServer(resultSet.getDouble("latitude"), resultSet.getDouble("longitude"));
                int weather = Integer.valueOf(wserver.getWeather());
                message.put("weather", weather);
            }
            jsonArray.put(message);
        }
        return jsonArray;
    }  


    LocalDateTime setSent (long epoch){
        LocalDateTime sent = null;
        sent = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
        return sent;
    } 
    long dateAsInt(LocalDateTime sent){
        return sent.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
     
}

