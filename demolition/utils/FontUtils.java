package com.omniworks.demolition.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;

public class FontUtils 
{

	public static float scaleByBounds(TextBounds textBounds, Rectangle rect)
	{
		float width = textBounds.width;
		float height = textBounds.height;
		
		float boundWidth = rect.width;
		float boundHeight = rect.height;
		
		float dw = width/boundWidth;
		float dh = height/boundHeight;
		
		return (1/Math.max(dw, dh));		
	}
	
	public static float fontScaleByPixels()
	{
		float targetX = 480;
		float targetY = 800;
		
		float xScale = Gdx.graphics.getWidth()/targetX;
		float yScale = Gdx.graphics.getHeight()/targetY;
		
		return Math.min(xScale, yScale);

	}
}
