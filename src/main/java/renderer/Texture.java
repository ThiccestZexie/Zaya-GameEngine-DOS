package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private String filePath;
    private int texID;
    public Texture(String filePath) {
        this.filePath = filePath;

        //Generate  texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //Set texture parameters
        //Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); //S is x-axis
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); //T is y-axis
        //When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //Minifying the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); //Magnifying the image, pixelate

        //Load the image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channel = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filePath, width, height, channel, 0);

        if(image != null){
            if (channel.get(0) == 3)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            else if (channel.get(0) == 4 ){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else {
                assert false : "Error: (Texture) Unknown number of channels '" + channel.get(0) + "'";
            }

        }
        else {
            assert false : "Error: (Texture) Could not load image '" + filePath + "'";
        }
        //Frees memory!
        stbi_image_free(image);

    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
    }
    public void unBind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}