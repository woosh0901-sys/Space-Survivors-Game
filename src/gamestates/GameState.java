package gamestates;

import javafx.scene.layout.Pane; 

public abstract class GameState { 

    protected GameStateManager gsm; 
    protected Pane rootNode; 

    public GameState(GameStateManager gsm) { 
        this.gsm = gsm; 
        this.rootNode = new Pane(); 
    }

    public Pane getRootNode() { 
        return rootNode; 
    }

    public abstract void update(double deltaTime);
    
    public abstract void render();
}