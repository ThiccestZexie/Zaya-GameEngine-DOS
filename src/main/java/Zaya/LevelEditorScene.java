package Zaya;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;
import Zaya.Camera;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private Shader defaultShader;

    private float[] vertexArray = {
            //Pos,                  //Color               //UV COORD
            100.0f, -0.0f, 0.0f,    1.0f,0.0f,0.0f,1.0f,   1,1, // Bot right 0
            0.0f, 100.0f, 0.0f,     0.0f,1.0f,0.0f,1.0f,   0,0, // Top left 1
            100.0f, 100.0f, 0.0f,   0.0f,0.0f,1.0f,1.0f,   1,0, // Top right 2
            0.0f, 0.0f, 0.0f,       1.0f,1.0f,0.0f,1.0f,   0,1 // Bottom left 3
    };

    // IMPORTANT: MUST BE IN COUNTERCLOCKWISE ORDER

    /*
            x     x

            x     x
     */
    private int[] elementArray = {
            2,1,0, //top right triangle
            0,1,3 //bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Texture testTexture;

    GameObject testObj;

    private boolean firstTime = false;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        System.out.println("Creating tes tobject");
        this.testObj = new GameObject("test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);

        // Compile and link shaders
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile_and_link();
        this.testTexture = new Texture("assets/textures/testImage.png");


        //Generate VAO, VBO, EBO BUFFER OBJETCTS AND SEND TO GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices

        FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexbuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID); // make everything for this buffer object to be related to GL_ARRAY_BUFFER
        glBufferData(GL_ARRAY_BUFFER, vertexbuffer, GL_STATIC_DRAW); // We are working with array buffer and send this buffer and be static

        //Create the indicies
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        //Create EBO and upload the indices buffer
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW); // same ass above but element buffer

        // Add the vertex attribute pointers.
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0); // in our default.glsl we point to position 0

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,(positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        // camera.position.x -= dt*50.0f;
        // camera.position.y -= dt* 20.0f;

        defaultShader.use();

        //Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        //Bind the VAO we are using
        glBindVertexArray(vaoID);
        //enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
        if(!firstTime){
            System.out.println("Creating test object 2");
            GameObject go  = new GameObject("Test Object 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}