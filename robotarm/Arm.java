package robotarm;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

public class Arm {
	/* The arm consists of four joints, two arms and a tooltip. 
	 * The first joint moves the whole arm horizontally
	 * The second joint moves the arms, third joint and the tooltip in horizontal and vertical directions
	 * The third joint moves the last arm, tooltipjoint and tooltipnode.
	 * 
	*/
	//Cylinder(int axisSamples, int radialSamples, float radius, float height, boolean closed)
	Node nodeJoint1 = new Node(); //the whole arm is to be added to this node
	Node nodeJoint2 = new Node();
	Node nodeJoint3 = new Node();
	Node nodeJoint4 = new Node();
	
	//joint sizes
	float jointRadius = 1f;
	float joint1height = 8f;
	float armRadius = jointRadius;
	float arm1height = 24f;
	float arm2height = 22f;
	float toolRadius = jointRadius / 2;
	float toolHeight = 2f;
	
	Vector3f nodeJoint1coord = Main.stationCoords.add(0f, Station.size.y + joint1height / 2, 0f);
	Vector3f nodeJoint2coord;
	Material mat = Main.mat.clone();
	
	public Arm(AssetManager assetManager, Node rootNode) {
		mat.setColor("Diffuse", ColorRGBA.Red);
		
		
		
        //first joint
		Cylinder joint1 = new Cylinder(2, 32, jointRadius, joint1height, true);
		Geometry joint1geom = new Geometry("Cylinder", joint1);
		joint1geom.setMaterial(mat);
	    joint1geom.rotate(FastMath.HALF_PI,0,0);
		nodeJoint1.attachChild(joint1geom);
		
		//second joint
		Sphere joint2 = new Sphere(32, 32, jointRadius);
		Geometry joint2geom = new Geometry("Sphere", joint2);
		joint2geom.setMaterial(mat);
		nodeJoint2.attachChild(joint2geom);
		
		//third joint
		Sphere joint3 = new Sphere(32, 32, jointRadius);
		Geometry joint3geom = new Geometry("Sphere", joint3);
		joint3geom.setMaterial(mat);
		nodeJoint3.attachChild(joint3geom);
		
		//fourth joint
		Sphere joint4 = new Sphere(32, 32, jointRadius);
		Geometry joint4geom = new Geometry("Sphere", joint4);
		joint4geom.setMaterial(mat);
		nodeJoint4.attachChild(joint4geom);
		
		//first arm
		Cylinder arm1 = new Cylinder(2, 32, armRadius, arm1height, true);
		Geometry arm1geom = new Geometry("Cylinder", arm1);
		arm1geom.setLocalTranslation(arm1height / 2, 0f, 0f);
		arm1geom.rotate(FastMath.HALF_PI, 0f, FastMath.HALF_PI);
		arm1geom.setMaterial(mat);
		nodeJoint2.attachChild(arm1geom);
		
		//second arm
		Cylinder arm2 = new Cylinder(2, 32, armRadius, arm2height, true);
		Geometry arm2geom = new Geometry("Cylinder", arm2);
		arm2geom.setLocalTranslation(arm2height/2, 0f, 0f);
		arm2geom.rotate(FastMath.HALF_PI, 0f, FastMath.HALF_PI);
		arm2geom.setMaterial(mat);
		nodeJoint3.attachChild(arm2geom);
		
		//tooltip
		Cylinder tool = new Cylinder(2, 32, toolRadius, toolHeight, true);
		Geometry toolGeom = new Geometry("Cylinder", tool);
		toolGeom.setLocalTranslation(0f, -toolHeight / 2, 0f);
		toolGeom.rotate(FastMath.HALF_PI, 0f, 0f);
		toolGeom.setMaterial(mat);
		nodeJoint4.attachChild(toolGeom);
		
		//set nodes to desired coords and attach them correctly to each other
		nodeJoint1.setLocalTranslation(nodeJoint1coord);
		nodeJoint2.setLocalTranslation(0f, joint1height / 2, 0f);
		nodeJoint3.setLocalTranslation(arm1height, 0f, 0f);
		nodeJoint4.setLocalTranslation(arm2height, 0f, 0f);
		nodeJoint3.attachChild(nodeJoint4);
		nodeJoint2.attachChild(nodeJoint3);
		nodeJoint1.attachChild(nodeJoint2);
		rootNode.attachChild(nodeJoint1);

	}
}
