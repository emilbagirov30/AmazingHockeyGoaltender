package com.emil.ahg;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class GoalieControl implements InputProcessor {

    private final Body goalie;
    private final OrthographicCamera camera;
    private final Circle joystickPC;
    private final Circle joystickIPC;
    private final Vector3 currentlengthV = new Vector3();
    private final Vector2 currentlengthV2 = new Vector2();
    private final Vector3 currentlengthVSol = new Vector3();
    private final Vector2 currentlengthVSol2 = new Vector2();
    private  float kLength;
    private float resultLenght;
    private  float maxXY;
    public static boolean isStart = false;
    private final Vector3 lengthV = new Vector3();
    private final Vector3 lengthV2 = new Vector3();
    private final Vector3 touchPosV = new Vector3();
    private boolean touch = false;
    private final int playerSpeed = 475;
    private final Sprite joystickIS;

    public GoalieControl (Body goalie, OrthographicCamera camera, Circle joystickPC, Circle  joystickIPC, Sprite joystickIS){
     this.goalie = goalie;
     this.camera = camera;
     this.joystickPC = joystickPC;
     this.joystickIPC = joystickIPC;
     this.joystickIS = joystickIS;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch = false;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touchPosV.set((float) screenX, (float) screenY, 0.0f);
        camera.unproject(this.touchPosV);
        lengthV.set(touchPosV.x - joystickPC.x, touchPosV.y - joystickPC.y, 0.0f);
        resultLenght = (float) Math.sqrt(((lengthV.x * lengthV.x) + (lengthV.y * lengthV.y)));
        kLength = joystickPC.radius / resultLenght;
        lengthV2.set(lengthV.x * kLength, lengthV.y * kLength, 0.0f);
        currentlengthV.set(joystickIPC.x - joystickPC.x, joystickIPC.y - joystickPC.y, 0.0f);
        maxXY = Math.max(Math.abs(currentlengthV.x), Math.abs(currentlengthV.y));
        currentlengthVSol.set(currentlengthV.x / maxXY, currentlengthV.y /maxXY, 0.0f);
        currentlengthV2.set(currentlengthV.x, currentlengthV.y);
        currentlengthVSol2.set(currentlengthVSol.x, currentlengthVSol.y);
        if (resultLenght < joystickPC.radius) {
            joystickIPC.setPosition(touchPosV.x, touchPosV.y);
            touch = true;
            isStart = true;
            joystickIS.setTexture(AHGMain.joystickI);
        }

        if (resultLenght > joystickPC.radius&& touch)  joystickIPC.setPosition(lengthV2.x + joystickPC.x, lengthV2.y + joystickPC.y);
        if (touch){
            goalie.setLinearVelocity(((float) playerSpeed) * AHGMain.convertPixelsToMeters(currentlengthVSol.x),
                    ((float)playerSpeed) * AHGMain.convertPixelsToMeters(currentlengthVSol.y));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
