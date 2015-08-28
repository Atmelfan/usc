import org.jbox2d.collision.WorldManifold
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.contacts.Contact
import org.lwjgl.input.Keyboard


/**
 * Created by atmelfan on 2015-01-02.
 */
class EntityPlayer(space: Space) extends EntityShip(space) {
  override def update(): Unit = {
    cooldown = Math.max(0, cooldown - 1)
    super.update()
  }

  var cooldown = 0

  override def control(): Unit ={
    var vel = getBody.getLinearVelocity
    var ang = getBody.getAngularVelocity

    if(Keyboard.isKeyDown(Keyboard.KEY_W)){
      vel = vel.add(getBody.getWorldVector(new Vec2(  0, -20)))

      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2( 60, 190)), 1f, vel.negate().mul(0.1f))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2(-60, 190)), 1f, vel.negate().mul(0.1f))
    }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
      vel = vel.add(getBody.getWorldVector(new Vec2(  0,  10)))

      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2( 60, -20)), 0.5f, vel.negate().mul(0.05f))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2(-60, -20)), 0.5f, vel.negate().mul(0.05f))
    }else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
      vel = vel.add(getBody.getWorldVector(new Vec2( 20, 0)))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2( 80, 190)), 1f, vel.negate().mul(0.1f))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2(-80, 190)), 1f, vel.negate().mul(0.1f))
    }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
      vel = vel.add(getBody.getWorldVector(new Vec2(-20, 0)))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2( 80, 190)), 1f, vel.negate().mul(0.1f))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2(-80, 190)), 1f, vel.negate().mul(0.1f))
    }else if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
      ang = Math.min(ang + Math.PI/40, Math.PI/2).toFloat
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2( 80, 190)), 1f, vel.negate().mul(0.1f))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2(-80, 190)), 1f, vel.negate().mul(0.1f))
    }else if(Keyboard.isKeyDown(Keyboard.KEY_E)){
      ang = Math.max(ang - Math.PI/40, -Math.PI/2).toFloat
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2( 80, 190)), 1f, vel.negate().mul(0.1f))
      space.addParticle("particle_exhaust.gmd", getBody.getWorldPoint(new Vec2(-80, 190)), 1f, vel.negate().mul(0.1f))
    }else if(Keyboard.isKeyDown(Keyboard.KEY_F) && cooldown == 0){
      val p = new EntityProjectile(space,"missile.gmd",this)
      space.addEntity(p)
      p.setPosition(getPosition)
      val e = get_closest_entity(p)
      if(e != null){
        p.track(e)
      }else{
        p.fireFromParent(new Vec2(), 1000)
      }

      cooldown = 100
    }else if(Keyboard.isKeyDown(Keyboard.KEY_R)){
      vel = vel.mul(0.95f)
      ang = ang*0.95f
    }
    getBody.setAngularVelocity(ang)
    getBody.setLinearVelocity(vel)
  }


  override def on_damage(): Unit = {

  }

  override def hover_press(): Unit = {
    space.addParticle("particle_explode.gmd", getPosition)
  }

  override def on_collision(otherobj: Collidable, p1: Contact): Unit = {
    val manifold = new WorldManifold()
    p1.getWorldManifold(manifold)
    manifold.points.foreach{ vec =>
      if(vec.lengthSquared() != 0) space.addParticle("particle_explode.gmd", vec, 1f + Math.random().toFloat*1f)
    }
  }
}
