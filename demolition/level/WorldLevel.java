package com.omniworks.demolition.level;

import java.util.ArrayList;

public class WorldLevel
{
	
	private float time;
	private float score;
	private boolean isLocked;
	private int numStars;
	private int levelNum;
	private String imageName;
	private ArrayList<WorldItem> items;
	private float timeBonus;
	private float maxScore;
	
	public WorldLevel()
	{
		time = 0;
		score = 0;
		isLocked = true;
		numStars = 0;
		levelNum = 0;
		setImageName("");
		items = new ArrayList<WorldItem>();

	}

	public float time()
	{
		return time;
	}

	public void setTime(float time)
	{
		this.time = time;
	}

	public float score()
	{
		return score;
	}

	public void setScore(float score)
	{
		this.score = score;
	}

	public boolean locked()
	{
		return isLocked;
	}

	public void setLocked(boolean isLocked)
	{
		this.isLocked = isLocked;
		

	}

	public int numStars() {
		return numStars;
	}

	public void setNumStars(int numStars)
	{
		this.numStars = numStars;
	}

	public int levelNum()
	{
		return levelNum;
	}

	public void setLevelNum(int levelNum)
	{
		this.levelNum = levelNum;
	}

	public String imageName()
	{		
		if(isLocked) setImageName("locked_background");
		else setImageName("stage_background");
		return imageName;
	}

	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}
	
	public void addItem(WorldItem item)
	{
		this.items.add(item);
	}
	
	public ArrayList<WorldItem> items()
	{
		return items;
	}

	public float timeBonus() {
		return timeBonus;
	}

	public void setTimeBonus(float timeBonus) {
		this.timeBonus = timeBonus;
	}

	public float maxScore() {
		return maxScore;
	}

	public void setMaxScore(float maxScore) {
		this.maxScore = maxScore;
	}
	
}
