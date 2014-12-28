import javax.vecmath.Vector3f

import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display

/**
 * Created by atmelfan on 2014-12-22.
 */
class EntityMissile(dir: Vector3f) extends EntityPhysics{
  var isalive = true


  override def update(): Unit = {
    val vel = new Vector3f(Mouse.getX - Display.getWidth/2, Mouse.getY - Display.getHeight/2, 0)
    vel.sub(getPosition)
    if(vel.length() < 25)
      isalive = false
    vel.scale(0.1f)
    getBody.applyCentralImpulse(vel)
    super.update()
  }


  override def isAlive: Boolean = isalive

  override def getMass: Float = 10
}
