
package com.omniworks.demolition;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.omniworks.demolition.utils.DistanceFieldShader;
import com.omniworks.demolition.utils.FontUtils;

public class Assets
{

	public static BitmapFont generalFont;
	public static BitmapFont generalFont64;
	public static BitmapFont testFont;
	public static BitmapFont distanceFont;
	
	public static BitmapFont timerFont;
	public static BitmapFont scoreFont;
	public static BitmapFont specialScoreFont;

	public static String levelsIn;
	public static String levelsOut;
	public static String particlePath;
	public static String cloudParticlePath;
	
	public static String berlinFontPath;
	public static String berlinImagePath;
	public static String timerFontPath;
	public static String timerImagePath;
	public static String general64FontPath;
	public static String general64ImagePath;
	public static String distanceFontPath;
	public static String distanceImagePath;
	
	public static String coinSoundPath;
	public static String hitSoundPath;
	public static String jumpSoundPath;
	public static String musicSoundPath;
	public static String woodParticlePath;
	public static String woodParticlePath2;
	public static String cloudOnePath;
	public static String cloudTwoPath;
	
	public static String brickBreakPath;
	public static String woodBreakPath;
	public static String woodImpactPath;
	public static String bombSoundPath;
	public static String idleTruckPath;
	public static String scoreSoundPath;
	public static String itemDropPath;

	public static String atlasPath;
	public static String buttonAtlasPath;
	
	public static Sound hitSound;
	public static Sound coinSound;
	public static Sound jumpSound;
	public static Sound bombSound;
	public static Sound woodImpactSound;
	public static Sound woodBreakSound;
	public static Sound brickBreakSound;
	public static Sound idleTruckSound;
	public static Sound scoreSound;
	public static Sound itemDropSound;
	
	public static Music music;

	public static int pixelsPerMeter;
	
	public static TextureAtlas atlas;
	public static TextureAtlas buttonAtlas;
	
	public static DistanceFieldShader shader;
	
	public static float CAP_HEIGHT;
	public static float CAP_OFFSET;
	public static float PIXELS_SCALE;
	
	public static void setAssetPaths()
	{
		levelsIn = "game_in.xml";
		levelsOut = levelsIn;
		particlePath = "data/particles/wood_break.p";
		cloudParticlePath = "data/particles/clouds.p";
		
		distanceFontPath = "data/fonts/berlin_df.fnt";
		distanceImagePath = "data/fonts/berlin_df.png";
		
		berlinFontPath = "data/fonts/berlin_font.fnt";
		berlinImagePath = "data/fonts/berlin_font.png";

		timerFontPath = "data/fonts/timer_128.fnt";
		timerImagePath = "data/fonts/timer_128.png";
		
		general64FontPath = "data/fonts/timer_64.fnt";
		general64ImagePath = "data/fonts/timer_64.png";
		
		atlasPath = "data/game.atlas";
		buttonAtlasPath = "data/buttons.atlas";
		
		coinSoundPath = "data/sounds/coin.wav";
		hitSoundPath = "data/sounds/hit.wav";
		jumpSoundPath = "data/sounds/jump.wav";
		musicSoundPath = "data/sounds/music.mp3";
		woodParticlePath = "data/wood_chunk.png";
		woodParticlePath2 = "data/wood_chunk2.png";
		cloudOnePath = "data/cloud2.png";
		cloudTwoPath = "data/cloud3.png";
		
		brickBreakPath = "data/sounds/brick_break_v1.wav";
		woodBreakPath = "data/sounds/wood_break_v2.wav";
		woodImpactPath = "data/sounds/wood_impact_v1.wav";
		bombSoundPath = "data/sounds/bomb_v3.wav";
		idleTruckPath = "data/sounds/idle_truck_v2.wav";
		scoreSoundPath = "data/sounds/cha_ching_v1.wav";
		itemDropPath = "data/sounds/item_drop.wav";
	}
	
	public static void load(AssetManager manager) 
	{
		pixelsPerMeter = 32;
		
		atlas = manager.get(Assets.atlasPath, TextureAtlas.class);
		buttonAtlas = manager.get(Assets.buttonAtlasPath, TextureAtlas.class);
		hitSound = manager.get(Assets.hitSoundPath, Sound.class);
		coinSound = manager.get(Assets.coinSoundPath, Sound.class);
		jumpSound = manager.get(Assets.jumpSoundPath, Sound.class);
		
		brickBreakSound = manager.get(Assets.brickBreakPath, Sound.class);
		woodBreakSound = manager.get(Assets.woodBreakPath, Sound.class);
		woodImpactSound = manager.get(Assets.woodImpactPath, Sound.class);
		bombSound = manager.get(Assets.bombSoundPath, Sound.class);
		idleTruckSound = manager.get(Assets.idleTruckPath, Sound.class);
		scoreSound = manager.get(Assets.scoreSoundPath, Sound.class);
		itemDropSound = manager.get(Assets.itemDropPath, Sound.class);
		
		music = manager.get(Assets.musicSoundPath, Music.class);
		music.setLooping(true);
		music.setVolume(0.25f);
		
		distanceFont = manager.get(Assets.distanceFontPath);
		distanceFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		CAP_HEIGHT = distanceFont.getCapHeight();
		CAP_OFFSET = 12;
		
		testFont = manager.get(Assets.timerFontPath);
		testFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		generalFont = manager.get(Assets.berlinFontPath);
		generalFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		generalFont64 = manager.get(Assets.general64FontPath);
		generalFont64.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		timerFont = manager.get(Assets.timerFontPath);
		timerFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		scoreFont = manager.get(Assets.berlinFontPath);
		scoreFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		specialScoreFont = manager.get(Assets.berlinFontPath);
		
		shader = new DistanceFieldShader();
		
		PIXELS_SCALE = FontUtils.fontScaleByPixels();

	}

	public static void playSound (Sound sound) 
	{
		if (Settings.soundEnabled) sound.play(1);
	}
}
