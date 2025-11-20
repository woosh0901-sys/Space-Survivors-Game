package gamestates;

import main.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML; // ★ FXML import 필수

public class MenuController {

    private GameStateManager gsm;

    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    // ★ 1. START GAME 버튼 (원래 이름: handlePlayButton)
    @FXML
    void handlePlayButton(ActionEvent event) {
        if (gsm != null) {
            // PlayingState로 바로 가지 않고 캐릭터 선택창으로 이동
            gsm.setState(new CharacterSelectState(gsm));
        }
    }
    
    // ★ 2. SHOP 버튼 (원래 이름: handleShopButton)
    @FXML
    void handleShopButton(ActionEvent event) {
        if (gsm != null) {
            gsm.pushState(new ShopState(gsm));
        }
    }
    
    // ★ 3. RESET DATA 버튼 (원래 이름: handleResetButton)
    @FXML
    void handleResetButton(ActionEvent event) {
        GameData.reset();
        GameData.save();
        System.out.println("데이터가 초기화되었습니다.");
    }
}