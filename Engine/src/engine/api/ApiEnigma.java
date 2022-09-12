package engine.api;

import DTOS.ConfigrationsPropertyAdapter.FileConfigurationDTOAdapter;
import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import DTOS.Configuration.FileConfigurationDTO;
import DTOS.Configuration.UserConfigurationDTO;
import DTOS.StatisticsDTO.MachineStatisticsDTO;
import DTOS.Validators.xmlFileValidatorDTO;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.List;

public interface ApiEnigma {
    //here we will put all the implement for the machine
    public xmlFileValidatorDTO readData(String filePath);

    public void selectInitialCodeConfiguration(UserConfigurationDTO configuration);

    public String dataEncryption(String data);

    public void resetPositions();

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
    public void setUserConfigurationDTO(UserConfigurationDTO originalConfigurationDTO);
    public void UpdateCode(UserConfigurationDTO originalConfigurationDTO);
    public StringBuilder PrintStatistic();
    public Character encryptChar(char ch);
    public void updateStatistics(String input, String output, long processingTime);
    public void setStatisticsProperty(StringProperty statisticsProperty);
    public void DecipherMessage(String messageToDecipher,int difficulty, int missionSize);
}
