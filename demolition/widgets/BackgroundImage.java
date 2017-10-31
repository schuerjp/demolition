package com.omniworks.demolition.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundImage extends Actor
{
	ShapeRenderer shapeRender;
	
	public BackgroundImage()
	{
		shapeRender = new ShapeRenderer();
	}
	
	@Override
	public void draw(SpriteBatch batch, float arg1)
	{
		batch.end();
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		shapeRender.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(0f, 0f, 0f, .9f);
		shapeRender.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRender.end();
		
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		
		batch.begin();
	}
}
