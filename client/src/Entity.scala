import java.io.File

import org.jbox2d.collision.WorldManifold
import org.jbox2d.collision.shapes.{MassData, PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics._
import org.jbox2d.dynamics.contacts.Contact
import org.lwjgl.opengl.GL11
import renderer.GLutil

/**
 * Created by atmelfan on 2014-12-21.
 */


abstract class Entity(space: Space) extends Collidable{
  var body: Body = null
  var name = ""
  var alive = true

  /*==========================GRAPHICS==========================*/
  def spriteName = "asteroid.gmd"

  var sprite = USC_client.resources.getResource[Sprite](spriteName, ResourceType.Sprite){ s =>
    new Sprite(new File(s))
  }

  def getFrame = 0

  def draw(): Unit ={
    if(sprite == null) return

    GLutil.push(){
      GL11.glTranslatef(getPosition.x, getPosition.y, 0)
      GL11.glRotated((getRotation/Math.PI)*180, 0, 0, 1)
      sprite.draw(getFrame)
    }
  }

  def getParent: Entity = null

  def control(): Unit ={

  }

  def update(): Unit ={

  }

  def get_closest_entity(except: Entity = null): Entity ={
    var closest: Entity = null
    space.entities.foreach{e =>
      if(e != this && e != except && (closest == null || getDistanceToEntity(e) < getDistanceToEntity(closest))){
        closest = e
      }
    }
    closest
  }
  
  def on_damage(): Unit ={
    alive = false
  }
  
  /*==========================TEXT==========================*/

  def set_name(s: String) = {
    name = s
  }

  def get_name: String = name

  override def hover_text(): String = get_name + ""

  override def hover_press(): Unit ={

  }

  /*==========================SAVE/LOAD==========================*/

  def pre_load(props: Gmdtag): Unit ={
    props.ifExists("mass"){ tag =>
      mass = tag.getFloat(1)
    }
  }

  def post_load(props: Gmdtag): Unit ={
    props.ifExists("position", 2){ position =>
      setPosition(new Vec2(position.getFloat(1),position.getFloat(2)))
    }
    props.ifExists("rotation", 1){ rotation =>
      setRotation(rotation.getFloat(1))
    }
  }

  /*==========================PHYSICS==========================*/
  override def should_collide(otherobj: Collidable): Boolean = {
    otherobj match {
      case e: Entity =>
        if(e.getParent == this)
          false
        else
          super.should_collide(otherobj)
      case _ =>
        super.should_collide(otherobj)
    }
  }

  override def on_collision(otherobj: Collidable, p1: Contact): Unit = {

  }

  def getBodyDef: BodyDef = {
    val bodydef = new BodyDef
    bodydef.`type` = BodyType.DYNAMIC
    bodydef
  }

  def getFixture: FixtureDef = {
    if(sprite != null){
      val fixt = sprite.getFixture
      return fixt
    }
    null
  }

  var mass = 1000f
  def getMass: MassData = {
    val mas = new MassData()
    mas.mass = mass
    mas
  }

  def getDistanceTo(position: Vec2): Float ={
    if(position == null)
      return -1
    val temp = new Vec2(position)
    temp.subLocal(getPosition)
    temp.length()
  }

  def getDistanceToEntity(entity: Entity): Float ={
    if(entity == null)
      return -1
    getDistanceTo(entity.getPosition)
  }

  /*==========================POSITION==========================*/

  def getBody: Body = body

  def getRotation = getBody.getAngle

  def setRotation(ang: Float): Unit = getBody.setTransform(getPosition, ang)

  def getPosition = getBody.getPosition

  def setPosition(pos: Vec2): Unit = getBody.setTransform(pos, getRotation)

  def setPositionAndAngle(pos: Vec2, ang: Float) = getBody.setTransform(pos, ang)

  def isAlive: Boolean = alive

  def getScale = 1f
}
