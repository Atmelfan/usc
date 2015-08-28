import java.io.{FileNotFoundException, File}
import javax.vecmath
import javax.vecmath.Vector3f

import org.lwjgl.input.{Mouse, Keyboard}
import org.lwjgl.util.glu.GLU
import org.lwjgl.{Sys, input, opengl, BufferUtils}
import org.lwjgl.opengl._
import renderer.GLutil

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer


/**
 * Created by atmelfan on 2014-09-23.
 */

object USC_client {
  var client = new USC_client()
  var debugmode = false
  var resources: ResourceManager = null
  def main (args: Array[String]) {
    client.run()
  }

}


class USC_client {
  var debug = false
  val renderer = new Renderer()
  var close = false
  var space: Space = null
  val config = new Gmd()
  var player: Entity = null
  var time = 0L
  var chat = new Chat(renderer)
  var focusEntity: Entity = null

  def run(): Unit = {
    println("OS name: " + System.getProperty("os.name") + " " + System.getProperty("os.version"))
    println("OS arch: " + System.getProperty("os.arch"))
    try {
      config.parse(new File("settings.gmd"))
    } catch {
      case e: FileNotFoundException =>
        val g = config.add("graphics")
        g.add("resolution 1280 720")
        g.add("title USC")
        config.add("server _dev")
        config.dump(new File("settings.gmd"))
    }
    USC_client.resources = new ResourceManager(new File("servers/" + config("server").getString(1)))
    renderer.init(config("graphics")("resolution").getInt(1), config("graphics")("resolution").getInt(2), config("graphics")("title").getString(1))
    space = new Space(renderer, new File("servers/" + config("server").getString(1) + "/space.gmd"), this)
    player = new EntityPlayer(space)
    space.addEntity(player)
    val background = USC_client.resources.getResource[Texture]("space.png", ResourceType.Texture){
      new Texture(_)
    }
    var frame = 0
    time = Sys.getTime
    while (renderer.update() && !close){
      //-------------------------------INPUT-------------------------------
      while (Keyboard.next()){
        val eventkey = Keyboard.getEventKey
        val eventchar = Keyboard.getEventCharacter

        if(Keyboard.getEventKeyState){
          if(eventkey == Keyboard.KEY_F5){
            USC_client.resources.reload()
          }else
          if(eventkey == Keyboard.KEY_F3){
            debug = !debug
          }
        }
      }

      while (Mouse.next()){
        val eventbutton = Mouse.getEventButton
        val eventstate = Mouse.getEventButtonState
        if(eventstate){
          space.action()
        }
      }
      val t = Sys.getTime
      if(t - time >= 50){
        player.control()
        space.update()
        chat.update()
        time = t
      }



      //-------------------------------RENDER-------------------------------
      GLutil.push(){
        val focus = player.getPosition
        background.bind(){
          GLutil.glDrawSquare(background.height/2, background.width/2)
        }
        chat.draw()
        GL11.glTranslatef(-focus.x, -focus.y,0)
        space.draw()
        if(debug){
          space.drawDebug()
        }


      }

      //-------------------------------MISC-------------------------------
      val error = GL11.glGetError()
      if(error != GL11.GL_NO_ERROR){
        println("GL ERROR: " + GLU.gluErrorString(error))
      }

      Display.update()
      Display.sync(60)
    }
    USC_client.resources.destroy()
    renderer.destroy()

  }


}
