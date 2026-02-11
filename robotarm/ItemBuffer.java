package robotarm;

import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;


public class ItemBuffer {
	//node1 is where items are generated and node2 is where they are transferred
	Node node1 = new Node();
	Node node2 = new Node();
	Material mat = Main.mat.clone();
	ArrayList<Item> items = new ArrayList<>(100);
	ArrayList<Item> items2 = new ArrayList<>(100);
	ArrayList<Vector3f> slots = new ArrayList<>(100);
	float itemRadius = 1f;
	float itemHeight = 1f;
	Vector3f node1location;
	Vector3f node2location;
	float itemsX = 8;
	float itemsZ = 4;
	
	
	public ItemBuffer(AssetManager assetManager, Node rootNode, Vector3f coords, int itemAmount) {
		mat.setColor("Diffuse", ColorRGBA.Orange);
		
		//take items from this box
		Vector3f box1size = new Vector3f(Station.size.x, Station.size.y, Station.size.z / 2); // 20,2,10
		Box box1 = new Box(box1size.x, box1size.y, box1size.z); 
		Geometry geom1 = new Geometry("Box", box1);
		//transfer to items to this box
		Vector3f box2size = new Vector3f(Station.size.x / 2, Station.size.y, Station.size.z); //10,2,20
		Box box2 = new Box(box2size.x, box2size.y, box2size.z);
		Geometry geom2 = new Geometry("Box", box2);
        
		//set materials, attach childs and move nodes
        geom1.setMaterial(mat);
        geom2.setMaterial(mat);
        node1.attachChild(geom1);
        node2.attachChild(geom2);
        node1location = new Vector3f(coords.add(0f, 0f, -Station.size.z * 1.5f));
        node2location = new Vector3f(coords.add(Station.size.x * 1.5f, 0f, 0f));
        node1.setLocalTranslation(node1location);
        node2.setLocalTranslation(node2location);
        rootNode.attachChild(node1);
        rootNode.attachChild(node2);
        
        //make slots for item generation, generate the items and put them in a dynamic array
        float stepX = Station.size.x * (2 / itemsX);
        float stepZ = Station.size.z / 2 * (2 / itemsZ);
        float startX = -Station.size.x + stepX / 2;
        float startZ = -Station.size.z / 2 + stepZ / 2;
        
        for (int i = 0; i < itemsX; i++) {
        	float relativeX = startX + stepX * i;
        	for (int j = 0; j < itemsZ; j++) {
        		float relativeZ = startZ + stepZ * j;
        		Item item = new Item(assetManager, node1, itemRadius, itemHeight, new Vector3f(relativeX, box1size.y + itemHeight / 2, relativeZ), true);
        		items.add(item);
        	}
        }
        
        //add coords for item placement
        for (int i = 0; i < itemsX; i++) {
        	float relativeX = startX + stepX * i;
        	for (int j = 0; j < itemsZ; j++) {
        		float relativeZ = startZ + stepZ * j;
        		Vector3f itemCoords = node2location.add(relativeZ, box2size.y + itemHeight / 2, relativeX); 
        		slots.add(itemCoords);
        		Item item = new Item(assetManager, node2, itemRadius, itemHeight, new Vector3f(relativeZ, box1size.y+itemHeight / 2, relativeX), false);
        		items2.add(item);
        	}
        }
		
	}
}
