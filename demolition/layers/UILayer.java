package com.omniworks.demolition.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.managers.GameManager.GameState;
import com.omniworks.demolition.widgets.BackgroundImage;
import com.omniworks.demolition.widgets.UIButton;
import com.omniworks.demolition.widgets.UIButton.LevelState;
import com.omniworks.demolition.widgets.UIButton.ScreenState;

public class UILayer extends Layer
{
	
	private GameManager gm;
	private Stage stage;
	
	private UIButton pauseButton;
	private UIButton playButton;
	private UIButton replayButton;
	private UIButton nextButton;
	private UIButton prevButton;
	private UIButton soundButton;
	private UIButton homeButton;
	private UIButton settingsButton;
	private BackgroundImage background;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float ITEM_WIDTH = Gdx.graphics.getWidth()*.075f;
	float ITEM_HEIGHT = Gdx.graphics.getWidth()*.075f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float MAXCOLUMNS = 5;
	float DELTATIME = 1/60f;
	
	public UILayer(GameManager gm)
	{
		this.gm = gm;
		
		initialize();
	}
	
	private void initialize()
	{
		stage = new Stage();
		background = new BackgroundImage();
		
		createPauseButton();
		createPlayButton();
		createReplayButton();
		createNextButton();
		createPreviousButton();
		createSoundButton();
		createHomeButton();
		createSettingsButton();
	}
	
	@Override
	public Stage stage()
	{
		return stage;
	}

	@Override
	public void update(float delta)
	{
		stage.act(delta);
	}

	@Override
	public void draw()
	{
		stage.draw();
	}

	@Override
	public void createLayer()
	{
		stage.addActor(pauseButton.button());
		stage.addActor(soundButton.button());
	}
	
	public void updateLayer()
	{
		stage.clear();
		
		if(gm.state() == GameState.READY || gm.state() == GameState.RUNNING || gm.state() == GameState.LEVELEND)
		{
			stage.addActor(pauseButton.button());
			stage.addActor(soundButton.button());
		}
		else if(gm.state() == GameState.PAUSED)
		{
			stage.addActor(background);
			stage.addActor(settingsButton.button());
			stage.addActor(homeButton.button());
			stage.addActor(playButton.button());
			stage.addActor(nextButton.button());
			stage.addActor(prevButton.button());
			stage.addActor(soundButton.button());
			stage.addActor(replayButton.button());
		}
		else if(gm.state() == GameState.FINISHED)
		{
			stage.addActor(background);
			stage.addActor(settingsButton.button());
			stage.addActor(homeButton.button());
			stage.addActor(nextButton.button());
			stage.addActor(prevButton.button());
			stage.addActor(soundButton.button());
			stage.addActor(replayButton.button());
		}
	}

	private void createSettingsButton()
	{
		settingsButton = new UIButton(gm, Assets.buttonAtlas.findRegion("settings"));
		settingsButton.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
		settingsButton.addScreenListener(ScreenState.LEVEL);
	}

	private void createHomeButton()
	{
		homeButton = new UIButton(gm, Assets.buttonAtlas.findRegion("home"));
		homeButton.setBounds(Gdx.graphics.getWidth()/2-(2*BUTTON_WIDTH)/2, MARGIN, 2*BUTTON_WIDTH, 2*BUTTON_HEIGHT);
		homeButton.addScreenListener(ScreenState.MAINMENU);
	}

	private void createSoundButton()
	{
		soundButton = new UIButton(gm, Assets.buttonAtlas.findRegion("volume"));
		soundButton.setBounds(MARGIN, Gdx.graphics.getHeight()-2*(MARGIN+BUTTON_HEIGHT), BUTTON_WIDTH, BUTTON_HEIGHT);
		//soundButton.addListener(GameState.PAUSED);
	}

	private void createPreviousButton()
	{
		prevButton = new UIButton(gm, Assets.buttonAtlas.findRegion("previous"));
		prevButton.setBounds(MARGIN+(2*BUTTON_WIDTH), Gdx.graphics.getHeight()/2-BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		prevButton.addLevelListener(LevelState.PREVIOUS);
	}

	private void createNextButton()
	{
		nextButton = new UIButton(gm, Assets.buttonAtlas.findRegion("next"));
		nextButton.setBounds(Gdx.graphics.getWidth()-MARGIN-(3*BUTTON_WIDTH), Gdx.graphics.getHeight()/2-BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextButton.addLevelListener(LevelState.NEXT);
	}

	private void createReplayButton()
	{
		replayButton = new UIButton(gm, Assets.buttonAtlas.findRegion("replay"));
		replayButton.setBounds(Gdx.graphics.getWidth()/2-(2*BUTTON_WIDTH)/2, Gdx.graphics.getHeight()/2-(3*BUTTON_HEIGHT), 2*BUTTON_WIDTH, 2*BUTTON_HEIGHT);
		replayButton.addLevelListener(LevelState.REPLAY);
	}

	private void createPlayButton() 
	{
		playButton = new UIButton(gm, Assets.buttonAtlas.findRegion("play"));
		playButton.setBounds(Gdx.graphics.getWidth()/2-(2*BUTTON_WIDTH)/2, Gdx.graphics.getHeight()/2+(BUTTON_HEIGHT), 2*BUTTON_WIDTH, 2*BUTTON_HEIGHT);
		playButton.addListener(GameState.READY);
	}

	private void createPauseButton()
	{
		pauseButton = new UIButton(gm, Assets.buttonAtlas.findRegion("pause"));
		pauseButton.setBounds(MARGIN, Gdx.graphics.getHeight()-MARGIN-BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		pauseButton.addListener(GameState.PAUSED);
	}

	public void clear()
	{
		stage.clear();
	}

}
