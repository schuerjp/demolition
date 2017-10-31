package com.omniworks.demolition.level;

import java.util.ArrayList;


public class Stage 
{
	private ArrayList<Level> levels = new ArrayList<Level>();

	public ArrayList<Level> levels()
	{
		return levels;
	}

	public void setLevels(ArrayList<Level> levels)
	{
		this.levels = levels;
	}
	
	public void addLevel(Level level)
	{
		this.levels.add(level);
	}
}
