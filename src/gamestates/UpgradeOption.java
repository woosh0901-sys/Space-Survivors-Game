package gamestates;

// 업그레이드 항목을 정의하는 간단한 클래스
class UpgradeOption {
    String description;
    Runnable effect; // 업그레이드 효과를 실행할 코드

    UpgradeOption(String description, Runnable effect) {
        this.description = description;
        this.effect = effect;
    }
}