package com.omniworks.demolition;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.Item.ItemState;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.level.LevelManager;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.managers.ItemManager;
import com.omniworks.demolition.managers.GameManager.GameState;
import com.omniworks.demolition.screens.GameScreen;
import com.omniworks.demolition.screens.LevelsScreen;
import com.omniworks.demolition.screens.MainMenuScreen;
import com.omniworks.demolition.widgets.BackgroundImage;
import com.omniworks.demolition.widgets.BombItemGroup;
import com.omniworks.demolition.widgets.UIButton;

public class UIOverlay
{
	private Stage stage;
	private Label scoreLabel;
	private Image pauseButton;
	private Image playButton;
	private Image replayButton;
	private Image nextButton;
	private Image prevButton;
	private Image soundButton;
	private Image homeButton;
	private Image settingsButton;
	private Table itemTable;
	private Label highScoreLabel;
	private BackgroundImage backgroundImage;
	private BitmapFont font;
	private Group itemScoreGroup;
	private Label timeBonusLabel;
	private float timeBonusTotal;
	private Label finalCountLabel;
	private float finalCountTotal;
	private float finalCountDuration;
	private UIButton pause;
	private boolean itemAnimationComplete;
	private boolean itemAnimationCreated;
	
	private GameManager gm;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float ITEM_WIDTH = Gdx.graphics.getWidth()*.075f;
	float ITEM_HEIGHT = Gdx.graphics.getWidth()*.075f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float MAXCOLUMNS = 5;
	float DELTATIME = 1/60f;
	
	public UIOverlay(GameManager gm)
	{
		this.gm = gm;

		initialize();
	}
	
	public void initialize()
	{
		stage = new Stage();
		itemTable = new Table();
		itemTable.debug();
		itemScoreGroup = new Group();
		itemAnimationCreated = false;
		
		font = Assets.generalFont;
		Gdx.input.setCatchBackKey(true);

		createScoreLabel();
		createHighScoreLabel();
		createPauseButton();
		createPlayButton();
		createReplayButton();
		createNextButton();
		createPreviousButton();
		createSoundButton();
		createHomeButton();
		createSettingsButton();
		createTableForItems();
		createBackgroundImage();
		
		createOverlay(gm.state());
	}
	
	public void update(float delta)
	{
		
			updateScores();
			
			if(Gdx.input.isKeyPressed(Input.Keys.BACK))
			{
				if((gm.state() == GameState.READY) || (gm.state() == GameState.RUNNING))
				{
					gm.setState(GameState.PAUSED);
					createOverlay(GameState.PAUSED);
				}
				else
				{
					gm.game().setScreen(new LevelsScreen(gm));
				}
				
			}
			
		stage.act(delta);
	}
	
	public void draw()
	{
		stage.draw();
		
		//Table.drawDebug(stage);
	}
	
	private void createTableForItems()
	{
		itemTable.setBounds((2*MARGIN)+ITEM_WIDTH, 5*MARGIN, Gdx.graphics.getWidth()-(2*((2*MARGIN)+ITEM_WIDTH)), Gdx.graphics.getHeight()*.1f);
		float width = ITEM_WIDTH;
		
		for(int i = 0; i < gm.itemManager().items().size(); i++)
		{
			Item item = gm.itemManager().items().get(i);
			
			if(item.itemType() == ItemType.BOMB)
			{
				BombItemGroup bombGroup = (BombItemGroup)item.itemGroup();
				bombGroup.setSize(width);
			}
			
			itemTable.add(item.itemGroup()).pad(5);
			
			if((i+1)%5 == 0)
				itemTable.row();
		}

	}
	
	public void createBackgroundImage()
	{
		backgroundImage = new BackgroundImage();
		
	}
	
	public void createOverlay(GameState gameState)
	{
		stage.clear();
		
		if(gameState == GameState.READY)
		{
			createReadyOverlay();
		}
		if(gameState == GameState.PAUSED)
		{
			createPausedOverlay();
		}
		if(gameState == GameState.FINISHED)
		{
			createFinishedOverlay();
		}
	}
	
	private void createReadyOverlay()
	{
		//stage.addActor(scoreLabel);
		//.addActor(highScoreLabel);
		stage.addActor(pause.button());
		stage.addActor(soundButton);
		//stage.addActor(itemTable);
		stage.addActor(itemScoreGroup);

	}
	
