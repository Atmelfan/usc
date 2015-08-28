import java.io.File

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.dynamics.{BodyType, BodyDef}

/**
 * Created by atmelfan on 2015-08-14.
 */
class EntityProp(space: Space) extends EntityAI(space){
  var cmd_on_press = ""
  var cmd_on_collision = ""
  var collided_with = false


  override def getBodyDef: BodyDef = {
    val bodydef = new BodyDef
    bodydef.`type` = BodyType.DYNAMIC
    bodydef
  }


  override def hover_press(): Unit = {
    super.hover_press()
    if(!cmd_on_press.isEmpty)
    space.command(cmd_on_press, this)
  }

  override def update(): Unit = {
    super.update()
    if (collided_with && !cmd_on_collision.isEmpty){
      space.command(cmd_on_collision, this)
      collided_with = false
    }
  }


  override def on_collision(otherobj: Collidable, p1: Contact): Unit = {
    super.on_collision(otherobj, p1)
    collided_with = true
  }

  override def pre_load(props: Gmdtag): Unit ={
    super.pre_load(props)
    props.ifExists("sprite"){ tag =>
      sprite = USC_client.resources.getResource[Sprite](tag.getString(1), ResourceType.Sprite){ s =>
        new Sprite(new File(s))
      }
    }
    props.ifExists("on_press"){ tag =>
      cmd_on_press = tag.getString(1)
    }
    props.ifExists("on_collision"){ tag =>
      cmd_on_collision = tag.getString(1)
    }
  }
}
