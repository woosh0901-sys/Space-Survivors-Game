package main;

import java.util.HashSet;
import java.util.Set;
import gamestates.GameStateManager;
import gamestates.MenuState;
import gamestates.PlayingState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameMain extends Application {

    public static final int WIDTH = GameConstants.SCREEN_WIDTH;
    public static final int HEIGHT = GameConstants.SCREEN_HEIGHT;

    private GameStateManager gsm;
    private static Set<KeyCode> activeKeys = new HashSet<>();
    private StackPane mainPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameData.load();

        primaryStage.setTitle("Space Survivors");
        mainPane = new StackPane();
        Scene scene = new Scene(mainPane, WIDTH, HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> GameData.save());

        gsm = new GameStateManager(this);
        gsm.setState(new MenuState(gsm));

        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());

            if (gsm.getCurrentState() instanceof PlayingState) {
                if (event.getCode() == KeyCode.SPACE) {
                    ((PlayingState) gsm.getCurrentState()).shoot();
                }
            }
        });

        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastTimestamp = 0;
            
            @Override
            public void handle(long now) {
                if (lastTimestamp == 0) {
                    lastTimestamp = now;
                    return;
                }
                
                double deltaTime = (now - lastTimestamp) / 1_000_000_000.0;
                lastTimestamp = now;

                gsm.update(deltaTime);
                gsm.render();
            }
        };
        gameLoop.start();
    }

    public void setRoot(Pane pane) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(pane);
    }

    public static Set<KeyCode> getActiveKeys() {
        return activeKeys;
    }

    public static void main(String[] args) {
        launch(args);
    }
}