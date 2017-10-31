package com.omniworks.demolition.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.omniworks.demolition.worldelements.WorldElement;
import com.omniworks.demolition.worldelements.WorldElement.ElementType;

public class ElementManager
{
	private ArrayList<WorldElement> elementList;
	private ArrayList<WorldElement> valuablesList;
	private HashMap<Body,WorldElement> bodyElementMap;
	private ArrayList<WorldElement> attachmentList;
	
	public ElementManager()
	{
		initialize();
	}
	
	public ElementManager(ArrayList<WorldElement> elementList)
	{
		initialize();
		
		setElementList(elementList);
	}
	
	
	private void initialize()
	{
		elementList = new ArrayList<WorldElement>();
		valuablesList = new ArrayList<WorldElement>();
		bodyElementMap = new HashMap<Body,WorldElement>();
		attachmentList = new ArrayList<WorldElement>();
	}
	
	public ArrayList<WorldElement> elementList()
	{
		return elementList;
	}
	
	public void addWorldElement(WorldElement element)
	{
		elementList.add(element);
		bodyElementMap.put(element.elementBody(), element);
		
		if(element.elementType() == ElementType.VALUABLE)
		{
			valuablesList.add(element);
		}
	}
	
	public HashMap<Body,WorldElement> bodyElementMap()
	{
		return bodyElementMap;
	}
	
	public void removeWorldElement(WorldElement element)
	{
		if(elementList.contains(element)) elementList.remove(element);
		if(valuablesList.contains(element)) valuablesList.remove(element);
		if(bodyElementMap.containsKey(element.elementBody())) bodyElementMap.remove(element.elementBody());
		if(attachmentList.contains(element)) attachmentList.remove(element);
	}
	
	public void setElementList(ArrayList<WorldElement> elementList)
	{
		this.elementList = elementList;
		
		for(WorldElement element : elementList)
		{
			bodyElementMap.put(element.elementBody(), element);
			
			if(element.elementType() == ElementType.VALUABLE)
			{
				valuablesList.add(element);
			}
		}
	}
	
	public ArrayList<WorldElement> valuablesList()
	{
		return valuablesList;
	}
	
	public WorldElement getElement(String elementID)
	{
		for(WorldElement element : elementList)
		{
			if(element.getElementID().contains(elementID))
			{
				return element;
			}
		}
		
		return null;
	}
	
	public void clear()
	{
		elementList.clear();
		valuablesList.clear();
		bodyElementMap.clear();
		attachmentList.clear();
	}

	public ArrayList<WorldElement> attachmentList()
	{
		return attachmentList;
	}

	public void setAttachmentList(ArrayList<WorldElement> attachmentList)
	{
		this.attachmentList = attachmentList;
	}
	
	public void addAttachmentElement(WorldElement element)
	{
		if(!attachmentList.contains(element)) attachmentList.add(element);
	}
}
