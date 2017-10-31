package com.omniworks.demolition.world;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.omniworks.demolition.utils.JSONUtils;
import com.omniworks.demolition.utils.MathUtils;
import com.omniworks.demolition.worldelements.WorldElement;

public class WorldLayout 
{
	
	ArrayList<WorldElement> worldElements = new ArrayList<WorldElement>();
	ArrayList<Joint> joints = new ArrayList<Joint>();
	
	int worldWidth;
	int worldHeight;
	
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

	public static WorldLayout generateLevel(World world, int stage, int level) 
	{

		String filepath = "data/levels/stage_" + Integer.toString(stage) + "_level_" + Integer.toString(level) + ".json";
		try 
		{
			readLayoutArray(filepath);
			Map layoutMap = (Map)_layoutArray.get(0);
			return new WorldLayout(layoutMap, world);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int numberOfLevels()
	{
		return _layoutArray.size();
	}

	public static List listForKey(Map map, String key)
	{
		if (map.containsKey(key)) return (List)map.get(key);
		return Collections.EMPTY_LIST;
	}

	private void addWorldElements(Map layoutMap, String key, Class defaultClass, World world)
	{		
		for (Object obj : listForKey(layoutMap, key))
		{
			if (!(obj instanceof Map)) continue;
			Map params = (Map)obj;
			WorldElement element = WorldElement.createElement(params, world);
			if(element != null) worldElements.add(element);
		}
	}

	private void  addWorldJoints(Map layoutMap, String key, Class defaultClass, World world)
	{
		for (Object obj : listForKey(layoutMap, key))
		{
			if (!(obj instanceof Map)) continue;
			Map params = (Map)obj;
			joints.add(Box2DFactory.createJoint(params, world));
		}
	}
	
	public WorldLayout (Map layoutMap, World world)
	{
		getWorldSettings(layoutMap);
		addWorldElements(layoutMap, "body", null, world);
		addWorldJoints(layoutMap, "joint", null, world);
	}

	public WorldLayout() {
		// TODO Auto-generated constructor stub
	}

	public void getWorldSettings(Map layoutMap)
	{
		if(layoutMap.containsKey("customProperties"))
		{
			Object worldProps = layoutMap.get("customProperties");

			if(worldProps instanceof ArrayList)
			{
				ArrayList worldList = (ArrayList)worldProps;
				
				for(int i = 0; i < worldList.size(); i++)
				{
					if(worldList.get(i) instanceof Map)
					{
						Map propMap = (Map)worldList.get(i);
						
						if(propMap.containsKey("name"))
						{
							String name = (String)propMap.get("name");
							
							if(name.contains("width"))
							{
								if(propMap.containsKey("int"))
								{
									worldWidth = MathUtils.asInt(propMap.get("int"));
								}
							}
							else if(name.contains("height"))
							{
								if(propMap.containsKey("int"))
								{
									worldHeight = MathUtils.asInt(propMap.get("int"));
								}
							}
						}
					}
				}

			}
		}
	}
	
	public ArrayList<WorldElement> worldElements()
	{
		return worldElements;
	}
	
	public ArrayList<Joint> worldJoints()
	{
		return joints;
	}

	public int getWidth() 
	{
		return worldWidth;
	}

	public int getHeight() 
	{
		return worldHeight;
	}
}
