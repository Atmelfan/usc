import java.io.File

import org.jbox2d.common.Vec2
import org.lwjgl.opengl.GL11
import renderer.GLutil

/**
 * Created by atmelfan on 2015-08-09.
 */
class Particle(name: String, var pos: Vec2, scale: Float, vel: Vec2) {
  var frame = 0
  var particle = USC_client.resources.getResource[Sprite](name, ResourceType.Particle){ s =>
    new Sprite(new File(s))
  }

  def draw(): Unit ={
    GLutil.push(){
      GL11.glTranslatef(pos.x, pos.y, 0)
      GL11.glScalef(scale, scale, scale)
      particle.draw(frame)
    }

  }

  def update(): Unit ={
    frame += 1
    pos = pos.add(vel)
  }

  def shouldRemove() = frame >= particle.getNumFrames
}
