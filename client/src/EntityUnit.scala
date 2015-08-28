import org.jbox2d.common.Vec2
import org.lwjgl.input.{Mouse, Keyboard}

/**
 * Created by atmelfan on 2015-08-14.
 */
class EntityUnit(space: Space) extends Entity(space){
  override def spriteName: String = "char.gmd"

}