	private void createPausedOverlay()
	{
		stage.addActor(scoreLabel);
		stage.addActor(highScoreLabel);
		stage.addActor(backgroundImage);
		stage.addActor(playButton);
		
		if(!gm.levelManager().stage().levels().get(gm.levelManager().currentLevel()).locked())
		{
			stage.addActor(nextButton);
		}
		
		
		stage.addActor(prevButton);
		stage.addActor(homeButton);
		stage.addActor(soundButton);
		stage.addActor(settingsButton);
		stage.addActor(replayButton);
	}
	
	
	private void createFinishedOverlay()
	{
		stage.addActor(scoreLabel);
		stage.addActor(highScoreLabel);
		stage.addActor(backgroundImage);
		
		if(!gm.levelManager().stage().levels().get(gm.levelManager().currentLevel()).locked())
		{
			stage.addActor(nextButton);
		}
		
		stage.addActor(prevButton);
		stage.addActor(homeButton);
		stage.addActor(soundButton);
		stage.addActor(settingsButton);
		stage.addActor(replayButton);

	}
	
	private void updateScores()
	{
		String text = "score\n" + (int)gm.scoreManager().totalScore();
		scoreLabel.setText(text);
		float textHeight = scoreLabel.getTextBounds().height;
		scoreLabel.setPosition(MARGIN/2, Gdx.graphics.getHeight()-textHeight-MARGIN);
		
		text = "highscore\n" + (int)gm.levelManager().level().score();
		highScoreLabel.setText(text);
		textHeight = highScoreLabel.getTextBounds().height;
		float textWidth = highScoreLabel.getTextBounds().width;
		
		highScoreLabel.setPosition(Gdx.graphics.getWidth()-textWidth-MARGIN/2, Gdx.graphics.getHeight()-textHeight-MARGIN);
	}
	

	private void createScoreLabel()
	{
		scoreLabel = new Label("score\n", new Label.LabelStyle(Assets.generalFont, new Color(Color.WHITE)));
		scoreLabel.setAlignment(Align.bottom | Align.left);
		scoreLabel.setPosition(MARGIN/2, Gdx.graphics.getHeight()-scoreLabel.getTextBounds().height-MARGIN);
	}
	
	private void createHighScoreLabel()
	{
		highScoreLabel = new Label("highscore\n", new Label.LabelStyle(Assets.generalFont, new Color(Color.WHITE)));
		highScoreLabel.setAlignment(Align.bottom | Align.right);

		highScoreLabel.setPosition(Gdx.graphics.getWidth()-highScoreLabel.getTextBounds().width-MARGIN/2, Gdx.graphics.getHeight()-highScoreLabel.getTextBounds().height-MARGIN);
	}
	
	private void createPauseButton()
	{
		pause = new UIButton(gm, Assets.buttonAtlas.findRegion("pause"));
		pause.addListener(GameState.PAUSED);
	}
	
