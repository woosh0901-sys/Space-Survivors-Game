package gamestates; // 이 파일이 com.mygame.gamestates 패키지에 속해 있음을 선언합니다.

import java.util.Stack; // 상태를 스택처럼 관리하기 위해 Stack 클래스를 가져옵니다.

import main.GameMain;

public class GameStateManager { // 게임의 여러 상태(메뉴, 플레이, 게임오버 등)를 관리하는 클래스입니다.

    private Stack<GameState> states; // 게임 상태들을 저장할 스택입니다. 가장 위에 있는 상태가 현재 활성화된 상태입니다.
    private GameMain game; // 메인 게임 객체에 대한 참조 변수입니다.

    public GameStateManager(GameMain game) { // GameStateManager 객체를 생성하는 생성자입니다.
        this.game = game; // 메인 게임 객체 참조를 초기화합니다.
        states = new Stack<>(); // 상태를 저장할 새로운 스택을 생성합니다.
    }
    
    // 현재 상태 위에 새로운 상태를 추가하는 메소드입니다. (예: 게임 중 일시정지 메뉴)
    public void pushState(GameState state) {
        states.push(state); // 스택의 맨 위에 새로운 상태를 추가합니다.
        game.setRoot(state.getRootNode()); // 메인 게임 창의 화면을 새로운 상태의 화면으로 설정합니다.
    }
    
    // 현재 상태를 스택에서 제거하고 이전 상태로 돌아가는 메소드입니다. (예: 일시정지 해제)
    public void popState() {
        if (!states.isEmpty()) { // 스택이 비어있지 않은지 확인합니다.
            states.pop(); // 스택의 맨 위에 있는 현재 상태를 제거합니다.
            if (!states.isEmpty()) { // 상태를 제거한 후에도 스택이 비어있지 않다면,
                game.setRoot(states.peek().getRootNode()); // 이제 맨 위에 있는 (이전) 상태의 화면을 메인 게임 창에 설정합니다.
            }
        }
    }
    
    public GameState getCurrentState() { // 현재 활성화된 게임 상태를 반환하는 메소드입니다.
        if (!states.isEmpty()) { // 스택이 비어있지 않은지 확인합니다.
            return states.peek(); // 스택의 맨 위에 있는 상태를 제거하지 않고 반환합니다.
        }
        return null; // 스택이 비어있으면 null을 반환합니다.
    }
    
    public void setState(GameState state) { // 현재 상태를 완전히 새로운 상태로 교체하는 메소드입니다.
        if (!states.isEmpty()) { // 스택이 비어있지 않다면,
            states.pop(); // 현재 상태를 제거합니다.
        }
        states.push(state); // 새로운 상태를 스택에 추가합니다.
        game.setRoot(state.getRootNode()); // 메인 게임 창의 화면을 새로운 상태의 화면으로 설정합니다.
    }

    public void update(double deltaTime) { // 현재 활성화된 상태의 로직을 업데이트하는 메소드입니다.
        if (!states.isEmpty()) { // 스택에 활성화된 상태가 있는지 확인합니다.
            states.peek().update(deltaTime); // 스택의 맨 위에 있는 현재 상태의 update 메소드를 호출합니다.
        }
    }

    public void render() { // 현재 활성화된 상태의 화면을 그리는 메소드입니다.
        if (!states.isEmpty()) { // 스택에 활성화된 상태가 있는지 확인합니다.
            states.peek().render(); // 스택의 맨 위에 있는 현재 상태의 render 메소드를 호출합니다.
        }
    }
}