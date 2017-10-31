package com.omniworks.demolition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.omniworks.demolition.managers.GameManager;

public class FontView
{
	private BitmapFont generalFont;
	private BitmapFont generalFont64;
	private BitmapFont scoreFont;
	private BitmapFont timerFont;
	private GameManager gm;
	
	public FontView(GameManager gm)
	{
		this.gm = gm;
		
		initialize();
	}
	
	private void initialize()
	{
		generalFont = Assets.generalFont;
		generalFont64 = Assets.generalFont64;
		scoreFont = Assets.distanceFont;
		timerFont = Assets.timerFont;
		
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		batch.begin();
		
		drawScores(batch);
		//drawFrameRate(batch);
		
		batch.end();
	}
	
	private void drawScores(SpriteBatch batch)
	{
		scoreFont.setScale(1f);
		batch.setShader(Assets.shader);
		
		for(ScoreAnimation score : gm.scoreManager().scoreList())
		{

					Assets.shader.setSmoothing(1/(8*score.scale()/2));
					scoreFont.setScale(score.scale()/2);
					Vector3 pos = new Vector3(score.pos().x,score.pos().y,0f);
					gm.camera().project(pos);
					scoreFont.draw(batch, "" + (int)score.score(), pos.x-scoreFont.getBounds("" + score.score()).height/2, 
							pos.y+scoreFont.getBounds("" + score.score()).height/2);
		}
		
		batch.setShader(null);
		scoreFont.setScale(1f);
	}
	
	private void drawFrameRate(SpriteBatch batch)
	{
		generalFont.setScale(1);
		
		String text =  "FPS: ";
		
		int width = (int)generalFont.getBounds(text).width;
		int height = (int)generalFont.getBounds(text).height;
		
		generalFont.draw(batch, text, Gdx.graphics.getWidth()-1.5f*width, Gdx.graphics.getHeight());
		
		text =  "" + Gdx.graphics.getFramesPerSecond();
		
		width = (int)generalFont.getBounds(text).width;
		height = (int)generalFont.getBounds(text).height;
		
		generalFont.drawMultiLine(batch, text, Gdx.graphics.getWidth()-width, Gdx.graphics.getHeight(), width, HAlignment.RIGHT);
		
		generalFont.setScale(1);
	}

}
