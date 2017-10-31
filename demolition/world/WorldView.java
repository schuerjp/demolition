package com.omniworks.demolition.world;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.item.ItemIcon;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.worldelements.WorldElement;

public class WorldView
{
	
	private GameManager gm;
	
	public WorldView(GameManager gm)
	{
		this.gm = gm;
	}
	
	public void render(SpriteBatch batch)
	{
		batch.getProjectionMatrix().set(gm.camera().combined);
		batch.begin();
		
		drawElementImages(batch);
		drawParticleEffects(batch);
		
		batch.end();
	}
	
	private void drawElementImages(SpriteBatch batch)
	{      
		for(WorldElement element : gm.elementManager().elementList())
		{
			if(!element.getElementID().contains("truck") && !element.getElementID().contains("tire"))
			{
				element.draw(batch);
				if(gm.elementManager().attachmentList().contains(element))
				{
					drawAttachmentImages(batch, element);
				}
			}
		}
		
		for(WorldElement element : gm.elementManager().elementList())
		{
			if(element.getElementID().contains("truck") || element.getElementID().contains("tire"))
				element.draw(batch);
		}
	}
	
	private void drawAttachmentImages(SpriteBatch batch, WorldElement element)
	{
		batch.draw(Assets.buttonAtlas.findRegion("highlight"), element.elementSprite().getX()-.1f, element.elementSprite().getY()-.1f, 
										element.elementSprite().getOriginX()+.1f, element.elementSprite().getOriginY()+.1f, element.elementSprite().getWidth()+.2f,
										element.elementSprite().getHeight()+.2f, element.elementSprite().getScaleX(), element.elementSprite().getScaleY(),
										element.elementSprite().getRotation());
	}
	
	private void drawParticleEffects(SpriteBatch batch)
	{
		Iterator<ParticleEffect> particleIter = gm.particleManager().getParticleEffects().iterator();
		
		while(particleIter.hasNext())
		{
			ParticleEffect effect = particleIter.next();
			
			if(effect.isComplete()) particleIter.remove();
			else effect.draw(batch,Gdx.graphics.getDeltaTime());
			
		}
	}
	
}
