package AppSystem;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private int shaderProgram;

    private final float[] vertexArray ={
        //positon             // color
        0.5f, -0.5f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f, // Bottom right
        -0.5f, 0.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f, // Top left
         0.5f, 0.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f, // Top Right
        -0.5f, -0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f // Bottom left
    };

    // Сounter-clockwise order
    private final int[] elementArray = {
            2, 1, 0, //Top right tri
            0, 1, 3  // Bottom left tri

    };

    private int vertexArrayObj;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        //Compile and lint shaders
        //Load and compile the vertex shaders
        int vertexID = glCreateShader(GL_VERTEX_SHADER);

        //Pass the shader source to the GPU
        String vertexShaderSrc =
                "    #version 330 core\n" +
                "    layout (location=0) in vec3 aPos;\n" +
                "    layout (location=1) in vec4 aColor;\n" +
                "\n" +
                "    //Fragments\n" +
                "    out vec4 fColor;\n" +
                "\n" +
                "    void main(){\n" +
                "        fColor = aColor;\n" +
                "        gl_Position = vec4(aPos, 1.0);\n" +
                "    }";

        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        //Check for errors in comilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shaders compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false: "";
        }

        //Load and compile the fragment shaders
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //Pass the shader source to the GPU
        String fragmentShaderSrc =
                "    #version 330 core\n" +
                "\n" +
                "    in vec4 fColor;\n" +
                "\n" +
                "    out vec4 color;\n" +
                "\n" +
                "    void main(){\n" +
                "        color = fColor;\n" +
                "    }";

        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        //Check for errors in comilation
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shaders compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false: "";
        }

        //Link shaders and check errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //Check for lonk err
        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success == GL_FALSE){
             int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false: "";

        }
        // Generate VAO, VBO and BBO buffer objects and sent to GPU
        vertexArrayObj = glGenVertexArrays();
        glBindVertexArray(vertexArrayObj);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO and upload the vertex buffer
        int vertexBufferObj = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObj);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        int elementBufferObj = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObj);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attributes
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize,GL_FLOAT,false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize,GL_FLOAT, false, vertexSizeBytes,positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //Bind shader program
        glUseProgram(shaderProgram);

        // Bine VAO
        glBindVertexArray(vertexArrayObj);

        //Enable vertex attrib
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length, GL_UNSIGNED_INT,0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);
    }


}
