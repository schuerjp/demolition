package com.omniworks.demolition.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.item.ItemIcon;
import com.omniworks.demolition.layers.Layer;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.managers.GameManager.GameState;
import com.omniworks.demolition.utils.DistanceFieldShader;
import com.omniworks.demolition.utils.ScreenUtils;
import com.omniworks.demolition.worldelements.WorldElement;

public class GameScreen implements Screen
{
	private ShapeRenderer shapeRender;
	private SpriteBatch batch;
	private Box2DDebugRenderer renderer;
	
	private Sprite backgroundSprite;
		
	private GameManager gm;
	
	public GameScreen(GameManager gm)
	{
		
		this.gm = gm;
		this.initialize();
	}

	private void initialize()
	{			
		Gdx.input.setCatchBackKey(true);
		
		shapeRender = new ShapeRenderer();
		renderer = new Box2DDebugRenderer();

		backgroundSprite = Assets.atlas.createSprite("background");
		backgroundSprite.setBounds(-gm.camera().viewportWidth/2, -3 , gm.camera().viewportWidth,gm.camera().viewportHeight);
				
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta)
	{		
		float deltaTime = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gm.camera().update();
		
		batch.getProjectionMatrix().set(gm.camera().combined);
		batch.begin();
		backgroundSprite.draw(batch);
		batch.end();
		
		update(deltaTime);
		draw(deltaTime);
		for(Layer layer : gm.layers())
		{
			layer.draw();
		}

		batch.getProjectionMatrix().set(gm.camera().combined);
		//renderer.render(gm.worldModel().getWorld(), batch.getProjectionMatrix());
	}
	
	private void update(float delta)
	{		
		
		if(Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			if(gm.state() != GameState.PAUSED) gm.setState(GameState.PAUSED);
			else gm.game().setScreen(new LevelsScreen(gm));
		}
		
		for(Layer layer : gm.layers())
		{
			layer.update(delta);
		}
		
		if(gm.state() == GameState.READY)
		{
			updateReady(delta);
		}
		else if(gm.state() == GameState.RUNNING)
		{
			updateRunning(delta);
		}
		else if(gm.state() == GameState.PAUSED)
		{
			//updatePaused(delta);
		}
		else if(gm.state() == GameState.LEVELEND)
		{
			updateLevelEnd(delta);
		}
		else if(gm.state() == GameState.FINISHED)
		{
			updateFinished(delta);
		}
		else if(gm.state() == GameState.RESTART)
		{
		}
		

	}
	
	private void draw(float delta)
	{

		
		if(gm.state() == GameState.READY)
		{
			drawReady();
		}
		else if(gm.state() == GameState.RUNNING)
		{
			drawRunning();
		}
		else if(gm.state() == GameState.PAUSED)
		{
			drawPaused();
		}
		else if(gm.state() == GameState.LEVELEND)
		{
			drawLevelEnd();
		}
		else if(gm.state() == GameState.FINISHED)
		{
			drawFinished();
		}
		

	}
	
	private void updateReady(float delta)
	{
		if(gm.gameTimer().isStarted())
		{
			Assets.idleTruckSound.loop(.025f);
			gm.setState(GameState.RUNNING);
		}
	}
	
	
	private void updateRunning(float delta)
	{		
		gm.gameTimer().update(delta);
		gm.worldModel().step(delta);

		if(gm.elementManager().valuablesList().isEmpty() || gm.gameTimer().isComplete())
		{
			gm.gameTimer().setComplete(true);
			gm.worldModel().updateWorldFinished();
			gm.setState(GameState.LEVELEND);
		}
		
		gm.scoreManager().update(delta);
	}
	
	private void updateLevelEnd(float delta)
	{
		Assets.idleTruckSound.stop();
		WorldElement worldElement = gm.elementManager().getElement("truck");
		
		if(worldElement == null) return;
		
		if(!checkElementBounds(worldElement))
		{
				gm.worldModel().step(delta);
		}
		else
		{
			gm.saveLevel();
			gm.setState(GameState.FINISHED);
			
		}
		
		gm.scoreManager().update(delta);
	}
	

	
	private void updateFinished(float delta)
	{	
		gm.scoreManager().update(delta);
	}

	
	private void drawReady()
	{
		gm.worldView().render(batch);
		gm.itemView().render(shapeRender);
		gm.fontView().draw(batch);
	}
	
	
	private void drawRunning()
	{
		gm.worldView().render(batch);
		gm.itemView().render(shapeRender);
		gm.fontView().draw(batch);
	}
	
	private void drawPaused()
	{
		gm.worldView().render(batch);
		gm.itemView().render(shapeRender);
		gm.fontView().draw(batch);	
	}
	
	private void drawLevelEnd()
	{
		gm.worldView().render(batch);
		gm.itemView().render(shapeRender);
		gm.fontView().draw(batch);
	}
	
	private void drawFinished()
	{
		gm.worldView().render(batch);
	}
	
	private boolean checkElementBounds(WorldElement element)
	{
		return (element.elementBody().getPosition().x + element.elementBounds().width) < (gm.camera().position.x-(gm.camera().viewportWidth/2));
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

	}

	@Override
	public void pause() 
	{
		gm.saveLevel();
		
		if((gm.state() == GameState.READY) || (gm.state() == GameState.RUNNING))
		{
			gm.setState(GameState.PAUSED);
		}
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		shapeRender.dispose();
		renderer.dispose();
	}

}
