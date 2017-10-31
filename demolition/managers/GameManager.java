package com.omniworks.demolition.managers;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.CameraHelper;
import com.omniworks.demolition.CameraListener;
import com.omniworks.demolition.FontView;
import com.omniworks.demolition.GameTimer;
import com.omniworks.demolition.UIOverlay;
import com.omniworks.demolition.item.ItemView;
import com.omniworks.demolition.layers.HUDLayer;
import com.omniworks.demolition.layers.ItemLayer;
import com.omniworks.demolition.layers.Layer;
import com.omniworks.demolition.layers.UILayer;
import com.omniworks.demolition.level.LevelManager;
import com.omniworks.demolition.screens.GameScreen;
import com.omniworks.demolition.world.WorldDetector;
import com.omniworks.demolition.world.WorldModel;
import com.omniworks.demolition.world.WorldView;

public class GameManager
{
	public enum GameState
	{
		READY,
		RUNNING,
		PAUSED,
		LEVELEND,
		FINISHED,
		RESTART
	}
	
	private Game game;
	private WorldModel worldModel;
	private World world;
	private GameTimer gameTimer;
	private ElementManager elementManager;
	private ScoreManager scoreManager;
	private ItemManager itemManager;
	private LevelManager levelManager;
	private InputMultiplexer inputMultiplexer;
	private ParticleEffectManager particleManager;
	private SoundManager soundManager;
	private OrthographicCamera camera;
	private WorldView worldView;
	private FontView fontView;
	private ItemView itemView;
	private WorldDetector worldDetector;
	private CameraListener cameraListener;
	private HUDLayer hudLayer;
	private ItemLayer itemLayer;
	private UILayer uiLayer;
	
	private ArrayList<Layer> layers; 
	
	private GameState state;

	public GameManager(Game game)
	{
		this.game = game;
		
		initialize();
	}
	
	private void initialize()
	{
		levelManager = new LevelManager();
		scoreManager = new ScoreManager();
		itemManager = new ItemManager(this);
		particleManager = new ParticleEffectManager();
		elementManager = new ElementManager();
		soundManager = new SoundManager();
		worldModel = new WorldModel(this);
		worldView = new WorldView(this);
		itemView = new ItemView(this);
		fontView = new FontView(this);
		itemLayer = new ItemLayer(this);
		hudLayer = new HUDLayer(this);
		uiLayer = new UILayer(this);
		
		inputMultiplexer = new InputMultiplexer();
		setLayers(new ArrayList<Layer>());
		
		state = GameState.READY;
	}
	
	
	public void setLevel(int stage, int level)
	{
		levelManager.setCurrentStage(stage);
		levelManager.setCurrentLevel(level);
		createLevel();
	}
	
	public void incrementLevel()
	{
		levelManager.incrementLevel();
		createLevel();
	}
	
	public void decrementLevel()
	{
		levelManager.decrementLevel();
		createLevel();
	}
	
	public void createLevel()
	{
		resetNewLevel();
		soundManager.setSounds();
		state = GameState.READY;
		itemManager.createItemsList(levelManager.level());
		createWorld();
		gameTimer = new GameTimer(levelManager.level().time());
		createLayers();
		setLayers();
		setWorldDetector();
		setInputMultiplexer();
		game.setScreen(new GameScreen(this));
	}
	
