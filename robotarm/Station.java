package robotarm;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class Station {
    Node node = new Node();
    public static Vector3f size = new Vector3f(20f, 2f, 20f);
	Material mat = Main.mat.clone();
    
    public Station(AssetManager assetManager, Node rootNode, Vector3f coords) {
    	
        Box box = new Box(size.x, size.y, size.z); 
        Geometry geom = new Geometry("Box", box);
        
        geom.setMaterial(mat);
        node.setLocalTranslation(coords);
        node.attachChild(geom);
        rootNode.attachChild(node); 
    }
}