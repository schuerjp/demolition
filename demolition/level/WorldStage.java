package com.omniworks.demolition.level;

import java.util.ArrayList;


public class WorldStage 
{
	private ArrayList<WorldLevel> levels = new ArrayList<WorldLevel>();

	private float score;
	private boolean isLocked;
	private int numStars;
	private int stageNum;
	private String imageName;
	private int completed;
	
	public WorldStage()
	{
		score = 0;
		isLocked = true;
		numStars = 0;
		stageNum = 0;
		setImageName("");
	}
	
	public ArrayList<WorldLevel> levels()
	{
		return levels;
	}

	public void setLevels(ArrayList<WorldLevel> levels)
	{
		this.levels = levels;
	}
	
	public void addLevel(WorldLevel level)
	{
		this.levels.add(level);
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

	public int numStars()
	{
		return numStars;
	}

	public void setNumStars(int numStars)
	{
		this.numStars = numStars;
	}

	public int stageNum()
	{
		return stageNum;
	}

	public void setStageNum(int stageNum)
	{
		this.stageNum = stageNum;
	}

	public String imageName() 
	{
		return imageName;
	}

	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}

	public int completed() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}
	
}