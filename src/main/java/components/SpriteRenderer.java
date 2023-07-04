package components;

import Zaya.Component;

public class SpriteRenderer extends Component{
    private boolean firstTime = false;
    @Override
    public void start(){
        System.out.println("I am starting");
    }
    @Override
    public void update(float dt) {
        if(firstTime == false){
            System.out.println("I am updating");
            firstTime = true;
        }
    }
}