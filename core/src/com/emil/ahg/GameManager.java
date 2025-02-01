package com.emil.ahg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Timer;

public class GameManager extends Game {
    @Override
    public void create() {
        setScreen(new SplashScreen());
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                setScreen(new Menu());
            }
        }, 1);
    }
}
