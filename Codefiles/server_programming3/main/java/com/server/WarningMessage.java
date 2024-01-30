package com.server;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;



public class WarningMessage {
        
    public LocalDateTime sent;
    public String nick;
    public Double latitude;
    public Double longitude;
    public String dangertype;
    private String areacode = null;
    private String phonenumber = null;
    private String weather;
    ArrayList<WarningMessage> messages = new ArrayList<>();


    public WarningMessage() {
    
    }
   
    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }

    public String getNickname() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getDangertype() {
        return dangertype;
    }
    public void setDangertype(String dangertype) {
        this.dangertype = dangertype;
    }

    

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }


    

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    long dateAsInt(){
        return sent.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    void setSent (long epoch){
        sent = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    } 
}