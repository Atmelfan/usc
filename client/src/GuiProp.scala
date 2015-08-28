/**
 * Created by atmelfan on 2015-08-28.
 */
class GuiProp(renderer: Renderer) extends GuiWindow(renderer){
  override def init(): Unit = {
    super.init()
    components += new TextField().text("name").position(10, 10).size(100, 25)
    components += new TextField().text("value").position(120, 10).size(100, 25)
    components += new Button().text("X").position(230, 10).size(25, 25).callback(new ComponentCallback {
      override def on_press(): Unit = {
        open = false
        super.on_press()
      }
    })
    h = 45
    w = 265
  }
}
