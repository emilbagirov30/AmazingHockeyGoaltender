package com.emil.ahg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverDialog {
    private Texture background,retryTexture;
    private Skin skin;
    private TextButton.TextButtonStyle styleRetry;
    private TextButton retry;

    public GameOverDialog(Texture background) {
        this.background = background;
        retryTexture = new Texture(Gdx.files.internal("retry.png"));
        skin = new Skin();
        skin.add("retry",retryTexture);
        styleRetry = new TextButton.TextButtonStyle();
        styleRetry.up = skin.newDrawable("retry");
        styleRetry.down = skin.newDrawable("retry", Color.DARK_GRAY);
        skin.add("styleRetry",  styleRetry );

    }

    @SuppressWarnings("DefaultLocale")
    public void render(SpriteBatch batch, BitmapFont font, int userScore, int bestScore, float sv) {
        styleRetry.font = font;
        retry  = new TextButton("", skin,"styleRetry");
        retry.setPosition(500,  590);
        retry.setSize(retryTexture.getWidth(), retryTexture.getHeight());
        retry.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked!");
                Game game = (Game) Gdx.app.getApplicationListener();
                game.setScreen(new AHGMain());
            }
        });

        batch.begin();
        batch.draw(background, 350, 700);
        font.draw(batch, "Your score: " + userScore, 400, 980);
        font.draw(batch, "Save%: " +  String.format("%.2f",sv*100), 400, 920);
        font.draw(batch, "Best score: " + bestScore, 400, 860);
        font.draw(batch, "Best save%: " + String.format("%.2f",(float) bestScore/(float) (bestScore+1)*100), 400, 800);

        batch.end();

    }
}
