import java.awt.Color

import org.jbox2d.common.Vec2
import org.lwjgl.input.{Keyboard, Mouse}
import org.lwjgl.opengl.{Display, GL11}
import renderer.GLutil

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2015-08-28.
 */


class Gui(renderer: Renderer) {
  val components = new ArrayBuffer[Component]()
  var open = true


  def init(): Unit ={

  }

  def resize(): Unit ={
    components.foreach(_.resize())
  }

  var focus: Component = null

  def draw(): Unit ={
    if(Display.wasResized()){
      resize()
    }
    GLutil.push(){
      GL11.glTranslatef(-Display.getWidth/2, -Display.getHeight/2, 0)
      components.foreach(_.drawComponent())
    }

  }

  def update(): Boolean ={
    open
  }

  def onPress(mx: Int = Mouse.getX, my: Int = Mouse.getY): Boolean ={
    var hit = false
    components.foreach{ c =>
      if(c.hit(mx, my))
      {
        hit = true
        c.on_press()
        if(focus != null && focus != this){
          focus.on_focus(false)
        }
        focus = c
        focus.on_focus(true)
      }
    }
    hit
  }

  def onKey(key: Int, c: Char): Unit ={
    if(key == Keyboard.KEY_ESCAPE){
      open = false
    }
    if(focus != null){
      focus.on_key(key, c)
    }
  }

  def onClose(): Unit ={

  }

  trait ComponentCallback{
    var parent: Component = null

    def on_focus(focus: Boolean): Unit ={ }

    def on_press(): Unit ={ }

    def on_enter(s: String): Boolean ={ true }

    def on_resize(): Unit ={ }
  }

  trait Component{
    var text = ""
    var x = 0
    var y = 0
    var w = 0
    var h = 0
    var background = 0x55555555
    var color = 0xFF000088
    var focused = false

    var callback: ComponentCallback = null

    def text(s: String): Component = {
      text = s
      this
    }

    def position(nx: Int, ny: Int): Component ={
      x = nx
      y = ny
      this
    }

    def size(nw: Int, nh: Int): Component ={
      w = nw
      h = nh
      this
    }

    def background(c: Int): Component ={
      background = c
      this
    }

    def color(c: Int): Component ={
      color = c
      this
    }

    def callback(call: ComponentCallback): Component ={
      callback = call
      callback.parent = this
      this
    }

    def focused(focus: Boolean): Unit ={
      focused = focus
    }

    def on_focus(focus: Boolean): Unit = {
      if(callback != null) callback.on_focus(focus)
      focused = focus
    }

    def on_press(): Unit ={
      if(callback != null) callback.on_press()
    }

    def on_key(key: Int, c: Char): Unit ={

    }

    def resize(): Unit ={
      if(callback != null) callback.on_resize()
    }

    def getBackground = background

    def getColor = color

    def hit(bx: Int, by: Int): Boolean ={
      bx >= x && bx <= (x+w) && by >= y && by <= (y+h)
    }

    def colorInt(color: Int): Unit ={
      val r = (color >> 24) & 0xFF
      val g = (color >> 16) & 0xFF
      val b = (color >>  8) & 0xFF
      val a = (color >>  0) & 0xFF
      GL11.glColor4ub(r.toByte,g.toByte,b.toByte,a.toByte)
    }

    def drawComponent(): Unit ={
      val tx = x
      val ty = y
      colorInt(getBackground)
      GLutil.begin(GL11.GL_QUADS){
        GL11.glVertex2i(tx+0, ty+h)
        GL11.glVertex2i(tx+0, ty+0)
        GL11.glVertex2i(tx+w, ty+0)
        GL11.glVertex2i(tx+w, ty+h)
      }
      colorInt(getColor)
      GLutil.begin(GL11.GL_LINE_LOOP){
        GL11.glVertex2i(tx+0, ty+h)
        GL11.glVertex2i(tx+0, ty+0)
        GL11.glVertex2i(tx+w, ty+0)
        GL11.glVertex2i(tx+w, ty+h)
      }
      renderer.drawText(tx,ty - 5,text)

      colorInt(0xFFFFFFFF)

    }
  }


  case class Button() extends Component

  case class TextField() extends Component{


    override def getColor: Int = if(focused) 0x00FF0088 else super.getColor

    override def on_key(key: Int, c: Char): Unit = {
      if(focused && c >= 32 && c < 127){
        text = text + c
      }
      if(key == Keyboard.KEY_BACK && text.length > 0){
        text = text.substring(0, text.length-1)
      }
      if(key == Keyboard.KEY_RETURN && callback != null){
        if(!callback.on_enter(text)){
          text = ""
        }
      }
      super.on_key(key, c)
    }
  }
}


