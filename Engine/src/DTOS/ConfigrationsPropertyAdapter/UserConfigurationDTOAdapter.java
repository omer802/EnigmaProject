package DTOS.ConfigrationsPropertyAdapter;

import javafx.beans.property.SimpleStringProperty;

public class UserConfigurationDTOAdapter {

    private SimpleStringProperty chosenRotors;
    private SimpleStringProperty NotchAndLetterAtPeekPaneStartingPosition;
    private SimpleStringProperty chosenReflector;
    private SimpleStringProperty plugBoardToShow;
    public UserConfigurationDTOAdapter(SimpleStringProperty chosenRotors, SimpleStringProperty NotchAndLetterAtPeekPaneStartingPosition,
    SimpleStringProperty chosenReflector, SimpleStringProperty plugBoardToShow){
        this.chosenRotors = chosenRotors;
        this.NotchAndLetterAtPeekPaneStartingPosition = NotchAndLetterAtPeekPaneStartingPosition;
        this.chosenReflector = chosenReflector;
        this.plugBoardToShow = plugBoardToShow;
    }

}
