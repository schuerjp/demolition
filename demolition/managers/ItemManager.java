package com.omniworks.demolition.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.TouchBombItem;
import com.omniworks.demolition.item.Item.ItemState;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.level.WorldItem;
import com.omniworks.demolition.level.WorldLevel;
import com.omniworks.demolition.utils.OverlapTester;
import com.omniworks.demolition.utils.ScreenUtils;
import com.omniworks.demolition.worldelements.WorldElement;

public class ItemManager 
{
	
	private ArrayList<Item> items;
	private Item currentItem;
	private HashMap<Item,WorldElement> itemElementMap;
	private HashMap<Item,Group> itemGroupMap;
	private GameManager gm;
	
	public ItemManager(GameManager gm)
	{
		this.gm = gm;
		initialize();
	}
	
	private void initialize()
	{
		items = new ArrayList<Item>(); 
		itemElementMap = new HashMap<Item,WorldElement>();
		itemGroupMap = new HashMap<Item,Group>();
	}

	public ArrayList<Item> items()
	{
		return items;
	}

	public void setItems(ArrayList<Item> items) 
	{

		clear();
		
		this.items = items;
		
		for(Item item : items)
		{
			itemElementMap.put(item, item.itemElement());
			itemGroupMap.put(item, item.itemGroup());
		}
	}
	
	public void clear()
	{
		items.clear();
		itemElementMap.clear();
		itemGroupMap.clear();
		currentItem = null;
	}
	
	public void addItem(Item item)
	{
		this.items.add(item);
		itemElementMap.put(item, item.itemElement());
		itemGroupMap.put(item, item.itemGroup());
	}
	
	public boolean removeItem(Item item)
	{
		if(itemElementMap.containsKey(item)) itemElementMap.remove(item);
		if(itemGroupMap.containsKey(item)) itemGroupMap.remove(item);
		return this.items.remove(item);
	}
	
	public void setCurrentItem(Item item)
	{
		this.currentItem = item;
	}
	
	public Item currentItem()
	{
		return this.currentItem;
	}
	
	public void addItemElement(Item item, WorldElement element)
	{
		itemElementMap.put(item, element);
	}
	
	public void removeItemElement(Item item)
	{
		itemElementMap.remove(item);
	}
	
	public HashMap<Item,WorldElement> getItemElementMap()
	{
		return itemElementMap;
	}

	public HashMap<Item,Group> itemGroupMap() 
	{
		return itemGroupMap;
	}
	
	public void createItemsList(WorldLevel level)
	{
		for(WorldItem worldItem : level.items())
		{
			Item item = Item.createItem(worldItem.itemType(), new Vector2(0f,0f), gm);
			
			if(item.itemType() == ItemType.BOMB)
			{
				TouchBombItem bombItem = (TouchBombItem)item;
				bombItem.setPower(worldItem.power());
				bombItem.setRadius(worldItem.radius());
				bombItem.setBonus(worldItem.bonus());
			}
			
			
			items.add(item);
			itemGroupMap.put(item, item.itemGroup());		
		}
	}
	
	public Item itemFromGroup(Group group)
	{
		for(Item item : items)
		{
			if(itemGroupMap.get(item).equals(group))
			{
				return item;
			}
		}
		
		return null;
	}
	
	public Item getTouchedItem(Vector2 stageXY, Vector2 worldXY, OrthographicCamera camera)
	{
		Item currentItem = itemFromGroup(stageXY);
		setCurrentItem(currentItem);
		
		if(currentItem == null)
		{
			currentItem = itemFromIcon(worldXY, camera);
			setCurrentItem(currentItem);
			
			if(currentItem != null)
			{
				if(currentItem.itemState() == ItemState.ATTACHED)
				{
					currentItem.updateItem(ItemState.MOVING, stageXY, worldXY, gm.world(), null);
				}
			}
		}
		
		return currentItem;
	}
	
	public Item itemFromGroup(Vector2 stageXY)
	{
		for(Item item : items)
		{
			Group group = itemGroupMap.get(item);
			
			Rectangle groupBounds = new Rectangle(group.getX(),group.getY(),group.getWidth(),group.getHeight());
			
			if(OverlapTester.pointInRectangle(groupBounds, stageXY))
			{
				return item;
			}
		}
		
		return null;
	}
	
	public Item itemFromElement(Vector2 worldXY)
	{
		for(Item item : items)
		{
			WorldElement element = item.itemElement();
			
			if(element == null) continue;
			
			Rectangle bodyWorldBounds = new Rectangle(element.elementBody().getPosition().x-1,element.elementBody().getPosition().y-1,2,2);
			if(OverlapTester.pointInRectangle(bodyWorldBounds, worldXY))
			{
				return item;
			}

		}
		
		return null;
	}
	
	public Item itemFromIcon(Vector2 worldXY, OrthographicCamera camera)
	{
		for(Item item : items)
		{	
			if(item.itemIcon() == null) continue;
			
			if(OverlapTester.pointInRectangle(item.itemIcon().iconWorldBounds(), worldXY))
			{
				return item;
			}
		}
		
		return null;
	}
}
