import javax.vecmath.{Vector3f, Quat4f, Matrix4f}

import com.bulletphysics.collision.shapes.{SphereShape, StaticPlaneShape}
import com.bulletphysics.dynamics.{RigidBodyConstructionInfo, RigidBody}
import com.bulletphysics.linearmath.{Transform, DefaultMotionState}

/**
 * Created by atmelfan on 2014-12-22.
 */
class EntityPhysics extends Entity {
  val transform = new Transform()
  val groundShape = new SphereShape(1)

  // setup the motion state
  val groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1.0f)))

  val groundRigidBodyCI = new RigidBodyConstructionInfo(getMass, groundMotionState, groundShape, new Vector3f(0,0,0))
  val body = new RigidBody(groundRigidBodyCI)

  override def getBody: RigidBody = body

  override def getPosition: Vector3f = body.getWorldTransform(transform).origin

  override def getVelocity: Vector3f = body.getLinearVelocity(new Vector3f())
}
