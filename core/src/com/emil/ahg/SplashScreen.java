package com.emil.ahg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture splashTexture;
    float userWidth,userHeight,splashScale;
    public SplashScreen() {
        super();
        batch = new SpriteBatch();
        splashTexture = new Texture("icon.png");
        userWidth = Gdx.graphics.getWidth();
        userHeight = Gdx.graphics.getHeight();
        splashScale = userWidth/2;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(splashTexture, userWidth/2-splashScale/2, userHeight/2-splashScale/2,splashScale,splashScale);
        batch.end();
    }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void dispose() {
       splashTexture.dispose();
        batch.dispose();
    }
}