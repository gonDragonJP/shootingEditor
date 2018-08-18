package shootingEditor;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import shootingEditor.enemy.EnemyData;
import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;
import shootingEditor.treeView.TreeContent;


public class MainApp extends Application implements CallbackOfMainApp{
	
	private static final int WinX = 1280;
	private static final int WinY = 640;
	
	public static final int CanvasX = 320;
	public static final int CanvasY = 500;
	
	public Canvas canvas = new Canvas(CanvasX, CanvasY);
	public CheckBox checkEnableTex = new CheckBox("Enable Texture");
	public Slider slider = new Slider(0,0,0);
	public TextField scrollTextField = new TextField();
	public Button resetButton = new Button();
	public Button startButton = new Button();
	public Button stopButton = new Button();
	public Button testEnemyButton = new Button();
	public Button storeTreeDataButton = new Button();
	public TableView<EventData> eventTable = new TableView<>();
	public TableView<EnemyData> enemyTable = new TableView<>();
	public TreeView<TreeContent> treeView = new TreeView<>();
	public Tab tabEvent = new Tab();
	public Tab tabEnemy = new Tab();
	public TabPane tabPane = new TabPane();
	public Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
	
	private DrawModule drawModule;
	private TableModule tableModule;
	private TreeModule treeModule;
	private GameTestModule gameTestModule;
	
	public static void main(String[] args){
		
		Application.launch(args);
	}
	
	//static GeneratingTest test = new GeneratingTest();

	@Override
	public void start(Stage stage) throws Exception {
		
		drawModule = new DrawModule(this);
		tableModule = new TableModule(this);
		treeModule = new TreeModule(this);
		gameTestModule = new GameTestModule(this);
		
		initStage(stage);

		gameTestModule.setGameStage(1);
		
		int scrollMax = StageData.stageEndPoint;
		slider.setMax(scrollMax);
	}
	
	private void initStage(Stage stage){
		
		SetScene.exec(this, stage);
	
		stage.setTitle("ShootingEditor ver1.0");
		stage.setWidth(WinX);
		stage.setHeight(WinY);
		stage.setOnCloseRequest(event->{gameTestModule.cancelTimer();});
		stage.show();
		
		drawModule.drawScreen();
	}

	synchronized public void updateSlider(){
		
		drawModule.drawScreen();
		gameTestModule.refreshQueueOfEvent();
	}

	@Override
	public DrawModule getdrawModule() {
		
		return drawModule;
	}

	@Override
	public TableModule getTableModule() {
		
		return tableModule;
	}
	
	@Override
	public TreeModule getTreeModule() {
		
		return treeModule;
	}

	@Override
	public GameTestModule getgameTestModule() {
		
		return gameTestModule;
	}
}