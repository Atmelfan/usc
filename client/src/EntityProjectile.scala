import org.jbox2d.collision.WorldManifold
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.contacts.Contact

/**
 * Created by atmelfan on 2015-08-15.
 */
class EntityProjectile(space: Space, name: String, parent: Entity) extends EntityAI(space) {

  mass = 15

  override def getParent = parent

  override def spriteName: String = name

  def fire(pos: Vec2, velocity: Float, angle: Float): Unit ={
    setPositionAndAngle(pos, angle)
    val v = new Vec2(velocity*Math.cos(angle).toFloat, velocity*Math.sin(angle).toFloat)
    println(angle)
    getBody.setLinearVelocity(v)
  }

  def fireFromParent(ofs: Vec2, velocity: Float): Unit ={
    if(parent != null){
      var t = parent.getBody.getWorldVector(ofs)
      t = t.add(parent.getPosition)
      val a = parent.getRotation
      fire(t,velocity, a)
    }
  }

  var target: Entity = null
  def track(entity: Entity): Unit ={
    target = entity
  }

  override def update(): Unit = {
    if(target != null){
      if(target.isAlive){
        path.calculatePath(getPosition, target.getPosition)
      }else{
        alive = false
        space.addParticle("particle_explode.gmd", getPosition, 5f + Math.random().toFloat*1f)
      }
    }

    super.update()
  }

  override def getBodyDef: BodyDef = {
    val d = super.getBodyDef
    d.bullet = true
    d
  }



  override def on_collision(otherobj: Collidable, p1: Contact): Unit = {
    alive = false
    val manifold = new WorldManifold()
    p1.getWorldManifold(manifold)
    manifold.points.foreach{ vec =>
      if(vec.lengthSquared() != 0) space.addParticle("particle_explode.gmd", vec, 5f + Math.random().toFloat*1f)
    }
    otherobj match {
      case e:Entity =>
        e.on_damage()
      case _ =>
    }
    super.on_collision(otherobj, p1)
  }
}
