package com.omniworks.demolition.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.utils.OverlapTester;

public class MainMenuScreen implements Screen, InputProcessor
{
	SpriteBatch batch;
	BitmapFont font;
	Rectangle rect;
	ShapeRenderer shapeRender;
	GameManager gm;
	
	public MainMenuScreen (GameManager gm)
	{
		this.gm = gm;
		
		batch = new SpriteBatch();
		font = Assets.generalFont;
		rect = new Rectangle();
		shapeRender = new ShapeRenderer();	
		
		Gdx.input.setCatchBackKey(true);

		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float delta)
	{		
		float deltaTime = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		font.setScale(4);
		int secWidth = (int)font.getBounds("Play").width;
		int secHeight = (int)font.getBounds("Play").height;
		font.draw(batch, "Play", Gdx.graphics.getWidth()/2-secWidth/2, Gdx.graphics.getHeight()/2+secHeight/2);
		font.setScale(1);
		batch.end();
		
		rect.set(Gdx.graphics.getWidth()/2-secWidth/2, Gdx.graphics.getHeight()/2-secHeight/2, secWidth, secHeight);
		
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(.7f, .2f, .1f, 0);
		shapeRender.rect(Gdx.graphics.getWidth()/2-secWidth/2, Gdx.graphics.getHeight()/2-secHeight/2, secWidth, secHeight);
		shapeRender.end();
		
		if (Gdx.input.justTouched())
		{			
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			
			if(OverlapTester.pointInRectangle(rect, new Vector2(Gdx.input.getX(), Gdx.input.getY())))
				gm.game().setScreen(new StagesScreen(gm));
		}
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
		shapeRender.dispose();
		gm.game().dispose();
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Input.Keys.BACK)
		{
			Gdx.app.exit();
		}
		
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
