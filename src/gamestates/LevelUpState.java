package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

// 업그레이드 항목을 정의하는 간단한 클래스
class UpgradeOption {
    String description;
    Runnable effect; // 업그레이드 효과를 실행할 코드

    UpgradeOption(String description, Runnable effect) {
        this.description = description;
        this.effect = effect;
    }
}

public class LevelUpState extends GameState {
	
    public LevelUpState(GameStateManager gsm, PlayingState playingState) {
        super(gsm);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelUp.fxml"));
            this.rootNode = loader.load();
            LevelUpController controller = loader.getController();
            controller.init(gsm, playingState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void update(double deltaTime) {}
    @Override public void render() {}
}