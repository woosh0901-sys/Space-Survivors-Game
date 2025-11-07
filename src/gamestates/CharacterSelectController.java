package gamestates;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.GameData;

public class CharacterSelectController {

    private GameStateManager gsm;

    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    // "기본 캐릭터" 버튼을 눌렀을 때
    @FXML
    void handleSelectDefault(ActionEvent event) {
        // 1. 캐릭터 선택을 GameData에 저장
        GameData.selectedCharacter = "DEFAULT";
        GameData.save();
        
        // 2. 게임 시작
        gsm.setState(new PlayingState(gsm));
    }

    // "탱크 캐릭터" 버튼을 눌렀을 때
    @FXML
    void handleSelectTank(ActionEvent event) {
        // 1. 캐릭터 선택을 GameData에 저장
        GameData.selectedCharacter = "TANK";
        GameData.save();
        
        // 2. 게임 시작
        gsm.setState(new PlayingState(gsm));
    }

    // "뒤로가기" 버튼을 눌렀을 때
    @FXML
    void handleBackButton(ActionEvent event) {
        // 메인 메뉴(MenuState)로 돌아감
        gsm.setState(new MenuState(gsm));
    }
}