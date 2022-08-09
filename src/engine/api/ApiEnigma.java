package engine.api;


import DTOS.MachineSpecification;

public interface ApiEnigma {
    //here we will put all the implement for the machine
    public void readData(String str);
    public MachineSpecification showData();
    public void selectInitialCodeConfiguration(String configuration);
    public String dataEncryption(String data);

    public void systemReset();

}
