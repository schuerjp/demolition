package com.omniworks.demolition.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.utils.FontUtils;


public class AllBombsGroup extends Group
{
	private Image bombStatus;
	private Image bombBackground;
	private Image bombIndicator;
	private Table bombTable;
	
	private float WIDTH = Gdx.graphics.getWidth()*.05f;
	private float ROWDIV = 3;
	private float COMPLETED_ALPHA = .25f;
	
	private Vector2 pos;
	
	public AllBombsGroup(Vector2 pos)
	{
		this.pos = pos;
		
		this.setPosition(pos.x, pos.y);
		
		initialize();
	}
	
	private void initialize()
	{
		
		bombBackground = new Image(Assets.buttonAtlas.findRegion("bomb_background"));
		bombBackground.setFillParent(true);
		
		bombIndicator = new Image(Assets.buttonAtlas.findRegion("bomb_indicator"));
		bombStatus = new Image(Assets.buttonAtlas.findRegion("red_button"));
		
		bombTable = new Table();
		bombTable.setFillParent(true);
		bombTable.debug();
		
		this.addActor(bombBackground);
		this.addActor(bombTable);
	}
	
	public void setSize(float width)
	{
		this.setWidth(width);
		
		float imageWidth = bombBackground.getDrawable().getMinWidth();
		float imageHeight = bombBackground.getDrawable().getMinHeight();
		
		float scale = imageWidth/width;
		
		this.setHeight(imageHeight/scale);
		
		float indicatorSize = this.getHeight()/ROWDIV;
		
		bombTable.add(bombIndicator).height(indicatorSize).pad(2);
		bombTable.row();
		bombTable.add(bombStatus).expand().height(indicatorSize).width(indicatorSize).center();
	}
	
	public void setBombStatusImage(TextureRegion texture)
	{
		bombStatus.setDrawable(new TextureRegionDrawable(texture));
	}
	
	public void setStatusImage(TextureRegion texture)
	{
		bombStatus.setDrawable(new TextureRegionDrawable(texture));
	}
	
	public void setCompletedColor()
	{
		Color color = this.getColor();
		this.setColor(color.r, color.g, color.b, COMPLETED_ALPHA);
	}
	
	public void setIndicatorTint()
	{
		float randR = (float)Math.random();
		float randG = (float)Math.random();
		float randB =(float)Math.random();

		bombIndicator.setColor(randR, randG, randB, 1);
	}
	
	public Color indicatorTint()
	{
		return bombIndicator.getColor();
	}
}