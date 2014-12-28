import java.io.File

import org.lwjgl.opengl.GL11
import renderer.{GLutil, Texture}

/**
 * Created by atmelfan on 2014-12-22.
 */
class EntityShip extends EntityPhysics{
  val temptexture = new Texture(new File("resources/textures/deadalus.png"), GL11.GL_REPEAT, GL11.GL_NEAREST)

  override def debugDraw(): Unit = {
    GLutil.push(){
      val pos = getPosition
      GL11.glTranslatef(pos.x, pos.y, pos.z)
      temptexture.bind(){
        GLutil.glDrawSquare(163, 100)
      }
    }
    super.debugDraw()
  }
}
