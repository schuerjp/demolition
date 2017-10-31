package com.omniworks.demolition.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.level.LevelManager;
import com.omniworks.demolition.level.WorldLevel;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.widgets.LevelGroup;
import com.omniworks.demolition.widgets.UIButton;
import com.omniworks.demolition.widgets.UIButton.ScreenState;

public class LevelsScreen implements Screen, InputProcessor
{

	private float OVERSCROLL = .1f;
	private float LEVELWIDTH = .5f;
	private int NUMCOLUMNS = 3;
	private float PADDING = (1-LEVELWIDTH)*Gdx.graphics.getWidth()/(2*(NUMCOLUMNS+1));
	private float CLIPMARGIN = Gdx.graphics.getHeight()*.075f;
	private float ARROWSIZE = CLIPMARGIN;//Gdx.graphics.getHeight()*.05f;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float ITEM_WIDTH = Gdx.graphics.getWidth()*.075f;
	float ITEM_HEIGHT = Gdx.graphics.getWidth()*.075f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float MAXCOLUMNS = 5;
	float DELTATIME = 1/60f;
	
	private Stage stage;
	private Image backgroundActor;
	private ScrollPane scrollPane;
	private Table paneTable;
	private Table container;
	private Table stageTable;
	private Table arrowTable;
	
	private Image arrowUp;
	private Image arrowDown;
	private UIButton backButton;
	
	private InputMultiplexer inputMultiplexer;
	private GameManager gm;
	
	public LevelsScreen(GameManager gm)
	{
		this.gm = gm;
		
		initialize();
	}
	
	private void initialize()
	{
		
		//backButton = new UIButton(gm, Assets.buttonAtlas.findRegion("previous"));
		//backButton.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
		//backButton.addScreenListener(ScreenState.STAGE);
		
		createArrowTable();
	
		Gdx.input.setCatchBackKey(true);

		Vector2 initialView = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		stage = new Stage(initialView.x, initialView.y, true);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		backgroundActor = new Image(Assets.atlas.findRegion("background"));
		backgroundActor.setBounds(0, 0, initialView.x, initialView.y);
		
		stage.addActor(backgroundActor);
		stage.addActor(arrowTable);
		//stage.addActor(backButton.button());
		
		container = new Table();
		stage.addActor(container);
		container.setFillParent(true);
		container.debug();
		
		stageTable = new Table();
		
		paneTable = new Table();
		paneTable.debug();

		stageTable.add(paneTable).expand().align(Align.top);

		scrollPane = new ScrollPane(stageTable);
		scrollPane.setupOverscroll(Gdx.graphics.getHeight()*OVERSCROLL, 200, 350);
		scrollPane.setScrollingDisabled(true, false);
		
		addLevelsToPane();
		
		container.add(scrollPane).expand().fill().padTop(CLIPMARGIN).padBottom(CLIPMARGIN);

	}
	
	private void createArrowTable()
	{		
		arrowUp = new Image(Assets.buttonAtlas.findRegion("arrow_up"));
		arrowDown = new Image(Assets.buttonAtlas.findRegion("arrow_down"));
		
		arrowTable = new Table();
		arrowTable.setFillParent(true);
		
		arrowTable.add(arrowUp).expand().top().height(ARROWSIZE).width(ARROWSIZE);
		arrowTable.row();
		arrowTable.add(arrowDown).expand().bottom().height(ARROWSIZE).width(ARROWSIZE);		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta)
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		checkArrows();
		
		stage.act(deltaTime);
		stage.draw();
		
		//Table.drawDebug(stage);
	}

	private void checkArrows()
	{
		float top = scrollPane.getTop();
		if(scrollPane.getVisualScrollY() > 0)
		{
			arrowUp.setVisible(true);
		}
		else
		{
			arrowUp.setVisible(false);
		}
		
		if(scrollPane.getVisualScrollY() < scrollPane.getMaxY())
		{
			arrowDown.setVisible(true);
		}
		else
		{
			arrowDown.setVisible(false);
		}
	}

	private void addLevelsToPane()
	{
		ArrayList<WorldLevel> levelsList = gm.levelManager().stage().levels();
		
		for(int i = 0; i < levelsList.size(); i++)
		{
			LevelGroup levelGroup = new LevelGroup();
			
			if((i%NUMCOLUMNS) == 0)
			{
				paneTable.row().pad(PADDING);
			}
			
			paneTable.add(levelGroup).expandY();
			
			if(levelsList.get(i).locked())
			{
				levelGroup.setLevelImageTexture(Assets.buttonAtlas.findRegion("level_locked"));
				levelGroup.setSize(Gdx.graphics.getWidth()*LEVELWIDTH/NUMCOLUMNS);
				levelGroup.setLevel(Integer.toString(levelsList.get(i).levelNum()));
			}
			else 
			{
				levelGroup.setLevelImageTexture(Assets.buttonAtlas.findRegion("level_background"));
				levelGroup.setSize(Gdx.graphics.getWidth()*LEVELWIDTH/NUMCOLUMNS);
				levelGroup.setNumStars(levelsList.get(i).numStars());
				levelGroup.setLevel(levelsList.get(i).levelNum());
			}

			levelGroup.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y)
				{
					event.getListenerActor();
					Actor listenerActor = event.getListenerActor();
					if(listenerActor instanceof LevelGroup)
					{
						LevelGroup stageGroup = (LevelGroup)listenerActor;
						gm.levelManager().setCurrentLevel(stageGroup.level());
						
						if(!gm.levelManager().level().locked())
							gm.createLevel();
					}

				}
			});
		}
	}
	
	@Override
	public void resize(int width, int height)
	{
		Vector2 resizeView = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		stage.setViewport(resizeView.x, resizeView.y, true);
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
		stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Input.Keys.BACK)
		{
			gm.game().setScreen(new StagesScreen(gm));
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