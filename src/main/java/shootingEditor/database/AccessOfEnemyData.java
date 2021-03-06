package shootingEditor.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.CollisionRegion.CollisionShape;
import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.enemy.GeneratingChild;
import shootingEditor.enemy.MovingNode;
import shootingEditor.animation.AnimationData;
import shootingEditor.animation.AnimationData.RepeatAttribute;
import shootingEditor.animation.AnimationData.RotateAttribute;
import shootingEditor.animation.AnimationSet.AnimeKind;

public class AccessOfEnemyData {
	
	private static String databasePath = AbsoluteDirectryHolder.getStageManagerDBPath();
	
	public static void setEnemyList(ArrayList<EnemyData> enemyList, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		String sql;
		ResultSet resultSet;
		ArrayList<Integer> stackList = new ArrayList<>(); 
		// ResultSetが単一オブジェクトの為、ネストでクエリ呼び出しすると正常に作動しない
		// ネストを避ける為、結果の一時退避用に使用しています
		
		sql = "select objectID from BasicData_Stage_"
				+String.valueOf(stage)+" ;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		
		try {
			while(resultSet.next()){
				
				int objectID = resultSet.getInt("objectID");
				stackList.add(objectID);
			}
			
			for(int e: stackList){
				
				enemyList.add(generateEnemyData(e, stage));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private static EnemyData generateEnemyData(int objectID, int stage){
		
		EnemyData enemyData = new EnemyData();
		
		enemyData.objectID = objectID;
		setEnemyData(enemyData, stage);
		
		return enemyData;
	}
	
	private static void setEnemyData(EnemyData enemyData, int stage){
		
		String sql;
		ResultSet resultSet;
		String objectID =String.valueOf(enemyData.objectID);
		
		 sql = "select * from BasicData_Stage_"
				 +String.valueOf(stage)+" where objectID="+objectID+";";
		 resultSet = SQLiteManager.getResultSet(sql);
		
		setBasicData(enemyData, resultSet);
		
		sql = "select * from MovingNode_Stage_"
				+String.valueOf(stage)+" where parentID="+objectID+";";
		 resultSet = SQLiteManager.getResultSet(sql);
		 
		setMovingNode(enemyData, resultSet);
		
		sql = "select * from GeneratorNode_Stage_"
				+String.valueOf(stage)+" where parentID="+objectID+";";
		 resultSet = SQLiteManager.getResultSet(sql);
		 
		setGeneratorNode(enemyData, resultSet);
		
		sql = "select * from CollisionNode_Stage_"
				+String.valueOf(stage)+" where parentID="+objectID+";";
		 resultSet = SQLiteManager.getResultSet(sql);
		 
		setCollisionNode(enemyData, resultSet);
		
		sql = "select * from AnimationData_Stage_"
				+String.valueOf(stage)+" where parentID="+objectID+";";
		 resultSet = SQLiteManager.getResultSet(sql);
		 
		setAnimationData(enemyData, resultSet);
	}
	
	private static void setBasicData(EnemyData enemyData, ResultSet resultSet){
		
		try {
			enemyData.name = resultSet.getString("name");
			enemyData.isDerivativeType = resultSet.getBoolean("isDerivativeType");
			enemyData.explosiveObjectID = resultSet.getInt("explosiveObjectID");
			enemyData.hitPoints = resultSet.getInt("hitPoint");
			enemyData.atackPoints = resultSet.getInt("atackPoint");
			enemyData.startPosition.x = resultSet.getInt("startPosition_X");
			enemyData.startPosition.y = resultSet.getInt("startPosition_Y");
			enemyData.startPosAttrib.x = resultSet.getInt("startPosAttrib_X");
			enemyData.startPosAttrib.y = resultSet.getInt("startPosAttrib_Y");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private static void setMovingNode(EnemyData enemyData, ResultSet resultSet){
		
		try {
			
			while(resultSet.next()){
				
				MovingNode node = new MovingNode();
				enemyData.node.add(resultSet.getInt("nodeIndex"), node);
			
				node.startVelocity.x = resultSet.getDouble("startVelocity_X");
				node.startVelocity.y = resultSet.getDouble("startVelocity_Y");
				node.startAcceleration.x = resultSet.getDouble("startAcceleration_X");
				node.startAcceleration.y = resultSet.getDouble("startAcceleration_Y");
				node.homingAcceleration.x = resultSet.getDouble("homingAcceleration_X");
				node.homingAcceleration.y = resultSet.getDouble("homingAcceleration_Y");
				node.nodeDurationFrame = resultSet.getInt("nodeDurationFrame");
				node.startVelAttrib.x = resultSet.getInt("startVelAttrib_X");
				node.startVelAttrib.y = resultSet.getInt("startVelAttrib_Y");
				node.startAccAttrib.x = resultSet.getInt("startAccAttrib_X");
				node.startAccAttrib.y = resultSet.getInt("startAccAttrib_Y");	
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private static void setGeneratorNode(EnemyData enemyData, ResultSet resultSet){
		
		try {
			
			while(resultSet.next()){
				
				GeneratingChild node = new GeneratingChild();
				enemyData.generator.add(resultSet.getInt("nodeIndex"), node);
			
				node.objectID = resultSet.getInt("objectID");
				node.repeat = resultSet.getInt("repeat");
				node.startFrame = resultSet.getInt("startFrame");
				node.intervalFrame = resultSet.getInt("intervalFrame");
				node.centerX = resultSet.getInt("centerX");
				node.centerY = resultSet.getInt("centerY");	
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private static void setCollisionNode(EnemyData enemyData, ResultSet resultSet){
		
		try {
			
			while(resultSet.next()){
				
				CollisionRegion node = new CollisionRegion();
				enemyData.collision.add(resultSet.getInt("nodeIndex"), node);
			
				node.centerX = resultSet.getInt("centerX");
				node.centerY = resultSet.getInt("centerY");
				node.size = resultSet.getInt("size");
				int shapeID = resultSet.getInt("collisionShape");
				node.collisionShape = CollisionShape.getFromID(shapeID);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private static void setAnimationData(EnemyData enemyData, ResultSet resultSet){
		
		try {
			
			while(resultSet.next()){
				
				AnimationData data = new AnimationData();
				AnimeKind animeKind = AnimeKind.getFromID(resultSet.getInt("AnimationKind"));
				int keyNode = resultSet.getInt("keyNode");
				
				switch(animeKind){
				
				case NORMAL:
					enemyData.animationSet.normalAnime = data;
					break;
				case EXPLOSION:
					enemyData.animationSet.explosionAnime = data;
					break;
				case NODEACTION:
					enemyData.animationSet.nodeActionAnime.put(keyNode, data);
				default:
				}
				
				data.textureID = resultSet.getInt("textureID");
				data.drawSize.x = resultSet.getDouble("drawSize_X");
				data.drawSize.y = resultSet.getDouble("drawSize_Y");
				data.repeatAttribute = RepeatAttribute.getFromID(resultSet.getInt("RepeatAttribute"));
				data.frameOffset = resultSet.getInt("frameOffset");
				data.frameNumber= resultSet.getInt("frameNumber");
				data.frameInterval= resultSet.getInt("frameInterval");
				data.rotateAction = RotateAttribute.getFromID(resultSet.getInt("RotateAttribute"));
				data.rotateOffset= resultSet.getInt("rotateOffset");
				data.angularVelocity= resultSet.getDouble("angularVelocity");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void addEnemyList(ArrayList<EnemyData> enemyList, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		for(EnemyData e: enemyList){
			
			add(e, stage);
		}
		
		SQLiteManager.closeDatabase();
	}
	
	public static void addEnemyData(EnemyData enemyData, int stage){
		
		SQLiteManager.initDatabase(databasePath);
			
		add(enemyData, stage);
		
		SQLiteManager.closeDatabase();
	}
	
	private static void add(EnemyData enemyData, int stage){
		
		int  parentID = enemyData.objectID;
		int  nodeIndex;
		
		addBasicData(enemyData, stage);
			
		for(MovingNode e: enemyData.node){
				
			nodeIndex = enemyData.node.indexOf(e);
				
			addMovingNode(parentID, nodeIndex, e, stage);
		}
			
		for(GeneratingChild e: enemyData.generator){
				
			nodeIndex = enemyData.generator.indexOf(e);
				
			addGeneratorNode(parentID, nodeIndex, e, stage);
		}
			
		for(CollisionRegion e: enemyData.collision){
				
			nodeIndex = enemyData.collision.indexOf(e);
				
			addCollisionNode(parentID, nodeIndex, e, stage);
		}
		
		int keyNode =-1;
		
		addAnimationData
		(parentID, keyNode, AnimeKind.NORMAL, enemyData.animationSet.normalAnime, stage);
		addAnimationData
		(parentID, keyNode, AnimeKind.EXPLOSION, enemyData.animationSet.explosionAnime, stage);
		
		Set<Integer> keySet = enemyData.animationSet.nodeActionAnime.keySet();
		
		for(int e: keySet){
			
			keyNode = e;
			AnimationData animeData = enemyData.animationSet.nodeActionAnime.get(e);
			
			addAnimationData
			(parentID, keyNode, AnimeKind.NODEACTION, animeData, stage);
		}
	}

	private static void addBasicData(EnemyData enemyData, int stage){
		
		String sql = "insert into BasicData_Stage_"
				+String.valueOf(stage)+" values(";
		
		sql += String.valueOf(enemyData.objectID) +",";
		sql += "'"+ enemyData.name +"',";
		sql += (enemyData.isDerivativeType ? "1" : "0") +",";
		sql += String.valueOf(enemyData.explosiveObjectID) +",";
		sql += String.valueOf(enemyData.hitPoints) +",";
		sql += String.valueOf(enemyData.atackPoints) +",";
		sql += String.valueOf(enemyData.startPosition.x) +",";
		sql += String.valueOf(enemyData.startPosition.y) +",";
		sql += String.valueOf(enemyData.startPosAttrib.x) +",";
		sql += String.valueOf(enemyData.startPosAttrib.y) +",";
		sql += String.valueOf(enemyData.node.size()) +",";
		sql += String.valueOf(enemyData.generator.size()) +",";
		sql += String.valueOf(enemyData.collision.size());
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	private static void addMovingNode
	(int parentID, int nodeIndex, MovingNode node, int stage){
		
		String sql = "insert into MovingNode_Stage_"
				+String.valueOf(stage)+" values(";
		
		sql += String.valueOf(parentID) +",";
		sql += String.valueOf(nodeIndex) +",";
		sql += String.valueOf(node.startVelocity.x) +",";
		sql += String.valueOf(node.startVelocity.y) +",";
		sql += String.valueOf(node.startAcceleration.x) +",";
		sql += String.valueOf(node.startAcceleration.y) +",";
		sql += String.valueOf(node.homingAcceleration.x) +",";
		sql += String.valueOf(node.homingAcceleration.y) +",";
		sql += String.valueOf(node.nodeDurationFrame) +",";
		sql += String.valueOf(node.startVelAttrib.x) +",";
		sql += String.valueOf(node.startVelAttrib.y) +",";
		sql += String.valueOf(node.startAccAttrib.x) +",";
		sql += String.valueOf(node.startAccAttrib.y);
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	private static void addGeneratorNode
	(int parentID, int nodeIndex, GeneratingChild node, int stage){
		
		String sql = "insert into GeneratorNode_Stage_"
				+String.valueOf(stage)+" values(";
		
		sql += String.valueOf(parentID) +",";
		sql += String.valueOf(nodeIndex) +",";
		sql += String.valueOf(node.objectID) +",";
		sql += String.valueOf(node.repeat) +",";
		sql += String.valueOf(node.startFrame) +",";
		sql += String.valueOf(node.intervalFrame) +",";
		sql += String.valueOf(node.centerX) +",";
		sql += String.valueOf(node.centerY);
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	private static void addCollisionNode
	(int parentID, int nodeIndex, CollisionRegion node, int stage){
		
		String sql = "insert into CollisionNode_Stage_"
				+String.valueOf(stage)+" values(";
		
		sql += String.valueOf(parentID) +",";
		sql += String.valueOf(nodeIndex) +",";
		sql += String.valueOf(node.centerX) +",";
		sql += String.valueOf(node.centerY) +",";
		sql += String.valueOf(node.size) +",";
		sql += String.valueOf(node.collisionShape.getID());
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}

	private static void addAnimationData
	(int parentID, int keyNode, AnimeKind animeKind, AnimationData animeData, int stage){
		
		String sql = "insert into AnimationData_Stage_"
				+String.valueOf(stage)+" values(";
		
		sql += String.valueOf(parentID) +",";
		sql += String.valueOf(animeKind.getID()) +",";
		sql += String.valueOf(keyNode) +",";
		sql += String.valueOf(animeData.textureID) +",";
		sql += String.valueOf(animeData.drawSize.x) +",";
		sql += String.valueOf(animeData.drawSize.y) +",";
		sql += String.valueOf(animeData.repeatAttribute.getID()) +",";
		sql += String.valueOf(animeData.frameOffset) +",";
		sql += String.valueOf(animeData.frameNumber) +",";
		sql += String.valueOf(animeData.frameInterval) +",";
		sql += String.valueOf(animeData.rotateAction.getID()) +",";
		sql += String.valueOf(animeData.rotateOffset) +",";
		sql += String.valueOf(animeData.angularVelocity);
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	public static int addNewEnemyData(EnemyCategory category, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		EnemyData newData = generateNewEnemyData();
		newData.objectID = category.getID() * 1000;
		
		changeToLatestID(newData);
		add(newData, stage);
		
		SQLiteManager.closeDatabase();
		
		return newData.objectID;
	}
	
	private static EnemyData generateNewEnemyData(){
		
		EnemyData enemyData = new EnemyData();
		enemyData.initialize();
		
		return enemyData;
	}
	
	public static int addCopyEnemyData(EnemyData enemyData, int stage){
	
		SQLiteManager.initDatabase(databasePath);
		
		EnemyData copyData = generateCopyEnemyData(enemyData);
		
		changeToLatestID(copyData);
		add(copyData, stage);
		
		SQLiteManager.closeDatabase();
		
		return copyData.objectID;
	}
	
	private static EnemyData generateCopyEnemyData(EnemyData srcEnemyData){
		
		EnemyData enemyData = new EnemyData();
		enemyData.copy(srcEnemyData);
		
		return enemyData;
	}
	
	private static void changeToLatestID(EnemyData enemyData){
	
		int latestID = StageData.getLastIDinEnemyList(enemyData.getCategory())+1;	
		enemyData.objectID = latestID;
	}
	
	public static void deleteEnemyData(EnemyData enemyData, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		String sql = "delete from BasicData_Stage_"
				+String.valueOf(stage)+" where objectID=";
		sql += String.valueOf(enemyData.objectID);
		sql += ";";
		
		System.out.println(sql);
		SQLiteManager.update(sql);
		
		deleteChildData(enemyData, stage);
		
		SQLiteManager.closeDatabase();
	}
	
	private static void deleteChildData(EnemyData enemyData, int stage){
		
		String sql;
		String[] childTable 
			={"MovingNode","GeneratorNode","CollisionNode","AnimationData"};
		
		for(String e: childTable ){
		
			sql = "delete from " + e +"_Stage_"
					+String.valueOf(stage)+" where parentID=";
			sql += String.valueOf(enemyData.objectID);
			sql += ";";
		
			System.out.println(sql);
			SQLiteManager.update(sql);
		}
	}
	
	public static boolean checkExistSameObjectID(int objectID, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		String sql;
		ResultSet resultSet;
		boolean result = false;
		
		sql = "select * from BasicData_Stage_"
				+String.valueOf(stage)+" where objectID="+objectID+";";
		resultSet = SQLiteManager.getResultSet(sql);
		
		try {
			result = resultSet.next();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		SQLiteManager.closeDatabase();

		return result;
	}
}
