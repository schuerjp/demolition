package com.omniworks.demolition.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.managers.GameManager;

public class ItemView
{
	private GameManager gm;
	
	public ItemView(GameManager gm)
	{
		this.gm = gm;
	}
	
	public void render(ShapeRenderer shapeRenderer)
	{
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(gm.camera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		
		drawItemWorldEffects(shapeRenderer);
		
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL10.GL_BLEND);

	}
	
	private void drawItemWorldEffects(ShapeRenderer shapeRenderer)
	{

		shapeRenderer.setColor(0, .2f, .7f, .1f);
		if(gm.worldDetector().currentItem() != null)
		{
			Item item = gm.worldDetector().currentItem();
			
			if(item.itemIcon() != null)
			{
				ItemIcon itemIcon = gm.worldDetector().currentItem().itemIcon();
			
				if((itemIcon != null) && (item.itemType == ItemType.BOMB))
				{
					float radius = ((TouchBombItem)item).radius();
					
					itemIcon.setIconWorldBounds(gm);
					shapeRenderer.circle(itemIcon.iconWorldBounds().x+itemIcon.iconWorldBounds().width/2, 
							itemIcon.iconWorldBounds().y+itemIcon.iconWorldBounds().height/2, radius, 50);
				}
			}
		}
		
	}
}
