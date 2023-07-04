package Zaya;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3]; // 0 = left, 1 = middle, 2 = right
    private boolean isDragging;


    private MouseListener(){
        //Initillized the variables
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastY = 0;
        this.lastX = 0;

    }
    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }
    //Exact definitions for the callback is needed
    public static void mousePosCallback(long window, double xpos, double ypos)
    {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;

        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }

        }

    }
    public static void mouseScrollCallBack(long window, double xoffset, double yoffset){
        get().scrollX = xoffset;
        get().scrollY = yoffset;

    }
    public static void endFrame()    {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;

    }

    // --------------------------
    // Getters for the variables
    // --------------------------
    public static float getX(){
        return (float)get().xPos;
    }
    public static float getY(){
        return (float)get().yPos;
    }
    public static float getDx(){
        return (float) (get().lastX - get().xPos);
    }
    public static float getDy(){
        return (float) (get().lastY - get().yPos);
    }
    public static float getScrollX(){
        return (float)get().scrollX;
    }
    public static float getScrollY(){
        return (float)get().scrollY;
    }
    public static boolean isDragging(){
        return get().isDragging;
    }
    public static boolean isButtonPressed(int button){

        if(button < get().mouseButtonPressed.length)
        {
            return get().mouseButtonPressed[button];
        }
        return false;
    }
}