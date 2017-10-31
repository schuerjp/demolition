package com.omniworks.demolition;

import com.badlogic.gdx.math.Vector2;

public class CameraHelper
{
	public static Vector2 maxCameraViewport(Vector2 worldWH, Vector2 screenWH, float minPixelDensity, float margin)
	{
		float targetWidth = screenWH.x/minPixelDensity;
		float targetHeight = screenWH.y/minPixelDensity;
		
		float deltaW = (worldWH.x+margin)/targetWidth;
		float deltaH = (worldWH.y+margin)/targetHeight;
		
		if((deltaW <= 1) && (deltaH <= 1)) return new Vector2(targetWidth, targetHeight);
		
		float maxDelta = Math.max(deltaW, deltaH);
		
		targetWidth *= maxDelta;
		targetHeight *= maxDelta;
		
		return new Vector2(targetWidth,targetHeight);
		
		
	}
	
	public static Vector2 prefCameraViewport(Vector2 worldWH, Vector2 screenWH, float minPixelDensity, float margin)
	{
		float targetWidth = screenWH.x/minPixelDensity;
		float targetHeight = screenWH.y/minPixelDensity;
		
		float deltaW = (worldWH.x+margin)/targetWidth;
		float deltaH = (worldWH.y+margin)/targetHeight;
		
		if((deltaW <= 1) && (deltaH <= 1))
		{	
			float minDelta = Math.max(deltaW, deltaH);

			targetWidth *= minDelta;
			targetHeight *= minDelta;
			
			return new Vector2(targetWidth, targetHeight);
		}
		
		float maxDelta = Math.max(deltaW, deltaH);
		
		targetWidth *= maxDelta;
		targetHeight *= maxDelta;
		
		return new Vector2(targetWidth,targetHeight);
		
		
	}
	
	public static Vector2 minCameraViewport(Vector2 screenWH, float maxPixelDensity)
	{
		float targetWidth = screenWH.x/maxPixelDensity;
		float targetHeight = screenWH.y/maxPixelDensity;
		
		return new Vector2(targetWidth, targetHeight);
	}
}
