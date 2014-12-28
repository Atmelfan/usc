import java.io.File
import javax.vecmath.{Matrix4f, Vector3f}

import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.dynamics.RigidBody
import org.lwjgl.opengl.GL11
import renderer.{Texture, GLutil}

/**
 * Created by atmelfan on 2014-12-21.
 */

class Entity extends PhysicalObject{


  def update(): Unit ={

  }

  def debugDraw(): Unit ={
    //println("drawn!")
    GLutil.push(){
      val pos = getPosition
      val vel = getVelocity
      GL11.glTranslatef(pos.x, pos.y, pos.z)
      GLutil.begin(GL11.GL_LINES){
        GL11.glColor3f(1, 0, 0); GL11.glVertex3f(0, 0, 0)
        GL11.glColor3f(1, 0, 0); GL11.glVertex3f(10 + vel.x, 0, 0)
        GL11.glColor3f(0, 1, 0); GL11.glVertex3f(0, 0, 0)
        GL11.glColor3f(0, 1, 0); GL11.glVertex3f(0, 10 + vel.y, 0)
        GL11.glColor3f(0, 0, 1); GL11.glVertex3f(0, 0, 0)
        GL11.glColor3f(0, 0, 1); GL11.glVertex3f(0, 0, 10 + vel.z)
      }
      GL11.glColor3f(1, 1, 1)
    }
  }

  def getDistanceTo(position: Vector3f): Float ={
    if(position == null)
      return -1
    val temp = new Vector3f(position)
    temp.sub(getPosition)
    temp.length()
  }

  def getDistanceToEntity(entity: Entity): Float ={
    if(entity == null)
      return -1
    getDistanceTo(entity.getPosition)
  }

  def getMatrix = new Matrix4f()

  def getRotation = new Vector3f()

  def getPosition = new Vector3f()

  def getVelocity = new Vector3f(1, 1, 1)

  def isAlive = true

  //Physics callbacks/values
  override def onCollision(body: PhysicalObject, userpointer: AnyRef): Unit = {

  }

  override def getMask: Short = 0

  override def getGroup: Short = 0

  override def getMass: Float = 0f

  override def getBody: RigidBody = null
}
