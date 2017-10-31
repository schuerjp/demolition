package com.omniworks.demolition.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.widgets.TimerGroup;
import com.omniworks.demolition.widgets.UILabel;

public class HUDLayer extends Layer
{

	private GameManager gm;
	private Stage stage;
	private UILabel scoreLabel;
	private UILabel scoreTextLabel;
	private UILabel highScoreTextLabel;
	private UILabel highScoreLabel;
	private Table timeTable;
	private Table topTable;
	private Table scoreTable;
	
	private TimerGroup timerGroup;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float ITEM_WIDTH = Gdx.graphics.getWidth()*.075f;
	float ITEM_HEIGHT = Gdx.graphics.getWidth()*.075f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float MAXCOLUMNS = 5;
	float DELTATIME = 1/60f;
	
	float SCORE_LABEL_SCALE = .5f*Assets.PIXELS_SCALE;
	
	public HUDLayer(GameManager gm)
	{
		this.gm = gm;
		
		initialize();
	}

	private void initialize()
	{

		stage = new Stage();
		topTable = new Table();
		scoreTable = new Table();
		scoreTable.debug();
		
		createScoreLabel();
		createScoreTextLabel();
		createHighScoreTextLabel();
		createHighScoreLabel();
		createTimeTable();
		createScoreTable();
		createTopTable();
	}
	


	public void update(float delta)
	{
		updateLabels();
		stage.act(delta);
	}
	
	public void draw()
	{
		stage.draw();
		
		//Table.drawDebug(stage);
	}
	
	public void createLayer()
	{
		stage.addActor(topTable);
	}

	@Override
	public Stage stage()
	{
		return stage;
	}
	
	private void updateLabels()
	{
		scoreLabel.setText("" + (int)gm.scoreManager().totalScore());
		highScoreLabel.setText("" + (int)gm.levelManager().level().score());
		timerGroup.setTime(gm.gameTimer().secAsString());
	}
	
	private void createHighScoreTextLabel()
	{
		scoreTextLabel = new UILabel("score", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		scoreTextLabel.setScale(SCORE_LABEL_SCALE);
		scoreTextLabel.setScale(SCORE_LABEL_SCALE,SCORE_LABEL_SCALE);		
	}

	private void createScoreTextLabel()
	{
		highScoreTextLabel = new UILabel("best", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		highScoreTextLabel.setScale(SCORE_LABEL_SCALE);
		highScoreTextLabel.setScale(SCORE_LABEL_SCALE,SCORE_LABEL_SCALE);		
	}
	
	private void createScoreLabel()
	{
		scoreLabel = new UILabel("0", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		scoreLabel.setScale(SCORE_LABEL_SCALE);
		scoreLabel.setScale(SCORE_LABEL_SCALE,SCORE_LABEL_SCALE);
	}
	
	private void createHighScoreLabel()
	{
		highScoreLabel = new UILabel("0", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		highScoreLabel.setScale(SCORE_LABEL_SCALE);
		highScoreLabel.setScale(SCORE_LABEL_SCALE,SCORE_LABEL_SCALE);
	}
	
	private void createTimeTable()
	{
		timerGroup = new TimerGroup();
		timeTable = timerGroup.timeTable();
		timeTable.debug();
	}
	
	private void createScoreTable()
	{
		scoreTable.add(highScoreTextLabel).height(highScoreTextLabel.getTextBounds().height).top().right().padTop(-Assets.CAP_OFFSET*SCORE_LABEL_SCALE);
		scoreTable.row();
		scoreTable.add(highScoreLabel).height(highScoreLabel.getTextBounds().height).top().right().padTop(-Assets.CAP_OFFSET*SCORE_LABEL_SCALE);
		scoreTable.row();
		scoreTable.add(scoreTextLabel).height(scoreTextLabel.getTextBounds().height).top().right();
		scoreTable.row();
		scoreTable.add(scoreLabel).expandY().fillY().height(scoreLabel.getTextBounds().height).top().right().padTop(-Assets.CAP_OFFSET*SCORE_LABEL_SCALE);
	}
	
	private void createTopTable()
	{
		topTable.setFillParent(true);
		topTable.debug();
		topTable.add().expandY().fillY().width(scoreTextLabel.getTextBounds().width).pad(MARGIN/2);
		topTable.add(timeTable).expand().fill().top().pad(MARGIN/2);
		topTable.add(scoreTable).expandY().top().pad(MARGIN/2);
	}

	public void clear()
	{
		stage.clear();
	}

	@Override
	public void updateLayer() {
		// TODO Auto-generated method stub
		
	}
}