	private void setInputMultiplexer()
	{
		inputMultiplexer.clear();
		
		for(Layer layer : layers)
		{
			inputMultiplexer.addProcessor(layer.stage());
		}
		
		if((state == GameState.READY) || (state == GameState.RUNNING) || (state == GameState.LEVELEND))
		{
			inputMultiplexer.addProcessor(worldDetector);
		}
		
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	private void setLayers()
	{
		layers.clear();
		
		if(state == GameState.READY)
		{
			layers.add(itemLayer);
			layers.add(hudLayer);
			layers.add(uiLayer);
		}
		else if(state == GameState.RUNNING)
		{
			layers.add(itemLayer);
			layers.add(hudLayer);
			layers.add(uiLayer);
		}
		else if(state == GameState.PAUSED)
		{
			layers.add(hudLayer);
			layers.add(uiLayer);

		}
		else if(state == GameState.LEVELEND)
		{
			layers.add(itemLayer);
			layers.add(hudLayer);
			layers.add(uiLayer);

		}
		else if(state == GameState.FINISHED)
		{
			layers.add(hudLayer);
			layers.add(uiLayer);
		}
		
		updateLayers();
	}
	
	private void createLayers()
	{
		itemLayer.createLayer();
		hudLayer.createLayer();
		uiLayer.createLayer();
	}

	private void updateLayers()
	{
		for(Layer layer : layers)
		{
			layer.updateLayer();
		}
	}
	
	private void createWorld()
	{
		setCamera(worldModel.generateWorld());
		world = worldModel.getWorld();
	}
	
	private void setCamera(Vector2 worldWH)
	{
		Vector2 initialView = CameraHelper.maxCameraViewport(worldWH, new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 64, 2);
		camera = new OrthographicCamera(initialView.x,initialView.y);
		camera.position.set(0,(initialView.y/2)-5,0);
		camera.update();
		
		setCameraListener(new CameraListener(camera, worldWH, 64));
	}
	
	private void setWorldDetector()
	{
		worldDetector = new WorldDetector(this);
	}
	
	public void resetNewLevel()
	{
		scoreManager.clear();
		itemManager.clear();
		elementManager.clear();
		particleManager.clear();
		soundManager.clear();
		worldModel.clear();
		inputMultiplexer.clear();
		uiLayer.clear();
		hudLayer.clear();
		itemLayer.clear();
		layers.clear();
		
		state = GameState.READY;

	}
	
	public void saveLevel()
	{
		levelManager.stage().levels().get(levelManager.currentLevel()).setLocked(false);

		if(scoreManager.totalScore() > levelManager.level().score())
		{
			levelManager.level().setScore(scoreManager.totalScore());
			levelManager.level().setNumStars((int)Math.ceil(3*(levelManager.level().score()/levelManager.level().maxScore())));
		}
		
		levelManager.writeLevels(levelManager.stages(), Assets.levelsOut);
	}
	
	public Game game() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public WorldModel worldModel() {
		return worldModel;
	}

	public void setWorldModel(WorldModel worldModel) {
		this.worldModel = worldModel;
	}

	public GameTimer gameTimer() {
		return gameTimer;
	}

	public void setGameTimer(GameTimer gameTimer) {
		this.gameTimer = gameTimer;
	}

	public ElementManager elementManager() {
		return elementManager;
	}

	public void setElementManager(ElementManager elementManager) {
		this.elementManager = elementManager;
	}

	public ScoreManager scoreManager() {
		return scoreManager;
	}

	public void setScoreManager(ScoreManager scoreManager) {
		this.scoreManager = scoreManager;
	}

	public ItemManager itemManager() {
		return itemManager;
	}

	public void setItemManager(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public LevelManager levelManager() {
		return levelManager;
	}

	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}

	public GameState state() {
		return state;
	}

	public void setState(GameState state)
	{
		this.state = state;
		
		setLayers();
		setInputMultiplexer();
	}

	public World world() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public InputMultiplexer inputMultiplexer() {
		return inputMultiplexer;
	}

	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		this.inputMultiplexer = inputMultiplexer;
	}

	public ParticleEffectManager particleManager() {
		return particleManager;
	}

	public void setParticleManager(ParticleEffectManager particleManager) {
		this.particleManager = particleManager;
	}

	public SoundManager soundManager() {
		return soundManager;
	}

	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	public OrthographicCamera camera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public FontView fontView() {
		return fontView;
	}

	public void setFontView(FontView fontView) {
		this.fontView = fontView;
	}

	public WorldView worldView() {
		return worldView;
	}

	public void setWorldView(WorldView worldView) {
		this.worldView = worldView;
	}

	public WorldDetector worldDetector() {
		return worldDetector;
	}

	public void setWorldDetector(WorldDetector worldDetector) {
		this.worldDetector = worldDetector;
	}

	public CameraListener cameraListener() {
		return cameraListener;
	}

	public void setCameraListener(CameraListener cameraListener) {
		this.cameraListener = cameraListener;
	}

	public ItemLayer itemLayer() {
		return itemLayer;
	}

	public void setItemLayer(ItemLayer itemLayer) {
		this.itemLayer = itemLayer;
	}

	public ArrayList<Layer> layers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}
	
	public void addLayer(Layer layer)
	{
		layers.add(layer);
	}

	public UILayer uiLayer() {
		return uiLayer;
	}

	public void setUiLayer(UILayer uiLayer) {
		this.uiLayer = uiLayer;
	}

	public ItemView itemView() {
		return itemView;
	}

	public void setItemView(ItemView itemView) {
		this.itemView = itemView;
	}
}
