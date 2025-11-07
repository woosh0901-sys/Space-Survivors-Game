package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

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