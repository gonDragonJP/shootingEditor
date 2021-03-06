package shootingEditor.treeView.enemy;

import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.TreeContent;

public class TreeEnemyGroup extends TreeContent{

	public enum GroupType{
		
		MOVING         ("Moving",        ContentCategory.MUTABLE_GROUP),
		GENERATOR      ("Generator",     ContentCategory.MUTABLE_GROUP),
		COLLISION      ("Collision",     ContentCategory.MUTABLE_GROUP),
		ANIMATION      ("Animation",     ContentCategory.FIXED_GROUP),
		NORMAL_ANIME   ("NormalAnime",   ContentCategory.FIXED_GROUP),
		EXPLOSION_ANIME("ExplosionAnime",ContentCategory.FIXED_GROUP),
		NODE_ANIME     ("NodeAnime",     ContentCategory.MUTABLE_GROUP),
		MOVING_CHILD   ("Node",ContentCategory.CHILD_GROUP),
		GENERATOR_CHILD("Node",ContentCategory.CHILD_GROUP),
		COLLISION_CHILD("Node",ContentCategory.CHILD_GROUP),
		NODEANIME_CHILD("Node",ContentCategory.CHILD_GROUP);
		
		public String name;
		public ContentCategory contentCategory;
		
		GroupType(String argName, ContentCategory argType){
			
			name = argName;
			contentCategory = argType;
		}
	}
	
	public EnemyData enemyData;
	public GroupType groupType;
	
	public int childIndex;
	public int keyNode;
	
	public TreeEnemyGroup(EnemyData enemyData, GroupType groupType) {
		
		super(groupType.name, groupType.contentCategory);
		
		this.enemyData = enemyData;
		this.groupType = groupType;
		this.childIndex = -1;
		this.keyNode = -1;
	}
	
	public TreeEnemyGroup(EnemyData enemyData, GroupType groupType,int childIndex){
		//childGroupで使われるコンストラクタ(NodeAnime以外)
		
		this(enemyData, groupType);
		
		this.childIndex = childIndex;
	}
	
	public TreeEnemyGroup(EnemyData enemyData, GroupType groupType,int childIndex, int keyNode){
		//NodeAnimeで使われるコンストラクタ
		
		this(enemyData, groupType, childIndex);
	
		this.keyNode = keyNode;
	}
	
	public void setKeyNode(int keyNode) {
		//NodeAnimeのkey変更時に呼び出されます
		
		this.keyNode = keyNode;
	}
	
	@Override
	public String toString(){
		
		String index = childIndex >=0 ?
				"["+ String.valueOf(childIndex) + "]" : "";
		
		String key = keyNode >=0 ?
				"[key:"+ String.valueOf(keyNode) + "]" : "";
		
		return this.name + index + key;
	}
}
