package com.omniworks.demolition.level;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.omniworks.demolition.Assets;

public class LevelManager
{
		
	private ArrayList<WorldStage> stages = new ArrayList<WorldStage>();
	private ArrayList<WorldStage> levelConfig;
	private int currentStage;
	private int currentLevel;
	
	public LevelManager()
	{
		levelConfig = new ArrayList<WorldStage>();
		stages = new ArrayList<WorldStage>();
		
		readLevels(Assets.levelsIn);
	}

	public ArrayList<WorldStage> readLevels(String file)
	{		
		try 
		{

			
			XmlReader xmlReader = new XmlReader();
			Element element = null;
			
			if(checkFileExists(file))
			{
		        element = xmlReader.parse(Gdx.files.internal("data/" + file));
			}
			else
			{
				element = xmlReader.parse(Gdx.files.internal("data/" + file));
			}
			
	        mapLevels(element);
	        setStages(stages);
	    } 
		catch (IOException e)
	    {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		
        return stages;

	}
	
	public void writeLevels(ArrayList<WorldStage> levelConfig, String file)
	{		
		try 
		{
			
			StringWriter writer = new StringWriter();

			XmlWriter xmlWriter = new XmlWriter(writer);
			
			xmlWriter.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			
			xmlWriter.element("Game");
			
			for(int i = 0; i < levelConfig.size(); i++)
			{
				WorldStage stage = levelConfig.get(i);
				
				
				xmlWriter.element("Stage");
				xmlWriter.element("Number").write(Integer.toString(stage.stageNum()));
				xmlWriter.pop();
				xmlWriter.element("Image").write(stage.imageName());
				xmlWriter.pop();
				xmlWriter.element("Stars").write(Integer.toString(stage.numStars()));
				xmlWriter.pop();
				xmlWriter.element("Completed").write(Integer.toString(stage.numStars()));
				xmlWriter.pop();
				xmlWriter.element("Locked").write(Boolean.toString(stage.locked()));
				xmlWriter.pop();
				xmlWriter.element("Score").write(Float.toString(stage.score()));
				xmlWriter.pop();
				
				ArrayList<WorldLevel> levels = stage.levels();

				for(int j = 0; j < levels.size(); j++)
				{
					WorldLevel level = levels.get(j);
					
					xmlWriter.element("Level");
					xmlWriter.element("Number").write(Integer.toString(level.levelNum()));
					xmlWriter.pop();
					xmlWriter.element("Image").write(level.imageName());
					xmlWriter.pop();
					xmlWriter.element("Stars").write(Integer.toString(level.numStars()));
					xmlWriter.pop();
					xmlWriter.element("Locked").write(Boolean.toString(level.locked()));
					xmlWriter.pop();
					xmlWriter.element("Score").write(Float.toString(level.score()));
					xmlWriter.pop();
					xmlWriter.element("Time").write(Float.toString(level.time()));
					xmlWriter.pop();
					xmlWriter.element("TimeBonus").write(Float.toString(level.timeBonus()));
					xmlWriter.pop();
					xmlWriter.element("MaxScore").write(Float.toString(level.maxScore()));
					xmlWriter.pop();

					ArrayList<WorldItem> items = level.items();
					
					for(int k = 0; k < items.size(); k++)
					{
						WorldItem item = items.get(k);
						
						xmlWriter.element("Item");
						xmlWriter.element("Bonus").write(Float.toString(item.bonus()));
						xmlWriter.pop();
						xmlWriter.element("Type").write(item.name());
						xmlWriter.pop();
						xmlWriter.element("Radius").write(Float.toString(item.radius()));
						xmlWriter.pop();
						xmlWriter.element("Power").write(Float.toString(item.power()));
						xmlWriter.pop();
						xmlWriter.pop();
					}

					xmlWriter.pop();
					
				}
				
				xmlWriter.pop();
			}
			
			xmlWriter.pop();
			
			FileHandle xmlOut;
			
			if(checkFileExists(file))
			{
				xmlOut = Gdx.files.local(file);
			}
			else
			{
				xmlOut = Gdx.files.local(file);
			}
			
			Writer fileWriter = xmlOut.writer(false);
			fileWriter.write(writer.toString());
			
			xmlWriter.close();
			fileWriter.close();
	
	    } 
		catch (IOException e)
	    {
			throw new RuntimeException(e);
	    }

	}
	
	private ArrayList<WorldStage> mapLevels(Element element)
	{		
		Array<Element> stageElements = element.getChildrenByName("Stage");
		
		for(int i = 0; i < stageElements.size; i++)
		{
			WorldStage stage = new WorldStage();
			
			stage.setCompleted(stageElements.get(i).getInt("Completed"));
			stage.setStageNum(stageElements.get(i).getInt("Number"));
			stage.setLocked(stageElements.get(i).getBoolean("Locked"));
			stage.setImageName(stageElements.get(i).get("Image"));
			stage.setNumStars(stageElements.get(i).getInt("Stars"));
			stage.setScore(stageElements.get(i).getFloat("Score"));
			
			Array<Element> levelElements = stageElements.get(i).getChildrenByName("Level");
			
			for(int j = 0; j < levelElements.size; j++)
			{
				WorldLevel level = new WorldLevel();
				
				level.setTimeBonus(levelElements.get(j).getFloat("TimeBonus"));
				level.setLevelNum(levelElements.get(j).getInt("Number"));
				level.setTime(levelElements.get(j).getFloat("Time"));
				level.setLocked(levelElements.get(j).getBoolean("Locked"));
				level.setScore(levelElements.get(j).getFloat("Score"));
				level.setNumStars(levelElements.get(j).getInt("Stars"));
				level.setImageName(levelElements.get(j).get("Image"));
				level.setMaxScore(levelElements.get(j).getFloat("MaxScore"));
				
				Array<Element> itemElements = levelElements.get(j).getChildrenByName("Item");
				
				for(int k = 0; k < itemElements.size; k++)
				{
					WorldItem item = new WorldItem();
					
					item.setBonus(itemElements.get(k).getFloat("Bonus"));
					item.setName(itemElements.get(k).get("Type"));
					item.setPower(itemElements.get(k).getFloat("Power"));
					item.setRadius(itemElements.get(k).getFloat("Radius"));
					
					level.addItem(item);
				}
				
				stage.addLevel(level);
			}
			
			stages.add(stage);
		}
		
		return stages;
	}
	
	
	public int currentStage()
	{
		return currentStage;
	}

	public void setCurrentStage(int currentStage)
	{
		this.currentStage = currentStage;
	}

	public int currentLevel()
	{
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel)
	{
		this.currentLevel = currentLevel;
	}

	public ArrayList<WorldStage> stages()
	{
		return levelConfig;
	}

	public void setStages(ArrayList<WorldStage> stages) 
	{
		this.levelConfig = stages;
	}	
	
	public void incrementLevel()
	{
		if((currentLevel+1) > levelConfig.get(currentStage-1).levels().size())
		{
			currentLevel = 1;
			
			if((currentStage+1) > levelConfig.size()) currentStage = 1;
			else currentStage++;
		}
		else currentLevel++;
	}
	
	public void decrementLevel()
	{
		if((currentLevel-1) == 0)
		{
			if((currentStage-1) == 0)
			{
				currentStage = 1;
				currentLevel = 1;
			}
			else
			{
				currentStage--;
				currentLevel = levelConfig.get(currentStage-1).levels().size();
			}
		}
		else currentLevel--;
	}
	
	public WorldLevel level()
	{
		return levelConfig.get(currentStage-1).levels().get(currentLevel-1);
	}
	
	public WorldStage stage()
	{
		return levelConfig.get(currentStage-1);
	}
	
	private boolean checkFileExists(String filename)
	{
		FileHandle gameFile = Gdx.files.local(filename);
		
		return gameFile.file().isFile();
	}
}
