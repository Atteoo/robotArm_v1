package robotarm;

import java.util.ArrayList;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Kinematics {
	ArrayList<Vector3f> points;
	int index;
	int size; 

	//float dist = fromCoords.distance(toCoords);
	
	//initializes point vector
	public Kinematics() {
		points = new ArrayList<Vector3f>();
		index = 0;
	}
	
	public Vector3f solveIK(Vector3f target, Vector3f origin, float l1, float l2) {
        float dx = target.x - origin.x;
        float dy = target.y - origin.y;
        float dz = target.z - origin.z;
        
        float distHorizontal = FastMath.sqrt(dx * dx + dz * dz);
        float distTotal = FastMath.sqrt(distHorizontal * distHorizontal + dy * dy);
        distTotal = FastMath.clamp(distTotal, 0, l1 + l2 - 0.001f);

        float baseAngle = -FastMath.atan2(dz ,dx);
        
        float cosAngleElbow = (l1 * l1 + l2 * l2 - distTotal * distTotal) / (2 * l1 * l2);
        float angleElbow = FastMath.PI - FastMath.acos(cosAngleElbow); 

        float alpha = FastMath.atan2(dy, distHorizontal);
        float cosAngleBeta = (l1 * l1 + distTotal * distTotal - l2 * l2) / (2 * l1 * distTotal);
        float beta = FastMath.acos(cosAngleBeta);

        float angleShoulder = alpha + beta;

        return new Vector3f(baseAngle, angleShoulder, -angleElbow);
	}
	
}
