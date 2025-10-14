package gamestates;

import main.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {

    private GameStateManager gsm;

    // GameStateManager를 외부(MenuState)에서 받아오기 위한 메소드
    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    // FXML 파일의 onAction="#handlePlayButton"과 연결됩니다.
    @FXML
    void handlePlayButton(ActionEvent event) {
        if (gsm != null) {
            gsm.setState(new PlayingState(gsm));
        }
    }
    
    // FXML 파일의 onAction="#handleShopButton"과 연결됩니다.
    @FXML
    void handleShopButton(ActionEvent event) {
        if (gsm != null) {
            gsm.pushState(new ShopState(gsm));
        }
    }
    
    // FXML 파일의 onAction="#handleResetButton"과 연결됩니다.
    @FXML
    void handleResetButton(ActionEvent event) {
        GameData.reset();
        GameData.save();
        System.out.println("데이터가 초기화되었습니다.");
    }
}