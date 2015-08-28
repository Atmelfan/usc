import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.contacts.Contact

/**
 * Created by atmelfan on 2015-08-07.
 */
class PhysObj extends Collidable{
  var body: Body = null

  var onCollision = ""

  def on_collision(otherobj: Collidable, p1: Contact): Unit = {

  }
}
