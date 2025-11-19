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
    void handleSelectTank(ActionEvent event) { // 이름 변경: handleSelectTank1 -> handleSelectTank
        selectCharacterAndStart(CharacterType.TANK);
    }
    
    @FXML
    void handleSelectAB(ActionEvent event) { // 이름 변경: handleSelectTank2 -> handleSelectAB
        selectCharacterAndStart(CharacterType.AB);
    }
    
    @FXML
    void handleSelectCD(ActionEvent event) { // 이름 변경: handleSelectTan3 -> handleSelectCD
        selectCharacterAndStart(CharacterType.CD);
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