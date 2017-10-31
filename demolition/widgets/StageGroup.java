package com.omniworks.demolition.widgets;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.omniworks.demolition.Assets;

public class StageGroup extends Group
{
	private int stage;
	private int numStars;
	private float fontScale;

	private Image stageImage;
	private UILabel stageNumber;
	private UILabel scoreLabel;
	private UILabel levelInfoLabel;
	
	private ArrayList<Image> stars;
	
	private Table starsTable;
	private Table stageTable;
	private Table levelInfoTable;
	private Table infoTable;
	
	private float stageNumberHeight;
	private float scoresHeight;
	
	private float DIVISION = 5.5f;
	private float CAPFACTOR = 2f;
	private float PADDING_PERCENT = .025f;
	private float STAR_RELATIVE = .7f;
	private float SCORE_HEIGHT_DIV = 4.75f;
	
	public StageGroup()
	{
		
		initialize();
	}
	
	private void initialize()
	{
		stage = 0;
		fontScale = 1;
		
		stageImage = new Image();
		stageImage.setFillParent(true);
		
		stageNumber = new UILabel("0", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		stageNumber.setFontScale(fontScale);
		stageNumber.setScale(fontScale,fontScale);
		
		levelInfoLabel = new UILabel("0", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		levelInfoLabel.setFontScale(fontScale);
		levelInfoLabel.setScale(fontScale,fontScale);
		
		scoreLabel = new UILabel("0", new Label.LabelStyle(Assets.distanceFont, new Color(Color.WHITE)));
		scoreLabel.setFontScale(fontScale);
		scoreLabel.setScale(fontScale,fontScale);
		
		stars = new ArrayList<Image>();
		starsTable = new Table();
		starsTable.debug();
		
		stageTable = new Table();
		stageTable.setFillParent(true);
		stageTable.debug();
		
		infoTable = new Table();
		infoTable.debug();
		
		levelInfoTable = new Table();
		levelInfoTable.debug();
		
		this.addActor(stageImage);
		this.addActor(stageTable);

	}

	
	public void setStageImageTexture(TextureRegion texture)
	{
		stageImage.setDrawable(new TextureRegionDrawable(texture));
	}
	
	public void setStageNumberText(String text)
	{
		stageNumber.setText(text);
	}
	
	public void setStage(int stage)
	{
		this.stage = stage;
		
		setStageNumberText(Integer.toString(stage));
	}
	
	public void setStage(String stage)
	{		
		this.stage = Integer.parseInt(stage);
		
		setStageNumberText("");
	}
	
	public int stage()
	{
		return this.stage;
	}
	
	public void setSize(float width)
	{
		this.setWidth(width);
		
		float imageWidth = stageImage.getDrawable().getMinWidth();
		float imageHeight = stageImage.getDrawable().getMinHeight();
		
		float scale = imageWidth/width;
		
		this.setHeight(imageHeight/scale);
		
		stageNumberHeight = width/DIVISION;
		scoresHeight = this.getHeight()/SCORE_HEIGHT_DIV;
		
		fontScale = stageNumberHeight/(CAPFACTOR*Assets.CAP_HEIGHT);
		
		stageNumber.setFontScale(fontScale);
		stageNumber.setScale(fontScale,fontScale);
		
		levelInfoLabel.setFontScale(fontScale);
		levelInfoLabel.setScale(fontScale,fontScale);
		
		scoreLabel.setFontScale(fontScale);
		scoreLabel.setScale(fontScale,fontScale);
		
		levelInfoTable.add(stageNumber).left().height(scoresHeight).padLeft(this.getWidth()*PADDING_PERCENT).padBottom(Assets.CAP_OFFSET*fontScale);
		levelInfoTable.add().expandX().fillX();
		levelInfoTable.add(levelInfoLabel).right().height(scoresHeight).padRight(this.getWidth()*PADDING_PERCENT).padBottom(Assets.CAP_OFFSET*fontScale);
		
		infoTable.add(starsTable).left().height(scoresHeight).padLeft(this.getWidth()*PADDING_PERCENT).padLeft(this.getWidth()*PADDING_PERCENT);
		infoTable.add().expandX().fillX();
		infoTable.add(scoreLabel).right().height(scoresHeight-Assets.CAP_OFFSET*fontScale).padBottom(Assets.CAP_OFFSET*fontScale).padRight(this.getWidth()*PADDING_PERCENT);
		
		stageTable.add(levelInfoTable).expandX().fillX().height(scoresHeight);
		stageTable.row();
		stageTable.add().expand().fill();
		stageTable.row();
		stageTable.add(infoTable).expandX().fillX().height(scoresHeight);
	}
	
	public Image stageImage()
	{
		return stageImage;
	}

	public void setLevelImage(LevelImage stageImage)
	{
		this.stageImage = stageImage;
	}

	public UILabel levelNumber()
	{
		return stageNumber;
	}
	
	private void createStars()
	{
		stars.clear();
		
		float starsSize = scoresHeight*STAR_RELATIVE;
		
		for(int i = 0; i < numStars; i++)
		{
			stars.add(new Image(Assets.buttonAtlas.findRegion("star")));
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
	
	public void setScore(float score)
	{
		scoreLabel.setText(Integer.toString((int)score));
	}
	
	public void setStageInfo(int levels, int total)
	{
		String text = Integer.toString(levels) + "/" + Integer.toString(total);
		levelInfoLabel.setText(text);
	}
}
