package gamestates;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class ShopState extends GameState {

    public ShopState(GameStateManager gsm) {
        super(gsm);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Shop.fxml"));
            this.rootNode = loader.load();
            ShopController controller = loader.getController();
            controller.init(gsm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void update(double deltaTime) {}
    @Override public void render() {}
}