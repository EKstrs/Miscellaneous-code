package Karhinen;


public class Circle extends Visibility implements ShapeBasic{

    public Circle(){
    }

    @Override
    public void draw(){
        System.out.print("Drawing a circle\n");
    }
    @Override
    public void grow(){
        System.out.print("Growing the size of the circle\n");
    }
    @Override
    public String printText(){
        return "Circle";
    }
}