package com.omniworks.demolition.level;

import java.util.ArrayList;

public class Level
{
	
	private float time;
	private float score;
	private boolean isLocked;
	private int numStars;
	private int levelNum;
	private ArrayList<WorldItem> items;
	private float timeBonus;
	
	public Level()
	{
		time = 0;
		score = 0;
		isLocked = true;
		numStars = 0;
		levelNum = 0;
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
}
