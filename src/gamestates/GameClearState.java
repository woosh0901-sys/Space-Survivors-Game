package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class GameClearState extends GameState {

    public GameClearState(GameStateManager gsm, double clearTime) {
        super(gsm);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameClear.fxml"));
            this.rootNode = loader.load();
            GameClearController controller = loader.getController();
            controller.init(gsm, clearTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override 
    public void update(double deltaTime) {}
    
    @Override 
    public void render() {}
}