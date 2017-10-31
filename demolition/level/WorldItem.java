package com.omniworks.demolition.level;

import com.omniworks.demolition.item.Item.ItemType;


public class WorldItem
{
	
	private float power;
	private float radius;
	private ItemType itemType;
	private String itemName;
	private float bonus;
	
	public WorldItem()
	{
		power = 0;
		radius = 0;
		itemType = ItemType.BOMB;
		itemName = "";
	}

	public float power()
	{
		return power;
	}
	
	public void setPower(float power)
	{
		this.power = power;
	}
	
	public float radius()
	{
		return radius;
	}
	
	public void setRadius(float radius)
	{
		this.radius = radius;
	}
	
	public ItemType itemType()
	{
		if(itemName.contains("bomb"))
		{
			itemType = ItemType.BOMB;
		}
		
		return itemType;
	}
	
	public void setItemType(ItemType itemType)
	{
		this.itemType = itemType;
	}
	
	public void setName(String itemName)
	{
		this.itemName = itemName;
	}
	
	public String name()
	{
		return itemName;
	}

	public float bonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}
	
	
	
}