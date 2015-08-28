import java.awt.Shape
import java.io.{FileNotFoundException, IOException, File}

import org.jbox2d.dynamics.{FixtureDef, Fixture}
import org.lwjgl.opengl.GL11
import renderer.GLutil

/**
 * Created by atmelfan on 2015-08-07.
 */

class Sprite(file: File) extends Resource{
  var atlas_x = 1
  var atlas_y = 1
  var scale = 1f
  var texture: Texture = null
  var gmd: Gmd = null
  parse(file)

  def this(s: String) = this(new File(s))

  def getNumFrames = atlas_x*atlas_y

  def getFixture: FixtureDef ={
    if(gmd == null)
      return  null
    ShapeGmd.fixtureFromGmd(gmd("fixture"))
  }

  def parse(file: File): Unit ={
    try {
      gmd = new Gmd(file)
      texture = USC_client.resources.getResource[Texture](gmd("texture").getString(1), ResourceType.Texture){
        new Texture(_)
      }
      atlas_x = gmd("atlas").getInt(1)
      atlas_y = gmd("atlas").getInt(2)
      scale = gmd("scale").getFloat(1)
    }catch{
      case ioe: FileNotFoundException =>
        println("Error reading sprite:")
        ioe.printStackTrace()
      case e: Exception =>
        println("Error loading sprite:")
        e.printStackTrace()
    }

  }

  def draw(frame: Int = 0): Unit ={
    if(texture != null){
      val w = (texture.width/atlas_x)/2
      val h = (texture.height/atlas_y)/2
      val dw = 1f/atlas_x
      val dh = 1f/atlas_y
      val x = (frame % atlas_x)*dw
      val y = (frame / atlas_x)*dh
      //println("f %d, x %d, y %d".format(frame, frame % atlas_x, frame / atlas_y))
      texture.bind(){
        GLutil.push(){
          GL11.glScalef(scale,scale,scale)
          GLutil.begin(GL11.GL_QUADS){
            GLutil.glVertex(-1*w, -1*h, 0, x,     y+dh)
            GLutil.glVertex( 1*w, -1*h, 0, x+dw,  y+dh)
            GLutil.glVertex( 1*w,  1*h, 0, x+dw,  y)
            GLutil.glVertex(-1*w,  1*h, 0, x,     y)
          }
        }
      }
    }
  }

  class animation(){


    def isComplete = false
  }

  override def name: String = file.getName

  override def reload(): Unit = {

  }

  override def destroy(): Unit = {

  }
}
