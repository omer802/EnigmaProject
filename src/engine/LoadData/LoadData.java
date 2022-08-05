package engine.LoadData;

import engima.Machine.EnigmaMachine;

public interface LoadData {
    // TODO: 8/2/2022 change to object and not a string path. in the implimntation we move this object to string
    public EnigmaMachine loadDataFromInput(String path);
}
