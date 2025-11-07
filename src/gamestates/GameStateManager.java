package gamestates; 

import java.util.Stack; 
import main.GameMain;

public class GameStateManager { 

    private Stack<GameState> states; 
    private GameMain game; 

    public GameStateManager(GameMain game) { 
        this.game = game; 
        states = new Stack<>(); 
    }
    
    public void pushState(GameState state) {
        states.push(state); 
        game.setRoot(state.getRootNode()); 
    }
    
    public void popState() {
        if (!states.isEmpty()) { 
            states.pop(); 
            if (!states.isEmpty()) { 
                game.setRoot(states.peek().getRootNode()); 
            }
        }
    }
    
    public GameState getCurrentState() { 
        if (!states.isEmpty()) { 
            return states.peek(); 
        }
        return null; 
    }
    
    public void setState(GameState state) { 
        if (!states.isEmpty()) { 
            states.pop(); 
        }
        states.push(state); 
        game.setRoot(state.getRootNode()); 
    }

    public void update(double deltaTime) { 
        if (!states.isEmpty()) { 
            states.peek().update(deltaTime); 
        }
    }

    public void render() { 
        if (!states.isEmpty()) { 
            states.peek().render(); 
        }
    }
}