	private void createPlayButton()
	{
		playButton = new Image(Assets.buttonAtlas.findRegion("play"));
		playButton.setBounds(Gdx.graphics.getWidth()/2-(2*BUTTON_WIDTH)/2, Gdx.graphics.getHeight()/2+(BUTTON_HEIGHT), 2*BUTTON_WIDTH, 2*BUTTON_HEIGHT);
		
		playButton.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				gm.setState(GameState.READY);
				createOverlay(gm.state());
			}
		});
		
	}
	
	private void createReplayButton()
	{
		replayButton = new Image(Assets.buttonAtlas.findRegion("replay"));
		replayButton.setBounds(Gdx.graphics.getWidth()/2-(2*BUTTON_WIDTH)/2, Gdx.graphics.getHeight()/2-(3*BUTTON_HEIGHT), 2*BUTTON_WIDTH, 2*BUTTON_HEIGHT);
		
		replayButton.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				gm.setLevel(gm.levelManager().currentStage(), gm.levelManager().currentLevel());
			}
		});
	}
	
	private void createNextButton()
	{
		nextButton = new Image(Assets.buttonAtlas.findRegion("next"));
		nextButton.setBounds(Gdx.graphics.getWidth()-MARGIN-(3*BUTTON_WIDTH), Gdx.graphics.getHeight()/2-BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		nextButton.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				gm.incrementLevel();
			}
		});
	}
	
	private void createPreviousButton()
	{
		prevButton = new Image(Assets.buttonAtlas.findRegion("previous"));
		prevButton.setBounds(MARGIN+(2*BUTTON_WIDTH), Gdx.graphics.getHeight()/2-BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		prevButton.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				gm.decrementLevel();
			}
		});
	}
	
	private void createSoundButton()
	{
		soundButton = new Image(Assets.buttonAtlas.findRegion("volume"));
		soundButton.setBounds(Gdx.graphics.getWidth()-MARGIN-BUTTON_WIDTH, MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		soundButton.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
			}
		});
	}
	
	private void createHomeButton()
	{
		homeButton = new Image(Assets.buttonAtlas.findRegion("home"));
		homeButton.setBounds(Gdx.graphics.getWidth()/2-(2*BUTTON_WIDTH)/2, MARGIN, 2*BUTTON_WIDTH, 2*BUTTON_HEIGHT);
		
		homeButton.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y)
			{
				gm.game().setScreen(new MainMenuScreen(gm));
			}
		});
	}
	
	private void createSettingsButton()
	{
		settingsButton = new Image(Assets.buttonAtlas.findRegion("settings"));
		settingsButton.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	public void createTimeBonusLabel()
	{
		timeBonusLabel = new Label("", new Label.LabelStyle(Assets.generalFont64, new Color(Color.ORANGE)));
		timeBonusLabel.setAlignment(Align.bottom | Align.left);
		timeBonusLabel.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-200);
		stage.addActor(timeBonusLabel);
		
		timeBonusTotal = 0;
	}
	
	public void createFinalCountLabel()
	{
		finalCountLabel = new Label("", new Label.LabelStyle(Assets.generalFont, new Color(Color.WHITE)));
		finalCountLabel.setAlignment(Align.bottom | Align.left);
		finalCountLabel.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		stage.addActor(finalCountLabel);

		finalCountTotal = 0;
		finalCountDuration = (gm.scoreManager().totalScore()*DELTATIME)/(2);
		
	}
	
	public Stage stage()
	{
		return stage;
	}
	
	public void createFinishedAnimation()
	{
		itemAnimationComplete = false;
		itemAnimationCreated = false;
		
		createItemAnimation();
	}
	
	public boolean checkFinshedAnimation()
	{
		if(checkItemAnimations())
		{
			if(checkItemScoreAnimations())
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void createItemAnimation()
	{
		float initialDelay = 0;
		
		for(Item item : gm.itemManager().items())
		{
			if(item.itemState() == ItemState.HOME)
			{
				item.itemGroup().addAction(Actions.delay(initialDelay, Actions.moveTo(item.itemGroup().getX(), 
						item.itemGroup().getY()+(Gdx.graphics.getHeight()*.05f), .25f, Interpolation.linear)));
				initialDelay = initialDelay + .2f;
			}
			else
			{
				itemTable.removeActor(item.itemGroup());
			}
			
		}
		
		setItemAnimationCreated(true);
	}

	public boolean checkItemAnimations()
	{
		
		if(itemTable.getChildren().size == 0) return true;
		
		Iterator<Actor> actorIter = itemTable.getChildren().iterator();
		
		while(actorIter.hasNext())
		{
			Actor actor = actorIter.next();
			
			if((actor instanceof BombItemGroup) && (actor.getActions().size == 0))
			{
				itemScoreGroup.addActor(spawnItemScoreLabel(new Vector2(actor.getX()+actor.getWidth(),actor.getY()+(2*actor.getHeight())), 
						gm.itemManager().itemFromGroup((Group)actor).bonus()));
				actorIter.remove();
			}
		}
		
		if(itemTable.getChildren().size == 0) return true;
		else return false;
	}
	
	private Label spawnItemScoreLabel(Vector2 pos, float bonus)
	{
		gm.scoreManager().addScore(bonus);
		Label tempLabel = new Label("" + (int)bonus, new Label.LabelStyle(Assets.generalFont64, new Color(Color.GREEN)));
		tempLabel.setPosition(pos.x, pos.y);
		
		tempLabel.addAction(Actions.parallel(Actions.delay(.5f,Actions.fadeOut(1f,Interpolation.linear)),
							Actions.moveTo(pos.x-2*BUTTON_WIDTH,pos.y+6*BUTTON_WIDTH,1.5f, Interpolation.linear) ));
		
		return tempLabel;
		
	}

	public boolean checkItemScoreAnimations()
	{
		if(itemScoreGroup.getChildren().size == 0) return true;
		
		Iterator<Actor> actorIter = itemScoreGroup.getChildren().iterator();
		
		while(actorIter.hasNext())
		{
			Actor actor = actorIter.next();
			
			if((actor instanceof Label) && (actor.getActions().size == 0))
			{
				actorIter.remove();
			}
		}
		
		if(itemScoreGroup.getChildren().size == 0) return true;
		else return false;
	}
	
	public boolean isItemAnimationComplete()
	{
		return itemAnimationComplete;
	}

	public boolean isItemAnimationCreated()
	{
		return itemAnimationCreated;
	}

	public void setItemAnimationCreated(boolean itemAnimationCreated)
	{
		this.itemAnimationCreated = itemAnimationCreated;
	}
	

	public void animateItemBonus(float deltaTime)
	{
		gm.scoreManager().addScore(deltaTime*gm.levelManager().level().timeBonus());
		timeBonusTotal += deltaTime*gm.levelManager().level().timeBonus();
		String text = "+" + (int)timeBonusTotal;
		timeBonusLabel.setText(text);
	}
	
	public boolean animateFinalCount()
	{
		if((finalCountTotal + finalCountDuration) > gm.scoreManager().totalScore())
		{
			return true;
		}
		
		finalCountTotal += finalCountDuration;
		
		String text = "" + (int)finalCountTotal;
		finalCountLabel.setText(text);
		
		return false;
	}
}
