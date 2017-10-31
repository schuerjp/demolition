package com.omniworks.demolition;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.Item.ItemState;
import com.omniworks.demolition.managers.ItemManager;
import com.omniworks.demolition.utils.BodyData;

public class ItemView 
{
	public ItemView()
	{
		
	}
	
	public void render(ItemManager itemManager, SpriteBatch batch)
	{
		for(Item item : itemManager.items())
		{
			drawItemHome(item, batch);
			drawItems(item, batch, itemManager);
		}
	}
	
	private void drawItems(Item item, SpriteBatch batch, ItemManager itemManager)
	{
//		for(Item item2 : itemManager.items())
//		{	
//			for(Body body : item2.itemElement().bodyList())
//			{
//				BodyData bodyData = (BodyData)body.getUserData();
//				float x = body.getPosition().x-(bodyData.size().width()/2);
//				float y = body.getPosition().y-(bodyData.size().height()/2);
//				
//				float angle = MathUtils.radiansToDegrees*body.getAngle();
//				batch.draw(item2.itemElement().bodyTextureMap().get(body), x, y, bodyData.size().width()/2, bodyData.size().height()/2, 
//						   bodyData.size().width(), bodyData.size().height(), 1f, 1f, angle);
//			}
//		}
	}
	
	private void drawItemHome(Item item, SpriteBatch batch)
	{
//		batch.draw(item.itemHome().itemHomeTexture(), item.itemHome().itemHomeBounds().x, item.itemHome().itemHomeBounds().y, 
//				   item.itemHome().itemHomeBounds().width, item.itemHome().itemHomeBounds().height);
	}
}
