package com.omniworks.demolition.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.omniworks.demolition.worldelements.WorldElement;

public class ItemUtils
{
	Map allParameters;

	static List _layoutArray;

	static void readLayoutArray(String filepath)
	{
		try
		{
			InputStream fin = Gdx.files.internal(filepath).read();
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));

			StringBuilder buffer = new StringBuilder();
			String line;
			buffer.append("[");
			while ((line = br.readLine()) != null)
			{
				buffer.append(line);
			}
			buffer.append("]");
			fin.close();
			_layoutArray = JSONUtils.listFromJSONString(buffer.toString());
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	public static WorldElement generateItemElement(World world, String filepath) 
	{
		try 
		{
			readLayoutArray(filepath);
			Map layoutMap = (Map)_layoutArray.get(0);
			return addItemElement(layoutMap, "body", null, world);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static List listForKey(Map map, String key)
	{
		if (map.containsKey(key)) return (List)map.get(key);
		return Collections.EMPTY_LIST;
	}

	public static WorldElement addItemElement(Map layoutMap, String key, Class defaultClass, World world)
	{		
		for (Object obj : listForKey(layoutMap, key))
		{
			if (!(obj instanceof Map)) continue;
			Map params = (Map)obj;
			return  WorldElement.createElement(params, world);
		}
		
		return null;
	}

}
