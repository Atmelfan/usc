import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display

/**
 * Created by atmelfan on 2015-08-28.
 */
class GuiChat(renderer: Renderer) extends Gui(renderer){

  override def init(): Unit ={
    println("T")
    val tf = new TextField().text("").position(0, 0).size(1180, 25).callback(new ComponentCallback {
      override def on_resize(): Unit = {
        parent.w = Display.getWidth - 100
        super.on_resize()
      }

      override def on_enter(s: String): Boolean = {
        println(">" + s)
        false
      }
    })
    val send = new Button().text("Send").position(1180, 0).size(100, 25).callback(new ComponentCallback {
      override def on_resize(): Unit = {
        parent.x = Display.getWidth - 100
        super.on_resize()
      }

      override def on_press(): Unit = {
        println(">" + tf.text)
        super.on_press()
      }
    })
    components += tf
    components += send
  }

  override def onKey(key: Int, c: Char): Unit ={
    if(focus != null){
      if(key == Keyboard.KEY_ESCAPE){
        focus.focused(false)
        focus = null
      }else{
        focus.on_key(key, c)
      }
    }
  }
}
