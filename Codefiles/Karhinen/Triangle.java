package Karhinen;

import java.text.DecimalFormat;
import java.lang.Math; 

public class Triangle implements ShapeSpecial, CanBeColored {


    private final double a;
    private final double b;
    private final double c;
    private String color = "No Color";

    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public Triangle(double a, double b, double c){
        this.a = a;
        this.b = b; 
        this.c = c;
    }
    public Triangle(){
        this.a = 0;
        this.b = 0;
        this.c = 0;
    }
    public Triangle(String color){
        setColor(color);
        this.a = 0;
        this.b = 0;
        this.c = 0;
    }

    public Triangle(double a, double b, double c, String color){
        setColor(color);
        this.a = a;
        this.b = b; 
        this.c = c;
    }
    @Override
    public double calculateArea(){

        double p = (a + b + c)/2;
        double pa = (p * (p - a) * (p - b) * (p - c));
        pa = Math.sqrt(pa);
        return pa;
    }
    @Override
    public double calculatePerimeter(){
        double perimeter = a + b + c;
        return perimeter;
    }
    @Override
    public void drawShape(){
        
        System.out.println("Drawing a triangle with area: "+ df2.format(calculateArea())+ " and perimeter: "+ df2.format(calculatePerimeter())+ ". Color: "+ getColor());
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
