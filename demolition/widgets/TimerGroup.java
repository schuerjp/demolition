package com.omniworks.demolition.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.omniworks.demolition.Assets;

public class TimerGroup extends Group
{
	private UILabel timeLabel;
	private UILabel gameTimeLabel;
	private Table timeTable;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float CAPHEIGHT = Assets.distanceFont.getCapHeight();
	
	private float TIME_LABEL_SCALE = .75f*Assets.PIXELS_SCALE;
	private float GAME_LABEL_SCALE = 1.4f*Assets.PIXELS_SCALE;
	
	public TimerGroup()
	{
		initialize();
	}

	private void initialize()
	{
		createTimeLabel();
		createGameTimeLabel();
		addTimeToTable();
	}
	
	private void createTimeLabel()
	{
		timeLabel = new UILabel("TIME", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		timeLabel.setScale(TIME_LABEL_SCALE);
		timeLabel.setScale(TIME_LABEL_SCALE,TIME_LABEL_SCALE);
	}
	
	private void createGameTimeLabel()
	{
		gameTimeLabel = new UILabel("00", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		gameTimeLabel.setScale(GAME_LABEL_SCALE);
		gameTimeLabel.setScale(GAME_LABEL_SCALE,GAME_LABEL_SCALE);
	}
	
	private void addTimeToTable()
	{
		timeTable = new Table();
		timeTable.top();
		
		timeTable.add(timeLabel).height(timeLabel.getTextBounds().height).padTop(-Assets.CAP_OFFSET*timeLabel.getScaleY());
		timeTable.row();
		timeTable.add(gameTimeLabel).height(gameTimeLabel.getTextBounds().height).padTop(-Assets.CAP_OFFSET*gameTimeLabel.getScaleY());
	}
	
	public void setTime(String time)
	{
		gameTimeLabel.setText(time);
	}
	
	public Table timeTable()
	{
		return timeTable;
	}

	public void setTimeTable(Table timeTable)
	{
		this.timeTable = timeTable;
	}

	
}
