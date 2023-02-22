package Karhinen;

public class ShapeAdapter extends Visibility implements ShapeBasic {

    ShapeSpecial kuvio;

    public ShapeAdapter(ShapeSpecial kuvio){
        this.kuvio = kuvio;
    }

    @Override
    public void draw() {
        kuvio.drawShape();
    }

    @Override
    public void grow() {
        System.out.printf("%s, cannot grow\n",  printText());
    }

    @Override
    public String printText() {
       if(kuvio instanceof Triangle){
            return ("Triangle");
       }
       else if(kuvio instanceof ShapeDiamond){
            return ("ShapeDiamond");
       }
       else{
            return ("Wrong kind of shape - not supported");
       }
    }
    
}
