package com.omniworks.demolition.managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.omniworks.demolition.ScoreAnimation;

public class ScoreManager 
{

	private ArrayList<ScoreAnimation> scoreList;
	private ArrayList<TextureRegion> scoreTexture;
	private float totalScore;
	
	public ScoreManager()
	{
		setScoreList(new ArrayList<ScoreAnimation>());
		setScoreTexture(new ArrayList<TextureRegion>());
		setTotalScore(0);
	}

	public ArrayList<ScoreAnimation> scoreList()
	{
		return scoreList;
	}

	public void setScoreList(ArrayList<ScoreAnimation> scoreList)
	{
		this.scoreList = scoreList;
	}

	public void addScore(ScoreAnimation scoreAnimation)
	{
		setTotalScore(totalScore() + scoreAnimation.score());
		
		this.scoreList.add(scoreAnimation);
	}
	
	public void addScore(float score)
	{
		setTotalScore(totalScore() + score);
	}
	
	public void removeScore(ScoreAnimation scoreAnimation)
	{
		this.scoreList.remove(scoreAnimation);
	}
	
	public ArrayList<TextureRegion> scoreTexture()
	{
		return scoreTexture;
	}

	public void setScoreTexture(ArrayList<TextureRegion> scoreTexture)
	{
		this.scoreTexture = scoreTexture;
	}

	public float totalScore()
	{
		return totalScore;
	}

	public void setTotalScore(float totalScore)
	{
		this.totalScore = totalScore;
	}
	
	public void update(float delta)
	{
		Iterator<ScoreAnimation> iter = scoreList().iterator();
		
		while(iter.hasNext())
		{
			ScoreAnimation score = iter.next();

			score.update(delta);

			if(score.isComplete())
			{
				iter.remove();
			}

		}
	}
	
	public void clear()
	{
		scoreList.clear();
		scoreTexture.clear();
		totalScore = 0;
	}
	
}
