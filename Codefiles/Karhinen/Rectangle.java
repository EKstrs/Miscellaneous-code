package Karhinen;


public class Rectangle extends Visibility implements ShapeBasic{

    public Rectangle(){

    }

    @Override
    public void draw() {
        System.out.print("Drawing a rectangle\n");
        
    }

    @Override
    public void grow() {
        System.out.print("Growing the size of the rectangle\n");
        
    }

    @Override
    public String printText() {
        return "Rectangle";
    }
}