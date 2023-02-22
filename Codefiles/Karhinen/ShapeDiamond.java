package Karhinen;

import java.text.DecimalFormat;

public class ShapeDiamond implements ShapeSpecial, CanBeColored {

    private final double a;
    private final double b;
    private String color = "No Color";
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public ShapeDiamond(double a, double b){
        this.a = a;
        this.b = b;
    }
    public ShapeDiamond(){
        this.a = 0;
        this.b = 0;
    }

    public ShapeDiamond(String color){
        setColor(color);
        this.a = 0;
        this.b = 0;
    }

    public ShapeDiamond(String color, double a, double b){
        setColor(color);
        this.a = a;
        this.b = b;
    }

    @Override
    public double calculateArea() {
        double pa = a * b;
        return pa;
    }

    @Override
    public double calculatePerimeter() {
        double perimeter = a + a + b + b;
        return perimeter;
    }

    @Override
    public void drawShape() {
       
        System.out.println("Drawing a diamond shape with area: " + df2.format(calculateArea()) +" and perimeter: "+ df2.format(calculatePerimeter()) +". Color: " + getColor());
    }
    @Override
    public boolean setColor(String color) {
        if(color.equalsIgnoreCase("red") || color.equalsIgnoreCase("blue")){
            this.color = color; 
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public String getColor() {
        return this.color;
    }
    
}
