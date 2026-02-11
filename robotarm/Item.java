package robotarm;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;

public class Item {
	Material itemMat = Main.mat.clone();
	Vector3f coords;
	Node itemNode = new Node();
	
	public Item(AssetManager assetManager, Node rootNode, float radius, float itemHeight, Vector3f coords, Boolean createItem) {
		itemMat.setColor("Diffuse", ColorRGBA.Green);
		this.coords = coords;
		
		if (createItem) {
			Cylinder item = new Cylinder(2, 32, radius, itemHeight, true);
			Geometry itemGeom = new Geometry("Cylinder", item);
			itemGeom.rotate(FastMath.HALF_PI,0,0);
			itemGeom.setMaterial(itemMat);
			itemNode.attachChild(itemGeom);
			rootNode.attachChild(itemNode);
			itemNode.setLocalTranslation(coords);
		}
	}
}
