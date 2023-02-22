package Karhinen;

public class BrushStroke extends Visibility implements ShapeBasic {

    public BrushStroke(){

    }

    public void draw(){ 
        System.out.println("Drawing a brush stroke");

    }

    public void grow(){
        System.out.println("Adding some color...");
    }

    public String printText(){
        return "Brush Stroke";
    }
    
}
