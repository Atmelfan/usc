import org.lwjgl.input.Mouse
import org.lwjgl.opengl.{GL11, Display}
import renderer.GLutil

/**
 * Created by atmelfan on 2015-08-28.
 */
class GuiWindow(renderer: Renderer) extends Gui(renderer){
  var x = 100
  var y = 100
  var h = 0
  var w = 0
  var tx = 0
  var ty = 0

  def hit(bx: Int, by: Int): Boolean ={
    bx >= x && bx <= (x+w) && by >= y && by <= (y+h)
  }

  override def onPress(mx: Int = Mouse.getX, my: Int = Mouse.getY): Boolean = {
    if(hit(mx, my) && Mouse.isButtonDown(1)){
      tx = x - mx
      ty = y - my
    }
    super.onPress(mx - x, my - y)
  }

  override def update(): Boolean = {
    val mx: Int = Mouse.getX
    val my: Int = Mouse.getY
    println("" + hit(mx, my) + ": " + x + "< " + mx + "<" + (x+w) + ", "+ y + "< " + my + "<" + (y+h))
    if(hit(mx, my) && Mouse.isButtonDown(1)){

      x = mx + tx
      y = my + ty
      println("y")
    }
    super.update()
  }

  def colorInt(color: Int): Unit ={
    val r = (color >> 24) & 0xFF
    val g = (color >> 16) & 0xFF
    val b = (color >>  8) & 0xFF
    val a = (color >>  0) & 0xFF
    GL11.glColor4ub(r.toByte,g.toByte,b.toByte,a.toByte)
  }

  override def draw(): Unit = {
    GLutil.push(){
      GL11.glTranslatef( - Display.getWidth/2,  - Display.getHeight/2, 0)
      val tx = x
      val ty = y
      colorInt(0x444444AA)
      GLutil.begin(GL11.GL_QUADS){
        GL11.glVertex2i(tx+0, ty+h)
        GL11.glVertex2i(tx+0, ty+0)
        GL11.glVertex2i(tx+w, ty+0)
        GL11.glVertex2i(tx+w, ty+h)
      }
      colorInt(0x88000088)
      GLutil.begin(GL11.GL_LINE_LOOP){
        GL11.glVertex2i(tx+0, ty+h)
        GL11.glVertex2i(tx+0, ty+0)
        GL11.glVertex2i(tx+w, ty+0)
        GL11.glVertex2i(tx+w, ty+h)
      }

      colorInt(0xFFFFFFFF)
    }
    GLutil.push(){
      GL11.glTranslatef(x, y, 0)
      super.draw()

    }
  }
}
