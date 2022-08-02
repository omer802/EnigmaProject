package Engine.LoadData;

import EngineMachine.Machine;

public interface LoadData {
    // change to object and not a string path. in the implimntation we move this object to string
    public Machine LoadDataFromInput(String path);
}
