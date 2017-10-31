package com.omniworks.demolition.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.worldelements.WorldElement;

public abstract class Item
{

	public enum ItemState
	{
		ATTACHED,
		MOVING,
		COMPLETE,
		HOME
	}
	
	public enum ItemType
	{
		BOMB,
		CHAIN
	}
	
	protected WorldElement itemElement;
	protected Group itemGroup;
	protected ItemIcon itemIcon;
	protected ItemState itemState;
	protected ItemType itemType;
	
	protected RevoluteJoint anchor;
	protected boolean isCreated;
	protected float bonus;
	protected GameManager gm;
	
	public static Item createItem (ItemType type, Vector2 pos, GameManager gm) 
	{
		try 
		{
			Item self = null;
			
			if(type == ItemType.BOMB) self = new TouchBombItem();
			else self = new TouchBombItem();

			self.initialize(type, pos, gm);
			
			return self;
			
		} catch (Exception ex) 
		{
			throw new RuntimeException(ex);
		}
	}

	private void initialize(ItemType type, Vector2 pos, GameManager gm)
	{
		this.gm = gm;
		
		setItemType(type);
		setCreated(false);
		setItemState(ItemState.HOME);
		setBonus(0);
		
		anchor = null;
		
		createItemGroup(pos);
		createItemIcon();
	}

	public abstract void updateItem(ItemState state, Vector2 stageXY, Vector2 worldXY, World world, WorldElement element);
	protected abstract void createItemGroup(Vector2 pos);
	protected abstract void createItemIcon();
	public abstract void createItemElement(World world, Vector2 pos);
	public abstract void updateItemElementPosition(Vector2 pos);
	public abstract WorldElement checkIconElementOverlap(GameManager gm);
	public abstract void attachItemElement(World world, WorldElement element);
	
	public ItemIcon itemIcon()
	{
		return itemIcon;
	}
	
	public Group itemGroup()
	{
		return itemGroup;
	}

	public WorldElement itemElement()
	{
		return itemElement;
	}
	
	public ItemState itemState()
	{
		return itemState;
	}

	public ItemType itemType()
	{
		return itemType;
	}

	public void setItemState(ItemState itemState)
	{
		this.itemState = itemState;
	}
	
	public void setItemType(ItemType itemType)
	{
		this.itemType = itemType;
	}
	
	public void setCreated(boolean created)
	{
		isCreated = created;
	}

	public boolean created()
	{
		return isCreated;
	}
			
	public void setBonus(float bonus)
	{
		this.bonus = bonus;
	}
	
	public float bonus()
	{
		return bonus;
	}
	
	protected void applyDeltaPosition(WorldElement itemElement, WorldElement element)
	{
		Vector2 elementCenter = new Vector2(element.center());
		Vector2 itemCenter = new Vector2(itemElement.center());
		
		float bodyAngle = element.elementBody().getAngle()*180/(float)Math.PI;
		
		elementCenter.rotate(-bodyAngle);
		itemCenter.rotate(-bodyAngle);
		
		Vector2 delta = new Vector2(elementCenter);
		delta.sub(itemCenter);

		float itemEdge = itemCenter.x - (Math.signum(delta.x)*itemElement.elementBounds().width/2);
		float elementEdge = elementCenter.x - (Math.signum(delta.x)*element.elementBounds().width/2);
		float yMove = elementEdge - itemEdge;

		if(!((Math.signum(delta.x)*Math.signum(yMove)) > 0))
		{
			yMove = 0;
		}
		
		float xMove = delta.y-(Math.signum(delta.y)*((itemElement.elementBounds().height/2)+(element.elementBounds().height/2)));
		
		itemCenter.set(itemCenter.x+yMove,itemCenter.y+xMove);
		itemCenter.rotate(bodyAngle);
		
		itemElement.elementBody().setTransform(new Vector2(itemCenter.x, itemCenter.y), element.elementBody().getAngle());
	}
	
	public abstract void updateStatus(WorldElement element);
}
