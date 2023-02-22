package Karhinen;

import java.util.ArrayList;
import java.util.List;

public class Drawing{

    List<ShapeBasic> listakuvioita;

    public Drawing(){
        listakuvioita = new ArrayList<ShapeBasic>();
    }

    public void addBasicShape(ShapeBasic kuvio){
        listakuvioita.add(kuvio);
    }

    public List<ShapeBasic> getBasicShapes(){
        
        return listakuvioita;
        
    }
    
    public void draw(){

        if(listakuvioita.isEmpty()){
            System.out.println("Kuvioita ei ole lisätty");
        }
        else{
            for(int i = 0; i < listakuvioita.size(); i++){
                listakuvioita.get(i).draw();
            }
        }
    }

    public void grow(){

        if(listakuvioita.isEmpty()){
            System.out.println("Kuvioita ei ole lisätty");
        }
        else{
            for(int i = 0; i < listakuvioita.size(); i++){
                listakuvioita.get(i).grow();
            }
        }
    }

    public void showVisibility(){
        if(listakuvioita.isEmpty()){
            System.out.println("Kuvioita ei ole lisätty");
        }
        else{
            for(int i = 0; i < listakuvioita.size(); i++){
                if(listakuvioita.get(i).isVisible() == false){
                    System.out.printf("\n%d. shape is visible: %s ", i + 1, listakuvioita.get(i).printText());
                }
                else{
                    System.out.printf("\n%d. shape is in the background: %s ", i + 1, listakuvioita.get(i).printText());
                }
            }
        }

    }

   
}