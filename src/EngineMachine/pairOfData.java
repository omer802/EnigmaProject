package EngineMachine;

public class pairOfData {

    private final Character input;

    private final Character output;
    public pairOfData(Character right, Character left) {
        this.input = right;
        this.output = left;
    }

    public Character getInput() {
        return input;
    }
    public Character getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "pairOfData{" +
                "input=" + input +
                ", output=" + output + "\n" +
                '}';
    }
}
