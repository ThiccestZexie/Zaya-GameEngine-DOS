package Zaya;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    public Scene() {
        //it is going to later be the game wrapper

    }

    public void addGameObjectToScene(GameObject go  ){
        //add game object to scene
        if(!isRunning){
            gameObjects.add(go);
        } else{
            gameObjects.add(go);
            go.start();
        }

    }

    public void start(){
        for (GameObject go : gameObjects){
            go.start();
        }
        isRunning = true;
    }
    public abstract void update(float dt);

    public void init() {
    }

}