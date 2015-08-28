import org.jbox2d.dynamics.contacts.Contact

/**
 * Created by atmelfan on 2015-08-07.
 *
 */
trait Collidable {
  def on_collision(otherobj: Collidable, p1: Contact): Unit

  def should_collide(otherobj: Collidable): Boolean = true

  def hover_text(): String = ""

  def hover_press(): Unit ={

  }
}
