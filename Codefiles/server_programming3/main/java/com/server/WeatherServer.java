package com.server;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



    public class WeatherServer {

        private double latitude;
        private double longitude;

        public WeatherServer(Double latitude, Double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
        

        public double getLatitude() {
            return latitude;
        }


        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }




        public double getLongitude() {
            return longitude;
        }




        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }



        
        private String getXML(){
            String xmlmsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<coordinates>\n"
                +   "<latitude>" + getLatitude() + "</latitude>\n" + " <longitude>" + getLongitude() + "</longitude>\n"
                + "</coordinates>";
            return xmlmsg;
        }
    
        private static Document xmlFileConverter(String text) throws Exception{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(new InputSource(new StringReader(text)));
    
            return document;
        } 

        public String getWeather(){

            try {
                URL url = new URL("http://localhost:4001/weather");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/xml");
    
                OutputStream outputstream = connection.getOutputStream();
                byte[] bytes = getXML().getBytes("UTF-8");
                outputstream.write(bytes, 0, bytes.length);
                outputstream.flush();
                outputstream.close();
    
                String text = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
    
                Document document = xmlFileConverter(text);
                NodeList nodelist = document.getElementsByTagName("weather");
                Element weather_element = (Element) nodelist.item(0);
                String temperature = weather_element.getElementsByTagName("temperature").item(0).getTextContent();
                
                return temperature;
            } catch (Exception e) {
                System.out.println("Failure getting weather");
                System.out.println(e.getMessage());
                return null;

            }
        }
    
}

   
