package engine.enigma;

// TODO: 8/15/2022 change to pair  
public class pairOfData {

    private final Character right;

    private final Character left;
    public pairOfData(Character right, Character left) {
        this.right = right;
        this.left = left;
    }

    public Character getRight() {
        return right;
    }
    public Character getLeft() {return left;
    }

    @Override
    public String toString() {
        return "pairOfData{" +
                "right=" + right +
                ", left=" + left + "\n" +
                '}';
    }
}
