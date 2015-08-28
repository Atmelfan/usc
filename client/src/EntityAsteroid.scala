import org.jbox2d.collision.shapes.MassData
import org.jbox2d.dynamics.{BodyDef, BodyType}
/**
 * Created by atmelfan on 2015-01-02.
 */
class EntityAsteroid(space: Space) extends Entity(space){
  override def spriteName = "asteroid.gmd"

  override  def getBodyDef: BodyDef = {
    val bodydef = new BodyDef
    bodydef.`type` = BodyType.DYNAMIC
    bodydef
  }

  override def getMass: MassData = {
    val mass = new MassData()
    mass.mass = 0
    mass
  }
}
