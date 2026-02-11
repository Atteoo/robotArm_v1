package robotarm;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Main extends SimpleApplication {
    Station station;
    Arm arm;
    ItemBuffer buffer;
    Kinematics kinematics;
    public static Material mat;
    public static Vector3f stationCoords = new Vector3f(5f, -10f, -20f);
    
    //initialize movement
    boolean initMoveToItem = true; boolean initMoveToStorage = false;
    //temps for movement loops
    int initialTemp = 0; int baseTemp = 0; int shoulderTemp = 0; int elbowTemp = 0;
    //status of rotations
    boolean initialRotated = false; boolean baseRotated = false;
    boolean shoulderRotated = false; boolean elbowRotated = false;
    
    Vector3f targetItemPos; Vector3f targetSlotPos;
    Vector3f anglesItem  = new Vector3f(0f, 0f, 0f); Vector3f anglesSlot = new Vector3f(0f, 0f, 0f);
    int itemNumber = 0; int slotNumber = 0;
    boolean getNewItem = true; boolean getNewSlot = false;
    boolean shoulderFirst;
    int speed = 50;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //define material 
        mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.DarkGray); //color is DarkGray unless changed
    	
    	flyCam.setMoveSpeed(15);
    	
    	DirectionalLight sun1 = new DirectionalLight();
    	sun1.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal());
    	sun1.setColor(ColorRGBA.White);
    	rootNode.addLight(sun1);

        station = new Station(assetManager, rootNode, stationCoords);
        arm = new Arm(assetManager, rootNode);
        buffer = new ItemBuffer(assetManager, rootNode, stationCoords, 10);
        kinematics = new Kinematics();
        arm.nodeJoint2coord = Main.stationCoords.add(0f, Station.size.y + arm.joint1height, 0f);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        if (buffer.items.isEmpty()) return;

        if (getNewItem && itemNumber < buffer.items.size()) {
            targetItemPos = buffer.items.get(itemNumber).coords.add(buffer.node1location).add(0f, arm.toolHeight + buffer.itemHeight / 2, 0f);
            anglesItem = kinematics.solveIK(targetItemPos, arm.nodeJoint2coord, arm.arm1height, arm.arm2height);
            itemNumber++;
            getNewItem = false;
        } 

        if (getNewSlot && slotNumber < buffer.slots.size()) {
            targetSlotPos = buffer.items2.get(slotNumber).coords.add(buffer.node2location).add(0f, arm.toolHeight + buffer.itemHeight / 2, 0f);
            anglesSlot = kinematics.solveIK(targetSlotPos, arm.nodeJoint2coord, arm.arm1height, arm.arm2height);
            slotNumber++;
            getNewSlot = false;
        }
        
        if ((anglesItem.y - anglesSlot.y) >= 0) shoulderFirst = true; else shoulderFirst = false;

        
        if (initMoveToItem) {
            if (!baseRotated) {
                rotateBase(arm, anglesItem.x - anglesSlot.x);
                if (++baseTemp >= speed) baseRotated = true;
            } else if (!shoulderRotated && shoulderFirst) {
                rotateShoulder(arm, anglesItem.y - anglesSlot.y);
                if (++shoulderTemp >= speed) shoulderRotated = true;
            } else if (!elbowRotated) {
                rotateElbow(arm, anglesItem.z - anglesSlot.z);
                if (++elbowTemp >= speed) elbowRotated = true;
            } else if (!shoulderRotated && !shoulderFirst) {
                rotateShoulder(arm, anglesItem.y - anglesSlot.y);
                if (++shoulderTemp >= speed) shoulderRotated = true;
            }
            
            if (baseRotated && shoulderRotated && elbowRotated) {
                prepareTransition();
                initMoveToItem = false;
                initMoveToStorage = true;
                getNewSlot = true;
                Item x = buffer.items.get(itemNumber - 1);
                arm.nodeJoint4.attachChild(x.itemNode);
                x.itemNode.setLocalTranslation(0, -arm.toolHeight - buffer.itemHeight / 2, 0);
            }
            
        } else if (initMoveToStorage) {
            if (!baseRotated) {
                rotateBase(arm, anglesSlot.x - anglesItem.x);
                if (++baseTemp >= speed) baseRotated = true;
            } else if (!shoulderRotated && !shoulderFirst) {
                rotateShoulder(arm, anglesSlot.y - anglesItem.y);
                if (++shoulderTemp >= speed) shoulderRotated = true;
            } else if (!elbowRotated) {
                rotateElbow(arm, anglesSlot.z - anglesItem.z);
                if (++elbowTemp >= speed) elbowRotated = true;
            } else if (!shoulderRotated && shoulderFirst) {
                rotateShoulder(arm, anglesSlot.y - anglesItem.y);
                if (++shoulderTemp >= speed) shoulderRotated = true;
            }
            if (baseRotated && shoulderRotated && elbowRotated) {
                prepareTransition();
                initMoveToStorage = false;
                initMoveToItem = true;
                getNewItem = true;
                Item x = buffer.items.get(itemNumber - 1);
                arm.nodeJoint4.detachChild(x.itemNode);
                x.itemNode.setLocalTranslation(targetSlotPos.add(0f, -arm.toolHeight-buffer.itemHeight / 2, 0f));
                rootNode.attachChild(x.itemNode);
                
            }
        }
    }
    
    private void prepareTransition() {
        baseTemp = 0; shoulderTemp = 0; elbowTemp = 0; initialTemp = 0;
        baseRotated = false; shoulderRotated = false; elbowRotated = false; initialRotated = false;
    }
    
	private void rotateBase(Arm arm, float step) {
		arm.nodeJoint1.rotate(0f, step / speed, 0f);
	}
	private void rotateShoulder(Arm arm, float step) {
		arm.nodeJoint2.rotate(0f, 0f, step / speed);
		arm.nodeJoint4.rotate(0f, 0f, -step / speed);
	}
	private void rotateElbow(Arm arm, float step) {
		arm.nodeJoint3.rotate(0f, 0f, step / speed);
		arm.nodeJoint4.rotate(0f, 0f, -step / speed);
	}
}