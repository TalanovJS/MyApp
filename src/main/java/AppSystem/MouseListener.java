package AppSystem;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private double scrollX, scrollY;
    private double posX, posY, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDrag;

    //Singleton
    private static MouseListener instance;

    public MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    // Mouse position
    public static void mousePosCallback(long window, double xpos, double ypos){
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = xpos;
        get().posY = ypos;
        get().isDrag = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    //Buttons callbacks
    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (action == GLFW_PRESS){
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE){
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().isDrag = false;
            }
        }
    }

    public static void mouseScrollOffset(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    //Getters
    public static float getX(){
        return (float) get().posX;
    }

    public static float getY(){
        return (float) get().posY;
    }

    public static float getLastX(){
        return (float) (get().lastX - get().posX);
    }

    public static float getLastY(){
        return (float) (get().lastY - get().posY);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isDrag(){
        return get().isDrag;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
