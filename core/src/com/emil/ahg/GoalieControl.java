package com.emil.ahg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class GoalieControl implements InputProcessor {

    private Body goalie;
    private OrthographicCamera camera;
    private Circle joystickPC,joystickIPC;
    private float currentlength;
    private Vector3 currentlengthV = new Vector3();
    private Vector2 currentlengthV2 = new Vector2();
    private Vector3 currentlengthVSol = new Vector3();
    private  Vector2 currentlengthVSol2 = new Vector2();
    private  float klength;
    private float resultlenght;
    private  float maxXY;
    public static boolean isStart = false;
    private Vector3 lengthV = new Vector3();
    private Vector3 lengthV2 = new Vector3();
    private  Vector3 touchPosV = new Vector3();
    private boolean touch = false;
    private int playerSpeed = 475;
    private Sprite joystickIS;

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
        resultlenght = (float) Math.sqrt((double) ((lengthV.x * lengthV.x) + (lengthV.y * lengthV.y)));
       klength = joystickPC.radius / resultlenght;
       lengthV2.set(lengthV.x * klength, lengthV.y * klength, 0.0f);
        currentlengthV.set(joystickIPC.x - joystickPC.x, joystickIPC.y - joystickPC.y, 0.0f);
        currentlength = (float) Math.sqrt( ((currentlengthV.x * currentlengthV.x) + (currentlengthV.y * currentlengthV.y)));
       maxXY = Math.max(Math.abs(currentlengthV.x), Math.abs(currentlengthV.y));
        currentlengthVSol.set(currentlengthV.x / maxXY, currentlengthV.y /maxXY, 0.0f);
        currentlengthV2.set(currentlengthV.x, currentlengthV.y);
       currentlengthVSol2.set(currentlengthVSol.x, currentlengthVSol.y);
        if (resultlenght < joystickPC.radius) {
            joystickIPC.setPosition(touchPosV.x, touchPosV.y);
            touch = true;
            isStart = true;
            joystickIS.setTexture(AHGMain.joystickI);
        }

        if (resultlenght > joystickPC.radius&& touch)  joystickIPC.setPosition(lengthV2.x + joystickPC.x, lengthV2.y + joystickPC.y);


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
