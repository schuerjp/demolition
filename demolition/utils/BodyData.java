package com.omniworks.demolition.utils;

import com.badlogic.gdx.math.Vector2;
import com.omniworks.demolition.helpers.Size;

public class BodyData {

	private Vector2 initialPos;
	private String name;
	private int bodyID;
	private Size size;
	
	public BodyData()
	{
		initialPos = new Vector2();
		name = "";
		this.bodyID = -1;
		this.size = new Size(0,0,1);
	}
	
	public void setInitialPos(Vector2 initPos)
	{
		this.initialPos.set(initPos);
	}
	
	public Vector2 initialPos()
	{
		return this.initialPos;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String name()
	{
		return this.name;
	}
	
	public void setBodyID(int id)
	{
		this.bodyID = id;
	}
	
	public int bodyID()
	{
		return this.bodyID;
	}

	public Size size() {
		return size;
	}

	public void setSize(Size size)
	{
		this.size = size;
	}
}
