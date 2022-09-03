package engine.api;

import DTOS.ConfigrationsPropertyAdapter.FileConfigurationDTOAdapter;
import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import DTOS.Configuration.FileConfigurationDTO;
import DTOS.Configuration.UserConfigurationDTO;
import DTOS.Statistics.MachineStatisticsDTO;
import DTOS.Validators.xmlFileValidatorDTO;

import java.io.IOException;
import java.util.List;

public interface ApiEnigma {
    //here we will put all the implement for the machine
    public xmlFileValidatorDTO readData(String filePath);

    public void selectInitialCodeConfiguration(UserConfigurationDTO configuration);

    public String dataEncryption(String data);

    public void systemReset();

    public FileConfigurationDTO showDataReceivedFromFile();

    public UserConfigurationDTO getCurrentConfiguration();

    public List<String> getPossibleReflectors();

    public List<String> getPossibleRotors();

    public boolean isConfigFromUser();

    public int getAmountOfRotors();

    public boolean isIdenticalRotors(List<String> chosenRotorsList);

    public List<String> isLegalRotors(String chosenRotors);

    public boolean isRotorsPositionsInRange(String positions);

    public List<String> isReflectorValid(String chosenReflector);

    public List<String> cleanStringAndReturnList(String chosenRotorsInputStr);

    public boolean alreadyEncryptedMessages();

    public UserConfigurationDTO getOriginalConfiguration();

    public UserConfigurationDTO AutomaticallyInitialCodeConfiguration();

    public MachineStatisticsDTO getStatistics();

    public boolean haveConfigFromUser();

    public boolean haveConfigFromFile();

    public void saveMachineStateToFile(String filePath) throws IOException;

    public void loadMachineStateFromFIle(String filePath) throws IOException, ClassNotFoundException;

    public boolean validateStringToEncrypt(String stringToEncrypt);

    public void setDTOConfigurationAdapter(FileConfigurationDTOAdapter fileConfigurationDTOAdapter);

    public xmlFileValidatorDTO readDataJavaFx(String filePath);

    public StringBuilder getStringDataReceiveFromUser(UserConfigurationDTO machineConfigUser);

    public void setCurrentConfigurationProperties(UserConfigurationDTOAdapter DTOPropertiesToConfig);
}
