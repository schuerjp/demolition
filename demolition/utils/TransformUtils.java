package com.omniworks.demolition.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class TransformUtils
{
	public static float[] rotateRectangle(Rectangle rect, float angle)
	{
		float[] vertices = new float[8];
		final float p1x = rect.x;
		final float p1y = rect.y;
		final float p2x = rect.x+rect.width;
		final float p2y = rect.y;
		final float p3x = rect.x+rect.width;
		final float p3y = rect.y+rect.height;
		final float p4x = rect.x;
		final float p4y = rect.y+rect.height;

		float xCenter = rect.x+(rect.width/2);
		float yCenter = rect.y+(rect.height/2);
		
		
		// rotate
		if (angle != 0)
		{
			final float cos = MathUtils.cosDeg(angle);
			final float sin = MathUtils.sinDeg(angle);

			float xCenterTrans = (cos*xCenter-sin*yCenter);
			float yCenterTrans = (sin*xCenter+cos*yCenter);
			
			float dx = xCenter-xCenterTrans;
			float dy = yCenter-yCenterTrans;
			vertices[0] = (cos * p1x - sin * p1y)+dx;
			vertices[1] = (sin * p1x + cos * p1y)+dy;

			vertices[2] = (cos * p2x - sin * p2y)+dx;
			vertices[3] = (sin * p2x + cos * p2y)+dy;

			vertices[4] = (cos * p3x - sin * p3y)+dx;
			vertices[5] = (sin * p3x + cos * p3y)+dy;
			
			vertices[6] = (cos * p4x - sin * p4y)+dx;
			vertices[7] = (sin * p4x + cos * p4y)+dy;

			return vertices;
		} 
		else 
		{
			vertices[0] = p1x;
			vertices[1] = p1y;

			vertices[2] = p2x;
			vertices[3] = p2y;

			vertices[4] = p3x;
			vertices[5] = p3y;

			vertices[6] = p4x;
			vertices[7] = p4y;
			
			return vertices;
		}
	}
	
	public static Rectangle floatToRectangle(float[] vertices)
	{
		float width = Math.max(Math.abs(vertices[0]-vertices[2]),Math.abs(vertices[0]-vertices[4]));
		float height = Math.max(Math.abs(vertices[1]-vertices[5]),Math.abs(vertices[1]-vertices[5]));
		
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		
		for(int i = 0; i < vertices.length; i++)
		{
			if(i%2 == 0)
			{
				if(vertices[i] < minX)
				{
					minX = vertices[i];
				}
			}
			else
			{
				if(vertices[i] < minY)
				{
					minY = vertices[i];
				}
			}
		}
		Rectangle rect = new Rectangle();
		
		rect.set(minX,minY,width,height);
		
		return rect;
	}

}
