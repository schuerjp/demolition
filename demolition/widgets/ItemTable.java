package com.omniworks.demolition.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.managers.GameManager;

public class ItemTable
{
	private GameManager gm;
	private Table itemTable;
	
	float ITEM_WIDTH = Gdx.graphics.getWidth()*.075f;
	float ITEM_HEIGHT = Gdx.graphics.getWidth()*.075f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float MAXCOLUMNS = 8;
	
	public ItemTable(GameManager gm)
	{
		this.gm = gm;
		
		itemTable = new Table();
	}
	
	public Table createItemTable()
	{		
		for(int i = 0; i < gm.itemManager().items().size(); i++)
		{
			Item item = gm.itemManager().items().get(i);
			
			if(item.itemType() == ItemType.BOMB)
			{
				BombItemGroup bombGroup = (BombItemGroup)item.itemGroup();
				bombGroup.setSize(ITEM_WIDTH);
			}
			
			itemTable.add(item.itemGroup()).pad(5);
			
			if((i+1)%MAXCOLUMNS == 0)
				itemTable.row();
		}

		return itemTable;
	}
	
	public Table itemTable()
	{
		return itemTable;
	}
}
