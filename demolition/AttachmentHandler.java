package com.omniworks.demolition;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.managers.ElementManager;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.utils.ScreenUtils;
import com.omniworks.demolition.utils.ShapeUtils;
import com.omniworks.demolition.worldelements.WorldElement;

public class AttachmentHandler 
{

	GameManager gm;
	
	Polygon itemPolygon = null;
	Polygon elementPolygon = null;
	
	public AttachmentHandler(GameManager gm)
	{
		this.gm = gm;
	}
	
	
	
	public WorldElement checkIconElementOverlap(Item item)
	{
		 if(item.itemType() == ItemType.BOMB)
		 {
			 return checkSingleItemOverlap(item);
		 }
		 else
			 return null;
	}
	
	public WorldElement checkSingleItemOverlap(Item item)
	{
		if(item.itemIcon().icon() == null) return null;
		
		WorldElement elementToAttach = null;
		float currentDistance = Float.MAX_VALUE;
		
		item.itemIcon().setIconWorldBounds(gm);
		
		Vector2 iconCenter = new Vector2(item.itemIcon().iconWorldBounds().x+item.itemIcon().iconWorldBounds().width/2,item.itemIcon().iconWorldBounds().y+item.itemIcon().iconWorldBounds().height/2);
		itemPolygon = null;
		elementPolygon = null;

		itemPolygon = ShapeUtils.rectangleToPolygon(item.itemIcon().iconWorldBounds());

		for(WorldElement element : gm.elementManager().elementList())
		{
			if(element == null) continue;

			Body elementBody = element.elementBody();

			if(elementBody == null) continue;

			elementPolygon = element.elementShape();
			
			if(elementPolygon == null) continue;

			elementPolygon.setPosition(element.center().x, element.center().y);
			elementPolygon.setRotation(elementBody.getAngle()*(180/(float)Math.PI));

			if(Intersector.overlapConvexPolygons(elementPolygon, itemPolygon))
			{
				float distance = iconCenter.dst(element.elementBody().getPosition());
				
				if(element.isAttachable() && (distance < currentDistance))
				{
					elementToAttach = element;
					currentDistance = distance;
				}
			}
		}				
		return elementToAttach;
	}
	
	public WorldElement checkItemElementOverlap(Item item, ElementManager elementManager) 
	{
		if(item.itemElement() == null) return null;
		
		WorldElement elementToAttach = null;
		int overlapCount = 0;
		
		itemPolygon = null;
		elementPolygon = null;
		
		Body itemBody = item.itemElement().elementBody();
		
		if(itemBody == null) return null;
		
		itemPolygon = item.itemElement().elementShape();

		updateItemPolygon(itemBody);
		
		for(WorldElement element : elementManager.elementList())
		{
			if(element == null) continue;
			
			Body elementBody = element.elementBody();
			
			if(elementBody == null || (elementBody.equals(itemBody))) continue;
			
			elementPolygon = element.elementShape();
			elementPolygon.setPosition(element.center().x, element.center().y);
			elementPolygon.setRotation(elementBody.getAngle()*(180/(float)Math.PI));
			
			if(elementPolygon == null) continue;
			
			if(Intersector.overlapConvexPolygons(elementPolygon, itemPolygon))
			{
				if(element.isAttachable())
				{
					itemBody.setTransform(item.itemElement().center(), element.elementBody().getAngle());
					updateItemPolygon(itemBody);
					
					applyDeltaPosition(item.itemElement(), element);
					updateItemPolygon(itemBody);
					
					elementToAttach = element;
					return elementToAttach;
				}
			}

		}				
		return null;
	}
	
	private void updateItemPolygon(Body itemBody)
	{
		itemPolygon.setPosition(itemBody.getPosition().x, itemBody.getPosition().y);
		itemPolygon.setRotation(itemBody.getAngle()*(180/(float)Math.PI));	
	}
	
	private void applyDeltaPosition(WorldElement itemElement, WorldElement element)
	{
		Vector2 elementCenter = new Vector2(element.center());
		Vector2 itemCenter = new Vector2(itemElement.center());
		
		float bodyAngle = element.elementBody().getAngle()*180/(float)Math.PI;
		
		elementCenter.rotate(-bodyAngle);
		itemCenter.rotate(-bodyAngle);
		
		Vector2 delta = new Vector2(elementCenter);
		delta.sub(itemCenter);

		float xMove = delta.y-(Math.signum(delta.y)*((itemElement.elementBounds().height/2)+(element.elementBounds().height/2)));
		
		itemCenter.set(itemCenter.x,itemCenter.y+xMove);
		itemCenter.rotate(bodyAngle);
		
		itemElement.elementBody().setTransform(new Vector2(itemCenter.x, itemCenter.y), element.elementBody().getAngle());
	}
	
	public Polygon itemPolygon()
	{
		return itemPolygon;
	}
	
	public Polygon elementPolygon()
	{
		return elementPolygon;
	}
}
