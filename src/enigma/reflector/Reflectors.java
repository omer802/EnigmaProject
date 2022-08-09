package enigma.reflector;

import java.util.List;

public class Reflectors {
    public enum ReflectorEnum {
        I, II, III, IV,V }
    private int chosenReflector;
    private final static int didntchooseReflector= -1;
    private List<Reflector> reflectors;
    private int reflectorsAmount;

    public Reflectors(List<Reflector> reflectors ){
        chosenReflector = didntchooseReflector;
        this.reflectors = reflectors;
        this.reflectorsAmount = reflectors.size();
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
    public ReflectorEnum getChosenReflectorByRomeNumerals() {
        ReflectorEnum ReflectorToReturn = null;
        switch (this.chosenReflector) {
            case 0:
                ReflectorToReturn =  ReflectorEnum.I;
                break;
            case 1:
                ReflectorToReturn = ReflectorEnum.II;
                break;
            case 2:
                ReflectorToReturn = ReflectorEnum.III;
                break;
            case 3:
                ReflectorToReturn = ReflectorEnum.IV;
                break;
            case 4:
                ReflectorToReturn = ReflectorEnum.V;
                break;
        }
        return ReflectorToReturn;
    }
}
