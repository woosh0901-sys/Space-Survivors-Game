package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class GameOverState extends GameState {

    public GameOverState(GameStateManager gsm) {
        super(gsm);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOver.fxml"));
            this.rootNode = loader.load();
            GameOverController controller = loader.getController();
            controller.init(gsm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void update(double deltaTime) {}
    @Override public void render() {}
}