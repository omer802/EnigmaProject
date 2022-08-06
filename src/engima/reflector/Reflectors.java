package engima.reflector;

import engima.rotors.RotatingRotor;

import java.util.List;

public class Reflectors {
    public enum ReflectorEnum {
        I, II, III, IV,V }
    private int chosenReflector;
    private final static int didntchooseReflector= -1;
    private List<Reflector> reflectors;
    private int reflectorsAmount;
    private int reflectorsAmountInUse;

    public Reflectors(List<Reflector> reflectors ){
        chosenReflector = didntchooseReflector;
        this.reflectors = reflectors;
    }
    public Reflector getReflectorInUse(){
        return reflectors.get(chosenReflector);
    }
    public int ToReflect(int index){
        return getReflectorInUse().reflect(index);
    }
    public List<Reflector> getReflectors() {
        return reflectors;
    }

    public void setReflectors(List<Reflector> reflectors) {
        this.reflectors = reflectors;
    }

    public int getReflectorsAmount() {
        return reflectorsAmount;
    }

    public void setReflectorsAmount(int reflectorsAmount) {
        this.reflectorsAmount = reflectorsAmount;
    }

    public void setReflectorsAmountInUse(int reflectorsAmountInUse) {
        this.reflectorsAmountInUse = reflectorsAmountInUse;
    }

    public int getReflectorsAmountInUse() {
        return reflectorsAmountInUse;
    }
    public void SetChosenReflector(ReflectorEnum chosenReflector) {
        switch (chosenReflector) {
            case I:
                this.chosenReflector = 0;
                break;
            case II:
                this.chosenReflector = 1;
                break;
            case III:
                this.chosenReflector = 2;
                break;
            case IV:
                this.chosenReflector = 3;
                break;
            case V:
                this.chosenReflector = 4;
                break;
        }
    }
}
