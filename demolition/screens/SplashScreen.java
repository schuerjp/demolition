package com.omniworks.demolition.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;

public class SplashScreen implements Screen
{
	AssetManager manager;
	SpriteBatch batch;
	BitmapFont font;
	Texture splash;
	boolean isLoaded;
	Game game;
	float scale; 
	float desiredWidth;
	float MARGIN = Gdx.graphics.getWidth()*.05f;
	
	private GameManager gm;
	
	public SplashScreen(Game game)
	{

		this.game = game;
		
		initialize();
	}
	
	private void initialize()
	{		
		batch = new SpriteBatch(); 
		Texture texture = new Texture(Gdx.files.internal(Assets.berlinImagePath));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		font = new BitmapFont(Gdx.files.internal(Assets.berlinFontPath), new TextureRegion(texture), false);
		manager = new AssetManager(); 
		
		setLogo();
		
		addFilesToLoad();
	}
	
	private void setLogo()
	{
		splash = new Texture(Gdx.files.internal("data/images/omni_logo.png")); 
		splash.setFilter(TextureFilter.Linear, TextureFilter.Linear); 
		
		float width = splash.getWidth();
		float height = splash.getHeight();
		
		scale = width/height;
		desiredWidth = Gdx.graphics.getWidth()*.5f;
	}
	
	private void drawLoading()
	{
		font.setScale(.75f);
		font.setColor(new Color(62/255f, 181/255f, 225/255f, 1f));
		float width = font.getBounds("Loading...").width;
		float height = font.getBounds("Loading...").height;
		
		font.draw(batch, "Loading...", (Gdx.graphics.getWidth()-width)/2, Gdx.graphics.getHeight()/10);
		
		font.setScale(1f);
	}
	
	@Override 
	public void render(float delta)
	{ 
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); 
		//drawBoarder();

		if(manager.update())
		{ 
			Assets.load(manager);
			gm = new GameManager(game);
			gm.game().setScreen(new MainMenuScreen(gm)); 
		} 

		batch.begin(); 
		batch.draw(splash, (Gdx.graphics.getWidth()-desiredWidth)/2, (Gdx.graphics.getHeight())/2, desiredWidth , desiredWidth/scale); 
		drawLoading();
		batch.end(); 

	} 

	private void addFilesToLoad()
	{
		TextureParameter params = new TextureParameter();
		params.minFilter = TextureFilter.MipMapLinearNearest;
		params.magFilter = TextureFilter.Nearest;
		params.genMipMaps = true;
		
		manager.load(Assets.general64FontPath, BitmapFont.class);
		manager.load(Assets.timerFontPath, BitmapFont.class);
		manager.load(Assets.berlinFontPath, BitmapFont.class);
		manager.load(Assets.distanceFontPath, BitmapFont.class);
		manager.load(Assets.coinSoundPath, Sound.class);
		manager.load(Assets.hitSoundPath, Sound.class);
		manager.load(Assets.jumpSoundPath, Sound.class);
		manager.load(Assets.bombSoundPath, Sound.class);
		manager.load(Assets.woodImpactPath, Sound.class);
		manager.load(Assets.woodBreakPath, Sound.class);
		manager.load(Assets.brickBreakPath, Sound.class);
		manager.load(Assets.idleTruckPath, Sound.class);
		manager.load(Assets.scoreSoundPath, Sound.class);
		manager.load(Assets.itemDropPath, Sound.class);
		manager.load(Assets.musicSoundPath, Music.class);
		manager.load(Assets.atlasPath, TextureAtlas.class);
		manager.load(Assets.buttonAtlasPath, TextureAtlas.class);
		manager.load(Assets.woodParticlePath, Texture.class);
		manager.load(Assets.woodParticlePath2, Texture.class);
	}

	@Override 
	public void show() 
	{ 


	} 

	@Override
	public void resize(int width, int height)
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
		manager.dispose();
	}

}
