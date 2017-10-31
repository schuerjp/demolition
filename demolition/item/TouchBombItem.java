package com.omniworks.demolition.item;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.utils.ItemUtils;
import com.omniworks.demolition.utils.ShapeUtils;
import com.omniworks.demolition.widgets.BombItemGroup;
import com.omniworks.demolition.worldelements.WorldElement;
import com.omniworks.demolition.worldelements.WorldElement.DamageState;

public class TouchBombItem extends Item
{

	float power = 0;
	float radius = 0;
	
	@Override
	public void updateItem(ItemState state, Vector2 stageXY, Vector2 worldXY, World world, WorldElement element)
	{
		setItemState(state);
		
		if(state == ItemState.HOME)
		{			
			itemIcon.attachIcon();
		}
		else if(state == ItemState.MOVING)
		{
			if(isCreated) destroyItemElement();
			itemIcon.dettachIcon();
			itemIcon.updateIconPosition(stageXY);
			itemIcon.itemIcon.setVisible(true);
		}
		else if(state == ItemState.ATTACHED)
		{			
			if(!isCreated && element != null) 
			{
				createItemElement(world, worldXY);
				attachItemElement(world, element);
				gm.soundManager().getSounds().get("item_drop").play(.25f);
			}
			
			itemIcon.icon().setVisible(false);
		}
		else if(state == ItemState.COMPLETE)
		{
			if(isCreated) destroyItemElement();
			itemIcon = null;
			((BombItemGroup)itemGroup).setCompletedColor();
		}
	}

	@Override
	protected void createItemGroup(Vector2 pos)
	{
		itemGroup = new BombItemGroup(0,pos);
		((BombItemGroup)itemGroup).setIndicatorTint();
	}

	@Override
	public void createItemElement(World world, Vector2 pos)
	{	
		itemElement = ItemUtils.generateItemElement(world, "data/touch_bomb.json");
		itemElement.elementBody().setTransform(itemIcon.worldBounds.x+itemIcon.worldBounds.width/2, itemIcon.worldBounds.y+itemIcon.worldBounds.height/2, 0);
		setCreated(true);
	}
	
	public void updateItemElementPosition(Vector2 pos)
	{
		if(itemElement.elementBody() == null) return;
		
		itemElement.elementBody().setTransform(pos.x, pos.y+2f, 0);
	}
	
	public void anchorItem(World world, Body body)
	{
		if((body == null) || (itemElement.elementBody() == null)) return;
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(body, itemElement.elementBody(), itemElement.elementBody().getPosition());
		jointDef.collideConnected = true;
		
		anchor = (RevoluteJoint)world.createJoint(jointDef);
		
		itemElement.elementBody().setType(BodyType.DynamicBody);
	}
	

	public void destroyItemElement()
	{
		itemElement.setCurrentState(DamageState.DESTROY);
		
		anchor = null;
		
		setCreated(false);
	}
	
	public void setPower(float power)
	{
		this.power = power;
	}
	
	public float power()
	{
		return power;
	}
	
	public float radius()
	{
		return radius;
	}
	
	public void setRadius(float radius)
	{
		this.radius = radius;
	}
	
	@Override
	public WorldElement checkIconElementOverlap(GameManager gm)
	{
		if(itemIcon == null) return null;
		
		WorldElement elementToAttach = null;

		itemIcon.setIconWorldBounds(gm);
		
		Polygon itemPolygon = null;
		Polygon elementPolygon = null;

		itemPolygon = ShapeUtils.rectangleToPolygon(itemIcon.iconWorldBounds());

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
				if(element.isAttachable())
				{
					elementToAttach = element;
					return elementToAttach;
				}
			}
		}
		
		return null;
	}

	@Override
	public void attachItemElement(World world, WorldElement element)
	{
		itemElement.elementBody().setTransform(itemElement.center(), element.elementBody().getAngle());
		applyDeltaPosition(itemElement, element);
		if(anchor == null) anchorItem(world, element.elementBody());
	}

	@Override
	public void updateStatus(WorldElement element)
	{
		if(element == null) ((BombItemGroup)itemGroup).setStatusImage(Assets.buttonAtlas.findRegion("red_button"));
		else ((BombItemGroup)itemGroup).setStatusImage(Assets.buttonAtlas.findRegion("green_button"));
	}

	@Override
	protected void createItemIcon()
	{
		itemIcon = new ItemIcon(Assets.atlas.findRegion("bomb_timer"), itemGroup);
		itemIcon.attachIcon();
	}
}
