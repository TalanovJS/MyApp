package AppSystem;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private final boolean[] keyPressed = new boolean[350];

    //Singleton
    private static KeyListener instance;

    public KeyListener() {}

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scan, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
