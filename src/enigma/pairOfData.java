package enigma;

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
    public Character getLeft() {
        return left;
    }

    @Override
    public String toString() {
        return "pairOfData{" +
                "right=" + right +
                ", left=" + left + "\n" +
                '}';
    }
}
