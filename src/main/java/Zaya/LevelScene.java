package Zaya;

public class LevelScene extends Scene {
    public LevelScene() {
        System.out.println("Inside LevelScene  ");
        Window.get().setR(1);
        Window.get().setG(1);
        Window.get().setB(1);
    }

    @Override
    public void update(float dt) {
    }
}