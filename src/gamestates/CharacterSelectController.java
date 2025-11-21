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

    // 1. 박종화 (체력) - 기존 DEFAULT 타입 사용
    @FXML
    void handleSelectDefault(ActionEvent event) {
        selectCharacterAndStart(CharacterType.DEFAULT);
    }

    // 2. 우서현 (골드) - 새로 추가!
    @FXML
    void handleSelectWoo(ActionEvent event) {
        selectCharacterAndStart(CharacterType.WOO);
    }
    
    // 3. 이정환 (공격력) - 새로 추가!
    @FXML
    void handleSelectLee(ActionEvent event) {
        selectCharacterAndStart(CharacterType.LEE);
    }
    
    // 공통 로직
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