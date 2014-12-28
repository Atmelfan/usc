import java.io.File
import javax.vecmath
import javax.vecmath.Vector3f

import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.dispatch.{CollisionDispatcher, DefaultCollisionConfiguration}
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import org.lwjgl.input.{Mouse, Keyboard}
import org.lwjgl.util.glu.GLU
import org.lwjgl.{input, opengl, BufferUtils}
import org.lwjgl.opengl._
import renderer.{GLutil, Renderer}

/**
 * Created by atmelfan on 2014-09-23.
 */

object USC_client {
  var debugmode = false
  def main (args: Array[String]) {
    new USC_client().run()
  }

}

class USC_client {
  val renderer = new Renderer()
  var close = false
  var space: Space = null
  val config = new Gmd()

  def run(): Unit = {
    println("OS name: " + System.getProperty("os.name") + " " + System.getProperty("os.version"))
    println("OS arch: " + System.getProperty("os.arch"))
    config.parse(new File("settings.gmd"))
    renderer.init(config("graphics")("resolution").getInt(1), config("graphics")("resolution").getInt(2), config("graphics")("title").getString(1))
    val broadphase = new DbvtBroadphase()
    val collisionConfiguration = new DefaultCollisionConfiguration()
    val dispatcher = new CollisionDispatcher(collisionConfiguration)
    val solver = new SequentialImpulseConstraintSolver()

    space = new Space(dispatcher, broadphase, solver, collisionConfiguration)
    space.addEntity(new EntityShip)

    val model = new ModelSMD(new File("resources/models/modl.smd"))

    while (renderer.update() && !close){
      //-------------------------------INPUT-------------------------------
      while (Keyboard.next()){
        val eventkey = Keyboard.getEventKey
        val eventchar = Keyboard.getEventCharacter

        if(Keyboard.getEventKeyState){
          if(eventkey == Keyboard.KEY_F){
            val dir = new Vector3f()
            dir.x = (Math.random()*1000-500).toFloat
            dir.y = (Math.random()*1000-500).toFloat
            //dir.z = (Math.random()*500).toFloat
            space.addEntity(new EntityMissile(dir))
          }
        }
      }
      space.update()

      //-------------------------------RENDER-------------------------------
      GLutil.push(){
        space.draw()
      }

      //-------------------------------MISC-------------------------------
      val error = GL11.glGetError()
      if(error != GL11.GL_NO_ERROR){
        println("GL ERROR: " + GLU.gluErrorString(error))
      }

      Display.update()
      Display.sync(60)

    }
    renderer.destroy()

  }

}
