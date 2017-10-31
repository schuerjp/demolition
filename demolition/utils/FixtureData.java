package com.omniworks.demolition.utils;

import com.badlogic.gdx.math.Vector2;

public class FixtureData 
{
	
	Vector2 minXY;
	Vector2 maxXY;
	String name;
	
	public FixtureData()
	{
		this.minXY = new Vector2(0f,0f);
		this.maxXY = new Vector2(0f,0f);
		this.name = "";

	}
	public void setMinXY(Vector2 minXY)
	{
		this.minXY = minXY;
	}
	
	public void setMinXY(float x, float y)
	{
		this.minXY.set(x,y);
	}
	
	public Vector2 minXY()
	{
		return this.minXY;
	}
	
	public void setMaxXY(Vector2 maxXY)
	{
		this.maxXY = maxXY;
	}
	
	public void setMaxXY(float x, float y)
	{
		this.maxXY.set(x,y);
	}
	
	public Vector2 maxXY()
	{
		return this.maxXY;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String name()
	{
		return this.name;
	}
	
}
