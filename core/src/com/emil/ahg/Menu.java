package com.emil.ahg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.Button;


public class Menu extends ScreenAdapter {
    private Texture background,playTexture,title,exitTexture;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle stylePlay,styleExit;
    private TextButton newGame,exit;
    private SpriteBatch batch;

    float userWidth,userHeight, buttonPlayScale,titleWidth,titleHeight, buttonExitScale;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Preferences prefs;
    int bestScore,totalGames,totalShots;
    private String totalSavePercent;
    @SuppressWarnings("DefaultLocale")
    public Menu() {
        prefs = Gdx.app.getPreferences("UserData");
        bestScore =  prefs.getInteger("best", 0);
        totalGames = prefs.getInteger("totalGames",0);
        totalShots =  prefs.getInteger("totalShots",0);
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        userWidth = Gdx.graphics.getWidth();
        userHeight = Gdx.graphics.getHeight();
        buttonPlayScale = userWidth/2;
        buttonExitScale = userWidth/6;
        titleWidth = userWidth/2;
        titleHeight = titleWidth/1.37f;
       background = new Texture(Gdx.files.internal("menu.png"));
      playTexture = new Texture(Gdx.files.internal("play.png"));
      exitTexture = new Texture(Gdx.files.internal("exit.png"));
       title = new Texture(Gdx.files.internal("title.png"));
        skin = new Skin();
        skin.add("play", playTexture);
        skin.add("exit",exitTexture);
         font = new BitmapFont();


        generator = new FreeTypeFontGenerator(Gdx.files.internal("ramona.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (userWidth/20);
        parameter.color = Color.WHITE;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
        skin.add("default", font);
        stylePlay = new TextButton.TextButtonStyle();
        stylePlay.up = skin.newDrawable("play");
        stylePlay.down = skin.newDrawable("play", Color.DARK_GRAY);
        stylePlay.font = font;
        skin.add("stylePlay",  stylePlay );
        newGame  = new TextButton("", skin,"stylePlay");
        newGame.setSize( buttonPlayScale, buttonPlayScale);
        newGame.setPosition(userWidth/2- buttonPlayScale/2,  userHeight/2- buttonPlayScale/2);

        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game game = (Game) Gdx.app.getApplicationListener();
                game.setScreen(new AHGMain());
            }
        });
        styleExit = new TextButton.TextButtonStyle();
        styleExit.up = skin.newDrawable("exit");
        styleExit.down = skin.newDrawable("exit", Color.DARK_GRAY);
        styleExit.font = font;
            skin.add("styleExit",  styleExit );
       exit  = new TextButton("", skin,"styleExit");
        exit.setSize( buttonExitScale, buttonExitScale);
        exit.setPosition(userWidth- buttonExitScale-10,  10);

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               Gdx.app.exit();
            }
        });

        stage.addActor( newGame);
        stage.addActor( exit);
        if (totalShots>0) totalSavePercent = String.format("%.2f",(float) (totalShots-totalGames)/totalShots*100);
        else totalSavePercent = "0,00";
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label bestScoreLabel = new Label("Best score: " + bestScore, labelStyle);
        Label totalSaveLabel = new Label("Total save%: " + totalSavePercent, labelStyle);

        bestScoreLabel.setPosition(userWidth/2-4*parameter.size, userHeight/2 - 0.75f*buttonPlayScale);
        totalSaveLabel.setPosition(userWidth/2-4*parameter.size, userHeight/2 - 0.75f*buttonPlayScale-1.5f*parameter.size);
        stage.addActor(bestScoreLabel);
        stage.addActor(totalSaveLabel);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0,userWidth,userHeight);
        batch.draw(title, userWidth/2-titleWidth/2, userHeight/1.15f-titleHeight/2,titleWidth,titleHeight);
        //font.draw(batch,"Best score: " + bestScore,newGame.getX(),newGame.getY()-80);
       // font.draw(batch,"Total save%: " +  totalSavePercent,newGame.getX(),newGame.getY()-150);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        background.dispose();
        playTexture.dispose();
        skin.dispose();
        font.dispose();
    }
}