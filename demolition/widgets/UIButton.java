package com.omniworks.demolition.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.managers.GameManager.GameState;
import com.omniworks.demolition.screens.LevelsScreen;
import com.omniworks.demolition.screens.MainMenuScreen;
import com.omniworks.demolition.screens.StagesScreen;

public class UIButton extends Image
{
	public enum LevelState
	{
		NEXT,
		PREVIOUS,
		REPLAY
	}
	
	public enum ScreenState
	{
		MAINMENU,
		STAGE,
		LEVEL
	}
	private Image button;
	private GameManager gm;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	
	public UIButton(GameManager gm, TextureRegion textureRegion)
	{
		this.gm = gm;
		
		button = new Image(textureRegion);
		
		initialize();
	}
	
	private void initialize()
	{
	}
	
	public void addListener(final GameState state)
	{
		button.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				gm.setState(state);
			}
		});
	}

	public void addLevelListener(final LevelState levelState)
	{
		button.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				if(levelState == LevelState.NEXT) gm.incrementLevel();
				else if(levelState == LevelState.PREVIOUS) gm.decrementLevel();
				else if(levelState == LevelState.REPLAY) gm.setLevel(gm.levelManager().currentStage(), gm.levelManager().currentLevel());
			}
		});
	}
	
	public void addScreenListener(final ScreenState screenState)
	{
		button.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				if(screenState == ScreenState.MAINMENU) gm.game().setScreen(new MainMenuScreen(gm));
				else if(screenState == ScreenState.STAGE) gm.game().setScreen(new StagesScreen(gm));
				else if(screenState == ScreenState.LEVEL) gm.game().setScreen(new LevelsScreen(gm));
			}
		});
	}

	public Image button()
	{
		return button;
	}

	public void setButton(Image button)
	{
		this.button = button;
	}
	
	public void setBounds(float x, float y, float width, float height)
	{
		button.setBounds(x, y, width, height);
	}
	
}
