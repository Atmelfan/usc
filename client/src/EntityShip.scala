import org.jbox2d.common.Vec2


/**
 * Created by atmelfan on 2014-12-22.
 */
object EntityShip {
  val RCS_RIGHT = 0x01
  val RCS_LEFT = 0x02
  val RCS_FORWARD = 0x04
  val RCS_BACKWARD = 0x08
}


class EntityShip(space: Space) extends Entity(space){
  override def spriteName: String = "deadalus.gmd"

  class Thruster(offset: Vec2, dir: Int){
    def activate(head: Vec2, force: Float): Unit ={

    }
  }

  def addThruster(offset: Vec2, dir: Int): Unit ={

  }

  def rcs(dir: Vec2, force: Float): Unit ={

  }

}
