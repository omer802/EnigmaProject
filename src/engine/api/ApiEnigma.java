package engine.api;


import DTOS.MachineSpecificationFromFile;
import DTOS.MachineSpecificationFromUser;
import sun.misc.ExtensionInstallationException;

import java.util.List;
import java.util.Set;

public interface ApiEnigma {
    //here we will put all the implement for the machine
    public void readData(String str);
    public void selectInitialCodeConfiguration(MachineSpecificationFromUser configuration);
    public String dataEncryption(String data);

    public void systemReset();

    public MachineSpecificationFromFile showDataReceivedFromFile();

    public MachineSpecificationFromUser showDataReceivedFromUser();
    public List<String> getPossibleReflectors();
    public List<String> getPossibleRotors();
    public boolean isConfigFromUser();
    public int getAmountOfRotors();

    public boolean isLegalRotors(String chosenRotors) ;

    public boolean isRotorsPositionsInRange(String positions);
    public boolean isReflectorValid(String chosenReflector);
    public List<String> cleanStringAndReturnList(String chosenRotorsInputStr);
    public boolean alreadyEncryptedMessages();

    public MachineSpecificationFromUser getFirstConfig();

    public MachineSpecificationFromUser AutomaticallyInitialCodeConfiguration();
    }
