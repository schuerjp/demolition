package com.omniworks.demolition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.omniworks.demolition.utils.OverlapTester;

public class CameraListener implements GestureListener 
{
	

	private OrthographicCamera camera;
	private Vector2 worldSize;
	private float currentDistance;
	private Vector2 maxViewport;
	private Rectangle bounds;
	private Rectangle virtualBounds; 
	
	public CameraListener(OrthographicCamera camera, Vector2 worldSize, int pixelsPerMeter)
	{
		this.camera = camera;
		this.worldSize = worldSize;
		currentDistance = 0;
		
		
		maxViewport = CameraHelper.maxCameraViewport(worldSize, new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 64, 2);
		bounds = new Rectangle();
		bounds.set(-maxViewport.x/2,-3,maxViewport.x,maxViewport.y);
		virtualBounds = new Rectangle();
		virtualBounds.set(-maxViewport.x/2,-3,maxViewport.x,maxViewport.y);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		
		if(count == 2)
		{
			camera.zoom = 1;
			camera.position.set(0,(maxViewport.y/2)-5,0);
			camera.update();
		}
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		
		float virtualWidth = (camera.viewportWidth*camera.zoom);
		float virtualHeight = (camera.viewportHeight*camera.zoom);
		
		float virtualXDensity = Gdx.graphics.getWidth()/virtualWidth;
		float virtualYDensity = Gdx.graphics.getHeight()/virtualHeight;

		virtualBounds.set(camera.position.x-(virtualWidth/2)-(deltaX/virtualXDensity),camera.position.y-(virtualHeight/2)+(deltaY/virtualYDensity),virtualWidth,virtualHeight);
		
		if(bounds.contains(virtualBounds))
		{
			camera.position.set(camera.position.x-(deltaX/virtualXDensity), camera.position.y+(deltaY/virtualYDensity), 0);
		}
		else if(((virtualBounds.y+virtualBounds.height) < (bounds.y+bounds.height)) && (virtualBounds.y > bounds.y))
		{
			camera.position.set(camera.position.x, camera.position.y+(deltaY/virtualYDensity), 0);
		}
		else if(((virtualBounds.x+virtualBounds.width) < (bounds.x+bounds.width)) && (virtualBounds.x > bounds.x))
		{
			camera.position.set(camera.position.x-(deltaX/virtualXDensity), camera.position.y, 0);
		}
		
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance)
	{
		float delta = 1;
		
		if(currentDistance != 0)
		{
			delta = currentDistance/distance;
		}
		
		Vector2 minViewport = CameraHelper.minCameraViewport(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 64);
		Vector2 maxViewport = CameraHelper.maxCameraViewport(worldSize, new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 64, 2);

		float virtualWidth = (camera.viewportWidth*camera.zoom);
		float virtualHeight = (camera.viewportHeight*camera.zoom);
		
		if((delta > 1) && (virtualWidth <= maxViewport.x) && (virtualHeight <= maxViewport.y))
		{
			if(virtualWidth != maxViewport.x)
			{
			if(camera.viewportWidth*(camera.zoom + .01) <= maxViewport.x)
				camera.zoom += .02;
			else
				camera.zoom = maxViewport.x/camera.viewportWidth;
			}
		}
		else if((delta < 1) && (virtualWidth >= minViewport.x) && (virtualHeight >= minViewport.y))
		{
			if(virtualWidth != minViewport.x)
			{
			if(camera.viewportWidth*(camera.zoom - .01) >= minViewport.x)
				camera.zoom -= .02;
			else
				camera.zoom = minViewport.x/camera.viewportWidth;
			}
		}
			
		virtualWidth = (camera.viewportWidth*camera.zoom);
		virtualHeight = (camera.viewportHeight*camera.zoom);
		
		virtualBounds.set(camera.position.x-(virtualWidth/2),camera.position.y-(virtualHeight/2),virtualWidth,virtualHeight);

		if(!bounds.contains(virtualBounds))
		{
			float dx = -camera.position.x;
			float dy= (maxViewport.y/2-3)-camera.position.y;
			
			camera.position.set(camera.position.x+(dx/10),camera.position.y+(dy/10),0);
		}
		
		System.out.println(camera.zoom);
		System.out.println(camera.position.y);
		System.out.println(camera.position.x);
		currentDistance = distance;

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCurrentDistance(float distance)
	{
		this.currentDistance = distance;
	}
}
