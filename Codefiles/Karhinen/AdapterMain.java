package Karhinen;


public class AdapterMain{

    public static void main(String [] args){

        Drawing piirros = new Drawing();

        Rectangle rectangle1 = new Rectangle();
        piirros.addBasicShape(rectangle1);

        Rectangle rectangle2 = new Rectangle();
        rectangle2.changeVisibility();
        piirros.addBasicShape(rectangle2);

        Circle ympyra1 = new Circle();
        ympyra1.changeVisibility();
        piirros.addBasicShape(ympyra1);

        Triangle kolmio1 = new Triangle(1.0, 1.0, 1.0);
        kolmio1.setColor("Red");
        ShapeAdapter kolmio1adapt = new ShapeAdapter(kolmio1);

        piirros.addBasicShape(kolmio1adapt);

        ShapeDiamond timantti1 = new ShapeDiamond(1.0, 1.0);
        timantti1.setColor("Blue");
        ShapeAdapter timantti1adapt = new ShapeAdapter(timantti1);
        piirros.addBasicShape(timantti1adapt);

        ShapeDiamond timantti2 = new ShapeDiamond(1.0, 1.0);
        timantti2.setColor("Red");
        ShapeAdapter timantti2adapt = new ShapeAdapter(timantti2);
        timantti2adapt.changeVisibility();
        piirros.addBasicShape(timantti2adapt);

        Triangle kolmio2 = new Triangle(1.0, 1.0, 1.0);
        kolmio2.setColor("Blue");
        ShapeAdapter kolmio2adapt = new ShapeAdapter(kolmio2);
        kolmio2adapt.changeVisibility();
        piirros.addBasicShape(kolmio2adapt);

        BrushStroke BS1 = new BrushStroke();
        BS1.changeVisibility();
        piirros.addBasicShape(BS1);

        BrushStroke BS2 = new BrushStroke();
        piirros.addBasicShape(BS2);

        
        piirros.draw();
        System.out.print("\n");
        piirros.grow();
        piirros.showVisibility();
    }
}

