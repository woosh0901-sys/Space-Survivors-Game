package gamestates;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.CharacterType;
import main.GameData;

public class CharacterSelectController {

    private GameStateManager gsm;

    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @FXML
    void handleSelectDefault(ActionEvent event) {
        selectCharacterAndStart(CharacterType.DEFAULT);
    }

    @FXML
    void handleSelectTank(ActionEvent event) {
        selectCharacterAndStart(CharacterType.TANK);
    }
    
    private void selectCharacterAndStart(CharacterType type) {
        GameData.selectedCharacter = type.getId();
        GameData.save();
        gsm.setState(new PlayingState(gsm));
    }

    @FXML
    void handleBackButton(ActionEvent event) {
        gsm.setState(new MenuState(gsm));
    }
}
