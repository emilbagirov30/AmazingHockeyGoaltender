package com.emil.ahg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import java.awt.Taskbar;
import java.util.Random;

public class AHGMain implements Screen {
	SpriteBatch batch, batchControl;
	Texture field,goalieT,joystick,puckT,limit,start,gameOver;

	static Texture joystickI;
	OrthographicCamera camera, cameraControl;
Sprite joystickS,joystickIS,puckS,goalieS;
	Circle joystickPC, joystickIPC;
	int camHeight;
	int camWidth;
	World world;
	Body board,puck,goalie,boardHorizontalLower,boardHorizontalUpper,boardVerticalLeft,boardVerticalRight;
	BodyDef boardDef,puckDef,goalieDef,boardHorizontalLowerDef,boardHorizontalUpperDef,boardVerticalLeftDef,boardVerticalRightDef;
	Fixture boardFixture,puckFixture,goalieFixture;
	FixtureDef boardFixtureDef,puckFixtureDef,goalieFixtureDef,boardHorizontalLowerFixtureDef,boardHorizontalUpperFixtureDef,boardVerticalLeftFixtureDef,boardVerticalRightFixtureDef;
	ChainShape boardShape,goalieShape;
PolygonShape boardHorizontal,boardVertical;
Box2DDebugRenderer debugRenderer;
int maxY = 250;
	int minY =185;
	float direction;
	Random random;
	private float timeSinceLastPuck = 2;
	GoalieControl goalieControl;
	CircleShape circle;

	FreeTypeFontGenerator generator ;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter;

	BitmapFont font ;
int userScore = -1;
int bestScore,totalGames,totalShots;
boolean stop = false;
	Preferences prefs;
	Texture background,retryTexture;
	Skin skin;
	TextButton.TextButtonStyle styleRetry;
	TextButton retry;
Stage stage;
	GameOverDialog dialogWindow;

