package engine.api;


import DTOS.FileConfigurationDTO;
import DTOS.UserConfigurationDTO;
import DTOS.MachineStatisticsDTO;
import DTOS.Validators.xmlFileValidatorDTO;

import java.util.List;

public interface ApiEnigma {
    //here we will put all the implement for the machine
    public xmlFileValidatorDTO readData(String filePath);
    public void selectInitialCodeConfiguration(UserConfigurationDTO configuration);
    public String dataEncryption(String data);

    public void systemReset();

    public FileConfigurationDTO showDataReceivedFromFile();

    public UserConfigurationDTO showDataReceivedFromUser();
    public List<String> getPossibleReflectors();
    public List<String> getPossibleRotors();
    public boolean isConfigFromUser();
    public int getAmountOfRotors();

    public boolean isLegalRotors(String chosenRotors) ;

    public boolean isRotorsPositionsInRange(String positions);
    public boolean isReflectorValid(String chosenReflector);
    public List<String> cleanStringAndReturnList(String chosenRotorsInputStr);
    public boolean alreadyEncryptedMessages();

    public UserConfigurationDTO getCurrentConfiguration();

    public UserConfigurationDTO AutomaticallyInitialCodeConfiguration();
    public MachineStatisticsDTO getStatistics();

    public boolean haveConfigFromUser();

    public boolean haveConfigFromFile();
    }
