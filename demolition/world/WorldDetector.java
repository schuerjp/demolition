package com.omniworks.demolition.world;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.omniworks.demolition.AttachmentHandler;
import com.omniworks.demolition.DemoRayCastCallback;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.Item.ItemState;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.managers.GameManager.GameState;
import com.omniworks.demolition.utils.OverlapTester;
import com.omniworks.demolition.worldelements.WorldElement;

public class WorldDetector extends GestureDetector 
{
	private DemoRayCastCallback rayCallback;
	private Vector3 touchXYZ;
	private Vector2 worldXY;
	private Vector2 stageXY;
	private Item currentItem;
	private WorldElement currentElement;
	private WorldElement elementToAttach;
	private AttachmentHandler attachmentHandler;
	private GameManager gm;
	
	public WorldDetector(GameManager gm)
	{
		super(gm.cameraListener());
		this.gm = gm;
		initialize();
	}
	
	private void initialize()
	{
		currentItem = null;
		currentElement = null;
		setElementToAttach(null);
		attachmentHandler = new AttachmentHandler(gm);
		rayCallback = new DemoRayCastCallback(gm);
		
		touchXYZ = new Vector3(0,0,0);
		worldXY = new Vector2(0,0);
		stageXY = new Vector2(0,0);
	}
	
	@Override
	public boolean touchDragged (int x, int y, int pointer) 
	{
		stageXY = stageXY(x,y);		
		worldXY = worldXY(x,y);

		if(currentItem == null) return super.touchDragged((float)x, (float)y, pointer);
		
		if(currentItem.itemState() == ItemState.MOVING)
		{
			currentItem.itemIcon().updateIconPosition(stageXY);
			setElementToAttach(attachmentHandler.checkIconElementOverlap(currentItem));
			gm.elementManager().attachmentList().clear();
			gm.elementManager().addAttachmentElement(elementToAttach);
			currentItem.updateStatus(elementToAttach);
			
			return false;
		}
		
		return super.touchDragged((float)x, (float)y, pointer);
	}


	@Override 
	public boolean touchDown (int x, int y, int pointer, int button) 
	{
		if(!gm.gameTimer().isStarted()) gm.gameTimer().setStarted(true);
		
		gm.cameraListener().setCurrentDistance(0);

		if((gm.state() == GameState.PAUSED) || (gm.state() == GameState.FINISHED)) return false;
		
		stageXY = stageXY(x,y);	
		worldXY = worldXY(x,y);
		
		currentItem = gm.itemManager().getTouchedItem(stageXY, worldXY, gm.camera());
		
		if(currentItem == null) return super.touchDown(x, y, pointer, button);

		if(currentItem.itemState() == ItemState.HOME)
		{
			currentItem.itemIcon().updateIconPosition(stageXY);
			setElementToAttach(attachmentHandler.checkIconElementOverlap(currentItem));
			gm.elementManager().attachmentList().clear();
			gm.elementManager().addAttachmentElement(elementToAttach);
			currentItem.updateStatus(elementToAttach);
			
			currentItem.updateItem(ItemState.MOVING, stageXY, worldXY, gm.world(), elementToAttach);
		}
		else if(currentItem.itemState() == ItemState.ATTACHED)
		{
				rayCallback.perfromRayCast(currentItem);
				currentItem.updateItem(ItemState.COMPLETE, stageXY, worldXY, gm.world(), elementToAttach);
		}
		
		return super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		stageXY = stageXY(x,y);		
		worldXY = worldXY(x,y);
		
		gm.elementManager().attachmentList().clear();
		
		if(currentItem == null) return super.touchUp(x, y, pointer, button);
		
		if(currentItem.itemState() == ItemState.MOVING)
		{
			if(elementToAttach == null)
			{
				currentItem.updateItem(ItemState.HOME, stageXY, worldXY, gm.world(), elementToAttach);
			}
			else
			{
				currentItem.updateItem(ItemState.ATTACHED, stageXY, worldXY, gm.world(), elementToAttach);
				gm.elementManager().addWorldElement(currentItem.itemElement());	
			}
		}
		
		currentItem = null;
		gm.itemManager().setCurrentItem(null);
		
		return super.touchUp(x, y, pointer, button);
	}
	
	private Vector2 worldXY(int x, int y)
	{
		touchXYZ.set(x,y,0);
		gm.camera().unproject(touchXYZ);
		return new Vector2(touchXYZ.x,touchXYZ.y);
	}

	private Vector2 stageXY(int x, int y)
	{
		return gm.itemLayer().stage().screenToStageCoordinates(new Vector2(x,y));
	}

	private Item itemFromActor(Vector2 stageXY)
	{
		Actor actor = gm.itemLayer().stage().hit(stageXY.x, stageXY.y, true);

		if(actor == null) return null;
		
		while(actor != null)
		{
			if(actor instanceof Group)
			{
				return gm.itemManager().itemFromGroup((Group)actor);
			}
			else
			{
				actor = actor.getParent();
			}
		}
		return null;
	}
	
	public Polygon itemPolygon()
	{
		return attachmentHandler.itemPolygon();
	}
	
	public Polygon elementPolygon()
	{
		return attachmentHandler.elementPolygon();
	}

	public WorldElement elementToAttach()
	{
		return elementToAttach;
	}

	public void setElementToAttach(WorldElement elementToAttach)
	{
		this.elementToAttach = elementToAttach;
	}

	public Item currentItem()
	{
		return this.currentItem;
	}
}
