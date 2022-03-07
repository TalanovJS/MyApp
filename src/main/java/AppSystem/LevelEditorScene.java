package AppSystem;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene(){

    }

    @Override
    public void update(float dt) {
        if(KeyListener.isKeyPressed(KeyEvent.VK_SPACE) && !changingScene){
            changingScene = true;
        }

        if(changingScene && timeToChangeScene > 0){
            timeToChangeScene -= dt;
        } else if (changingScene){
            Window.changeScene(1);
        }
    }


}
