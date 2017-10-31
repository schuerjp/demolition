package com.omniworks.demolition.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ScreenUtils
{
	public static float aspectRatio(float width, float height)
	{
		return height/width;
	}
	
	public static Vector2 pixelDensity(Vector2 screenWH, Vector2 worldWH)
	{
		float xDensity = screenWH.x/worldWH.x;
		float yDensity = screenWH.y/worldWH.y;
		
		return new Vector2(xDensity, yDensity);
	}
	
	public static Rectangle screenToWorldBounds(OrthographicCamera camera, Rectangle screenBounds)
	{		
		Vector3 touchXYZ = new Vector3(screenBounds.x, screenBounds.y,0);
		camera.unproject(touchXYZ);
		
		float worldX = touchXYZ.x;
		float worldY = touchXYZ.y;
		
		touchXYZ.set(screenBounds.x+screenBounds.width, screenBounds.y, 0);
		camera.unproject(touchXYZ);

		float worldWidth = Math.abs(touchXYZ.x-worldX);
		
		touchXYZ.set(screenBounds.x, screenBounds.y+screenBounds.height,0);
		camera.unproject(touchXYZ);

		float worldHeight = Math.abs(worldY-touchXYZ.y);
		
		return new Rectangle(worldX, worldY, worldWidth, worldHeight);
		
	}
	
	public static Rectangle stageToWorldBounds(OrthographicCamera camera, Stage stage, Rectangle screenBounds)
	{		
		
		Vector2 iconScreenXY = stage.stageToScreenCoordinates(new Vector2(screenBounds.x, screenBounds.y));

		Vector3 touchXYZ = new Vector3(iconScreenXY.x, iconScreenXY.y,0);
		camera.unproject(touchXYZ);
		
		float worldX = touchXYZ.x;
		float worldY = touchXYZ.y;
		
		touchXYZ.set(iconScreenXY.x+screenBounds.width, iconScreenXY.y, 0);
		camera.unproject(touchXYZ);

		float worldWidth = Math.abs(touchXYZ.x-worldX);
		
		touchXYZ.set(iconScreenXY.x, iconScreenXY.y+screenBounds.height,0);
		camera.unproject(touchXYZ);

		float worldHeight = Math.abs(worldY-touchXYZ.y);
		
		return new Rectangle(worldX, worldY, worldWidth, worldHeight);
		
	}
}
