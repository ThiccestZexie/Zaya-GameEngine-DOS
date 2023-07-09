package Zaya;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private float r,g,b,a;
    private static Window window = null;

    private static Scene currentScene;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Zaya";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene" + newScene + "'";
                break;
        }
    }



    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }
    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        //As we are using C we need to free the memory as we have it allocated.
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Now we shall terminate it and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public void init(){
        GLFWErrorCallback.createPrint(System.err).set(); // Directs errors to System.err

        // Initialize GLFW so that we can use the library...
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints(); // Do you want it to be resizable, be default, etc...
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Do you want the window to be visible? Not until it is done
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Do you want the window to be resizable? Yes
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // Do you want the window to be maximized? Yes

        //Create a windowed mode window and its OpenGL context
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL); // On primary monitor and no share
        if(glfwWindow == NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }


        //Setting cursor callbacks!
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallBack);

        //Setting KeyListener
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make OpenGl current context
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow); // Show te Window on screen
        //Important, for GLFW to work with OpenGL context, makes bindings usable
        GL.createCapabilities();

        Window.changeScene(0);
    }
    public void loop(){
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll events gets the events and puts in into our key listeners
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT); // Tells OpenGL to clear the color buffer and how to clear it... basically flush out screen
            if(dt >= 0){ //For now, we have a lag of 2 frames when the scene starts
                currentScene.update(dt);
            }


            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

        }
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setB(float b) {
        this.b = b;
    }

    public static Scene getScene(){
        return get().currentScene;
    }
}