	@Override
	public void show() {
		batch = new SpriteBatch();
		batchControl = new SpriteBatch();
		field = new Texture("field.png");
		goalieT = new Texture("goalie.png");
		joystick = new Texture("joystick.png");
		joystickI = new Texture("joystick_inter.png");
		puckT = new Texture("puck.png");
		limit = new Texture("limit.png");
		start = new Texture("start.png");
		gameOver = new Texture("game_over_dialog.png");
		joystickS = new Sprite(joystick);
		joystickIS = new Sprite(start);
		puckS = new Sprite(puckT);
		goalieS = new Sprite(goalieT);
		joystickS.setAlpha(0.9f);
		joystickIS.setAlpha(0.75f);
		joystickPC = new Circle();
		joystickPC.setRadius(75);
		joystickPC.setPosition(740, 400);
		joystickIPC = new Circle();
		joystickIPC.setRadius(30);
		joystickIPC.setPosition(740, 400);
		joystickS.setBounds(joystickPC.x - joystickPC.radius, joystickPC.y - joystickPC.radius, joystickPC.radius * 2.0f, joystickPC.radius * 2.0f);
		joystickIS.setBounds(joystickIPC.x - joystickIPC.radius, joystickIPC.y - joystickIPC.radius, joystickIPC.radius * 2.0f, joystickIPC.radius * 2.0f);
		puckS.setBounds((puckS.getWidth() / 2.0f) + 580.0f, (puckS.getHeight() / 2.0f) + 984.0f, 25.0f, 25.0f);
		goalieS.setBounds(  530+71.5f, 279-48,143,96);
		camera = new OrthographicCamera();
		float screenWidth = (float) Gdx.graphics.getWidth();
		float screenHeight = (float) Gdx.graphics.getHeight();
			camHeight = 800;
			camWidth = (int) ((screenWidth / screenHeight) * 800.0f);

		camera.setToOrtho(false,  camWidth, camHeight);
		camera.zoom =0.5f;
		camera.position.y = 700f;
		camera.position.x = 600.0f;
		cameraControl = new OrthographicCamera();
		cameraControl.setToOrtho(false, (float) camWidth, (float) camHeight);
		cameraControl.zoom =0.5f;
		cameraControl.position.y = 700f;
		cameraControl.position.x = 600f;

		world = new World(new Vector2(0.0f, 0.0f), true);
		boardHorizontal = new PolygonShape();
		boardHorizontal.setAsBox(6, 0.01f);

		boardHorizontalUpperDef = new BodyDef();
		boardHorizontalUpperDef.type = BodyDef.BodyType.StaticBody;
		boardHorizontalUpperDef.position.set(6.1f, 19.75f);
		boardHorizontalUpper = world.createBody(boardHorizontalUpperDef);
		boardHorizontalUpperFixtureDef = new FixtureDef();
		boardHorizontalUpperFixtureDef.shape = boardHorizontal;
		boardHorizontalUpper.createFixture(boardHorizontalUpperFixtureDef);

		boardVertical = new PolygonShape();
		boardVertical.setAsBox(0.01f, 10f);
		boardVerticalLeftDef = new BodyDef();
		boardVerticalLeftDef.type = BodyDef.BodyType.StaticBody;
		boardVerticalLeftDef.position.set(0.25f, 10f);
		boardVerticalLeft = world.createBody(boardVerticalLeftDef);
		boardVerticalLeftFixtureDef = new FixtureDef();
		boardVerticalLeftFixtureDef.shape =boardVertical;
		boardVerticalLeft.createFixture(boardVerticalLeftFixtureDef);

		boardVerticalRightDef = new BodyDef();
		boardVerticalRightDef.type = BodyDef.BodyType.StaticBody;
		boardVerticalRightDef.position.set(11.75f, 10f);
		boardVerticalRight= world.createBody(boardVerticalRightDef);
		boardVerticalRightFixtureDef = new FixtureDef();
		boardVerticalRightFixtureDef.shape =boardVertical;
		boardVerticalRight.createFixture(boardVerticalRightFixtureDef);


		//СhainShape не видят друг друга в физическом мире
		/*
		boardDef = new BodyDef();
		boardDef.type = BodyDef.BodyType.StaticBody;
		boardDef.position.set(0.0f, 0.0f);
		board = world.createBody(this.boardDef);
		boardShape = new ChainShape();
		boardFixtureDef = new FixtureDef();
		boardShape.createChain(new Vector2[]
				      { new Vector2(0.27f, 19.71f), new Vector2(0.27f, 0.2f),
						new Vector2(11.72f, 0.2f), new Vector2(11.72f, 19.72f),
						new Vector2(0.27f, 19.71f)});
		boardFixtureDef.shape = boardShape;
		boardFixture =  board.createFixture(boardFixtureDef);
*/


		puckDef = new BodyDef();
		puckDef.type = BodyDef.BodyType.DynamicBody;
		puckDef.position.set(convertPixelsToMeters(puckS.getX()), convertPixelsToMeters(puckS.getY()));
		puck =  world.createBody(puckDef);
		circle = new CircleShape();
		circle.setRadius(convertPixelsToMeters(puckS.getWidth() / 2));
		puckFixtureDef = new FixtureDef();
		puckFixtureDef.shape = circle;
		puckFixtureDef.density = 0.17f;
		puckFixtureDef.friction = 0.01f;
		puckFixtureDef.restitution = 0.9f;
		puckFixture = puck.createFixture(puckFixtureDef);
		goalieDef = new BodyDef();
		goalieDef.type = BodyDef.BodyType.DynamicBody;
		goalieDef.position.set(convertPixelsToMeters(goalieS.getX()),convertPixelsToMeters(goalieS.getY()));
		goalie = world.createBody(goalieDef);
		goalieShape = new ChainShape();
		goalieFixtureDef = new FixtureDef();
		goalieShape.createChain(new Vector2[]{
				new Vector2(-0.585f,-0.47f), new Vector2(-0.715f,-0.46f),
				new Vector2(-0.715f,-0.42f), new Vector2(-0.635f,-0.36f),
				new Vector2(-0.585f,-0.38f), new Vector2(-0.475f,-0.40f),
				new Vector2(-0.495f,-0.05f), new Vector2(-0.485f,0.11f),
				new Vector2(-0.455f,0.16f), new Vector2(-0.585f,0.31f),
				new Vector2(-0.575f,0.40f), new Vector2(-0.495f,0.47f),
				new Vector2(-0.455f,0.47f), new Vector2(-0.375f,0.39f),
				new Vector2(-0.32f,0.29f), new Vector2(-0.275f,0.07f),
				new Vector2(-0.235f,0.07f), new Vector2(-0.165f,0.02f),
				new Vector2(-0.095f,0.14f), new Vector2(0.085f,0.14f),
				new Vector2(0.185f,0.19f), new Vector2(0.315f,0.32f),
				new Vector2(0.385f,0.33f), new Vector2(0.615f,0.48f),
				new Vector2(0.685f,0.43f), new Vector2(0.405f,0.25f),
				new Vector2(0.465f,0.03f), new Vector2(0.465f,-0.03f),
				new Vector2(0.445f,-0.07f), new Vector2(0.545f,-0.18f),
				new Vector2(0.645f,-0.16f), new Vector2(0.685f,-0.22f),
				new Vector2(0.705f,-0.22f), new Vector2(0.705f,-0.27f),
				new Vector2(0.575f,-0.47f), new Vector2(0.465f,-0.36f),
				new Vector2(0.365f,-0.30f), new Vector2(0.215f,-0.41f),
				new Vector2(-0.21f,-0.41f), new Vector2(-0.365f,-0.30f),
				new Vector2(-0.445f,-0.36f), new Vector2(-0.575f,-0.47f)

		});
		goalieFixtureDef.shape = goalieShape;
		goalieFixtureDef.density = 2.0f;
		goalieFixtureDef.friction = 0.04f;
		goalieFixtureDef.restitution = 0.01f;
		goalieFixture = goalie.createFixture(goalieFixtureDef);
		goalieControl = new GoalieControl(goalie,cameraControl,joystickPC,joystickIPC,joystickIS);
		Gdx.input.setInputProcessor(goalieControl);
		random = new Random();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("ramona.ttf"));
		  parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		  parameter.size = 50;
		parameter.color = Color.BLACK;
		parameter.magFilter = Texture.TextureFilter.Linear;
		parameter.minFilter = Texture.TextureFilter.Linear;
		  font = generator.generateFont(parameter);

