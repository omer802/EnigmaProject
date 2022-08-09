package DTOS;

public class PairOfNotchAndRotorId {
    private final String rotorId;
    private final int rotorNotch;

    public PairOfNotchAndRotorId(String rotorId, int rotorNotch){
        this.rotorId = rotorId;
        this.rotorNotch = rotorNotch;
    }

    public int getRotorNotch() {
        return rotorNotch;
    }
    public String getRotorId() {
        return rotorId;
    }

    @Override
    public String toString() {
        return
                "[rotorId='" + rotorId + '\'' +
                ", rotorNotch=" + rotorNotch +
                ']';
    }
}
