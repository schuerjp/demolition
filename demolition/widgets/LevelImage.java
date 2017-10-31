package com.omniworks.demolition.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class LevelImage extends Image
{
	private Color tintColor;
	
	public LevelImage()
	{
		setTint(new Color(1f, 0f, 1f, 1f));
	}
	
	@Override
	public void draw(SpriteBatch batch, float alpha)
	{
		super.draw(batch, alpha);
	}
	
	public void setTint(Color color)
	{
		tintColor = color;
	}
}
