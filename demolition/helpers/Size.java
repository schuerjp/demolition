package com.omniworks.demolition.helpers;

public class Size 
{
	
	private float width;
	private float height;
	private float scale;
	
	public Size(float width, float height)
	{
		this.setWidth(width);
		this.setHeight(height);
		this.setScale(1);
		
	}
	
	public Size(float width, float height, float scale)
	{
		this.setWidth(width);
		this.setHeight(height);
		this.setScale(scale);
	}

	public float width()
	{
		return width;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

	public float height()
	{
		return height;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public float scale()
	{
		return scale;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}
	
	public void setSize(Size size)
	{
		this.width = size.width;
		this.height = size.height;
		this.scale = size.scale;
	}
	
}