		  prefs = Gdx.app.getPreferences("UserData");
         bestScore =  prefs.getInteger("best", 0);
		 totalGames = prefs.getInteger("totalGames",0);
		totalGames++;
		 totalShots =  prefs.getInteger("totalShots",0);
		 retryTexture = new Texture(Gdx.files.internal("retry.png"));
		skin = new Skin();
		skin.add("retry",retryTexture);
		styleRetry = new TextButton.TextButtonStyle();
		styleRetry.up = skin.newDrawable("retry");
		styleRetry.down = skin.newDrawable("retry", Color.DARK_GRAY);
		skin.add("styleRetry",  styleRetry );
		styleRetry.font = font;
		retry  = new TextButton("", skin,"styleRetry");
		retry.setPosition(500,  590);
		retry.setSize(retryTexture.getWidth(), retryTexture.getHeight());
		retry.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game game = (Game) Gdx.app.getApplicationListener();
				game.setScreen(new AHGMain());
			}
		});
		stage = new Stage();
		stage.addActor(retry);
	     dialogWindow = new GameOverDialog(gameOver);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		if (!stop) {
			batch.setProjectionMatrix(camera.combined);
			batchControl.setProjectionMatrix(cameraControl.combined);
			camera.update();
			cameraControl.update();
			world.step(Gdx.graphics.getDeltaTime(), 6, 2);
			joystickS.setPosition(joystickPC.x - joystickPC.radius, joystickPC.y - joystickPC.radius);
			joystickIS.setPosition(joystickIPC.x - joystickIPC.radius, joystickIPC.y - joystickIPC.radius);
			puckS.setPosition(convertMetersToPixels(puck.getPosition().x) - (puckS.getWidth() / 2), convertMetersToPixels(puck.getPosition().y) - (puckS.getHeight() / 2));
			goalieS.setPosition(convertMetersToPixels(goalie.getPosition().x) - (goalieS.getWidth() / 2.0f), convertMetersToPixels(goalie.getPosition().y) - (goalieS.getHeight() / 2.0f));

			if (camera.zoom<=1.65){
				camera.zoom+=0.05f;

			}else{
				cameraControl.zoom=1.7f;
				camera.zoom=1.7f;
			}

			if (convertMetersToPixels(goalie.getPosition().y) > maxY) {
				goalie.setTransform(goalie.getPosition().x, convertPixelsToMeters(maxY), goalie.getAngle());
			}

			if (convertMetersToPixels(goalie.getPosition().y) < minY) {
				goalie.setTransform(goalie.getPosition().x, convertPixelsToMeters(minY), goalie.getAngle());
			}
			if (puckS.getY()>1350){
				timeSinceLastPuck +=3;
			}
			if (GoalieControl.isStart) {
				timeSinceLastPuck += Gdx.graphics.getDeltaTime();
				if (timeSinceLastPuck >= 3) {
					timeSinceLastPuck = 0;
					world.destroyBody(puck);
					createPuck();
					userScore++;
					direction = (random.nextFloat() * 2) - 1;
					puck.setLinearVelocity(485 * convertPixelsToMeters(direction), 485 * convertPixelsToMeters(-1f));
				}
				camera.position.x = goalieS.getX();
			}


			if (camera.position.x > 0.73 * field.getWidth()) {
				camera.position.x = (float) (0.73 * field.getWidth());
			}

			if (camera.position.x < 0.27 * field.getWidth()) {
				camera.position.x = (float) (0.27 * field.getWidth());
			}



		}
			batch.begin();
			batch.draw(field, 0, 0);
			puckS.draw(batch);
			goalieS.draw(batch);
			batch.draw(limit, 28, 119 - limit.getHeight());
			batch.end();
			batchControl.begin();
			if ( GoalieControl.isStart&& userScore>=0) font.draw(batchControl, String.valueOf(userScore), 600, camHeight * 1.7f);
			joystickS.draw(batchControl);
			joystickIS.draw(batchControl);
			batchControl.end();

		if (puckS.getY()<93){
				stop = true;
				Gdx.input.setInputProcessor(stage);
				stage.getViewport().setCamera(cameraControl);
				stage.act();
				stage.draw();
				if (GoalieControl.isStart) {
					saveScore();
				}
				dialogWindow.render(batchControl, font, userScore, bestScore, (float) userScore / (float) (userScore + 1));
				GoalieControl.isStart = false;
		}



	}





	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		saveScore();
		batch.dispose();
		batchControl.dispose();
		field.dispose();
		goalieT.dispose();
		joystick.dispose();
		joystickI.dispose();
		boardShape.dispose();
		goalieShape.dispose();
		boardHorizontal.dispose();
		boardVertical.dispose();
	world.dispose();
	generator.dispose();
	font.dispose();
	skin.dispose();

	}
	public static float convertPixelsToMeters(float pixels) {
		return pixels / 100;
	}

	public static float convertMetersToPixels(float meters) {
		return 100 * meters;
	}

	public void createPuck (){
		puckDef = new BodyDef();
		puckDef.type = BodyDef.BodyType.DynamicBody;
		puckDef.position.set(convertPixelsToMeters(600), convertPixelsToMeters(1000));
		puck = world.createBody(puckDef);
		circle = new CircleShape();
		circle.setRadius(convertPixelsToMeters(puckS.getWidth() / 2));
		puckFixtureDef = new FixtureDef();
		puckFixtureDef.shape = circle;
		puckFixtureDef.density = 0.17f;
		puckFixtureDef.friction = 0.01f;
		puckFixtureDef.restitution = 0.9f;
		puckFixture = puck.createFixture(puckFixtureDef);
	}

	public void saveScore (){
		if (userScore > bestScore) {
			bestScore = userScore;
			prefs.putInteger("best", bestScore);
		}
			prefs.putInteger("totalGames",totalGames);
			prefs.putInteger("totalShots",totalShots+userScore+1);
			prefs.flush();

	}

}
