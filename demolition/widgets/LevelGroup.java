package com.omniworks.demolition.widgets;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.utils.FontUtils;


public class LevelGroup extends Group
{
	private int level;
	private LevelImage levelImage;
	private UILabel levelNumber;
	private ArrayList<Image> stars;
	private Table starsTable;
	private Table levelTable;
	
	private float TABLEWIDTH;
	private float DIVISION = 2.6f;
	private float CAPFACTOR = 2.5f;
	private float fontScale;
	
	private int numStars;
	
	public LevelGroup()
	{
		
		initialize();
	}
	
	private void initialize()
	{
		level = 0;
		fontScale = 1;
		
		levelImage = new LevelImage();
		levelImage.setFillParent(true);
		
		stars = new ArrayList<Image>();
		starsTable = new Table();
		starsTable.debug();
		levelTable = new Table();
		levelTable.setFillParent(true);
		
		levelTable.debug();
		
		levelNumber = new UILabel("0", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		levelNumber.setFontScale(fontScale);
		levelNumber.setScale(fontScale,fontScale);
		
		this.addActor(levelImage);
		this.addActor(levelTable);

	}
	
	
	public void setActorBounds()
	{
	}
	
	public void setLevelImageTexture(TextureRegion texture)
	{
		levelImage.setDrawable(new TextureRegionDrawable(texture));
	}
	
	public void setLevelNumberText(String text)
	{
		levelNumber.setText(text);
	}
	
	public void setLevel(int level)
	{
		this.level = level;
		
		setLevelNumberText(Integer.toString(level));
	}
	
	public void setLevel(String level)
	{		
		this.level = Integer.parseInt(level);
		
		setLevelNumberText("");
	}
	
	public int level()
	{
		return this.level;
	}
	
	public void setSize(float width)
	{
		this.setWidth(width);
		
		float imageWidth = levelImage.getDrawable().getMinWidth();
		float imageHeight = levelImage.getDrawable().getMinHeight();
		
		float scale = imageWidth/width;
		
		this.setHeight(imageHeight/scale);
		
		TABLEWIDTH = width/DIVISION;
		
		fontScale = this.getHeight()/(CAPFACTOR*Assets.CAP_HEIGHT);
		
		levelNumber.setFontScale(fontScale);
		levelNumber.setScale(fontScale,fontScale);
		
		levelTable.add(starsTable).width(TABLEWIDTH);
		levelTable.add(levelNumber).expand().height(levelNumber.getTextBounds().height).padBottom(Assets.CAP_OFFSET*fontScale);

		setActorBounds();
	}
	
	public LevelImage levelImage()
	{
		return levelImage;
	}

	public void setLevelImage(LevelImage levelImage)
	{
		this.levelImage = levelImage;
	}

	public Label levelNumber()
	{
		return levelNumber;
	}
	
	private void createStars()
	{
		stars.clear();
		
		float starsSize = Math.min(TABLEWIDTH, this.getHeight()/3);
		starsSize -= (2*starsSize/10);
		
		for(int i = 0; i < numStars; i++)
		{
			stars.add(new Image(Assets.buttonAtlas.findRegion("star")));
			starsTable.row();
			starsTable.add(stars.get(i)).width(starsSize).height(starsSize).center();
		}
	}
	
	public void setNumStars(int numStars)
	{
		this.numStars = numStars;
		
		createStars();
	}
	
	public int numStars()
	{
		return numStars;
	}
}