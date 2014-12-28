/**
 * Created by atmelfan on 2014-12-28.
 */
class GuiWindow extends GuiComponent{

  def draw(x: Int = 0, y: Int = 0): Unit = {

  }
}

trait GuiComponent {
  var id = -1
  var x, y = (0, 0)

  def draw(x: Int = 0, y: Int = 0): Unit
}
