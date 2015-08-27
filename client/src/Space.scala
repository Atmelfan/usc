import java.io.File

import org.jbox2d.callbacks.{ContactFilter, DebugDraw, ContactImpulse, ContactListener}
import org.jbox2d.collision.{AABB, Manifold}
import org.jbox2d.common.{OBBViewportTransform, Transform, Color3f, Vec2}
import org.jbox2d.dynamics._
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.dynamics.joints.{Joint, JointDef}
import org.lwjgl.input.Mouse
import org.lwjgl.opengl
import org.lwjgl.opengl.{Display, GL11}
import renderer.GLutil

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2014-09-23.
 */

object Space {
  val commands = Map[String, Command](
    "say" -> new CommandSay,
    "help" -> new CommandSay
  )
}

case class FixtureCommand(command: String)

class Space(renderer: Renderer, config: File, usc: USC_client) extends Collidable{
  var world = new World(new Vec2(0,0))
  var entities = new ArrayBuffer[Entity]()
  var particles = ArrayBuffer[Particle]()
  var directory = config.getParentFile
  var levelbody: Body = {
    val bdef = new BodyDef()
    bdef.`type` = BodyType.STATIC
    bdef.fixedRotation = true
    bdef.userData = this
    world.createBody(bdef)
  }
  parse(config)

  def getRenderer = renderer

  def draw(): Unit ={


    for(entity <- entities){
      entity.draw()
    }

    for(particle <- particles){
      particle.draw()
    }

    val x = Mouse.getX - Display.getWidth/2
    val y = Mouse.getY - Display.getHeight/2
    val mouse = new Vec2(x,y).add(usc.player.getPosition)
    touch(mouse){ obj =>
      renderer.drawText(mouse.x,mouse.y,obj.hover_text())
    }


  }

  def action(): Unit ={
    val x = Mouse.getX - Display.getWidth/2
    val y = Mouse.getY - Display.getHeight/2
    val mouse = new Vec2(x,y).add(usc.player.getPosition)
    touch(mouse){ obj =>
      obj.hover_press()
    }
  }

  def touch(vec: Vec2)(f: Collidable => Unit): Unit ={

    var body = world.getBodyList
    var test = false
    while (body != null){
      var fixt = body.getFixtureList
      while (fixt != null){
        if(fixt.testPoint(vec)){
          test = true
        }
        fixt = fixt.getNext
      }
      if(test){
        f(body.getUserData.asInstanceOf[Collidable])
      }
      test = false
      body = body.getNext
    }
  }



  def drawDebug(): Unit ={
    GLutil.push(){
      world.drawDebugData()
    }
  }

  def command(cmd: String, actor: Entity = null, target: Entity = null): Unit = {
    val args = cmd.split(" ")
    val c = Space.commands.getOrElse(args(0), null)
    if(c != null){
      c.execute(args, this, actor, target)
    }else{
      USC_client.client.chat.add("!Unknown command: " + args(0))
    }
  }

  def update(): Unit ={
    world.step(1/20f, 6, 2)

    val iterator = entities.iterator
    val entitiesToDelete = ArrayBuffer[Entity]()
    while(iterator.hasNext){
      val entity = iterator.next()
      entity.update()
      if(!entity.isAlive){
        entitiesToDelete += entity
      }
    }
    entitiesToDelete.foreach{ e =>
      removeEntity(e)
    }


    val iteratorp = particles.iterator
    val particlesToDelete = ArrayBuffer[Particle]()
    while(iteratorp.hasNext){
      val particle = iteratorp.next()
      particle.update()
      if(particle.shouldRemove()){
        particlesToDelete += particle
      }
    }
    particles --= particlesToDelete
  }

  val debugdraw = new DebugDraw(new OBBViewportTransform){
    override def drawSolidCircle(p1: Vec2, p2: Float, p3: Vec2, p4: Color3f): Unit = {
      GL11.glColor4f(p4.x, p4.y, p4.z, 0.5f)
      GLutil.begin(GL11.GL_POLYGON){
        for(i <- 0 to 360){
          val x = p2*Math.cos(i*Math.PI/180) + p1.x
          val y = p2*Math.sin(i*Math.PI/180) + p1.y
          GL11.glVertex2d(x, y)
        }
      }

      GL11.glColor4f(p4.x, p4.y, p4.z, 1f)
      GLutil.begin(GL11.GL_LINE_LOOP){
        for(i <- 0 to 360){
          val x = p2*Math.cos(i*Math.PI/180) + p1.x
          val y = p2*Math.sin(i*Math.PI/180) + p1.y
          GL11.glVertex2d(x, y)
        }
      }
      GL11.glColor4f(1, 1, 1, 1)

    }

    override def drawCircle(p1: Vec2, p2: Float, p3: Color3f): Unit = {


    }

    override def drawSegment(p1: Vec2, p2: Vec2, p3: Color3f): Unit = {
      GL11.glColor4f(p3.x, p3.y, p3.z, 1f)
      GLutil.begin(GL11.GL_LINES){
        GL11.glVertex2f(p1.x, p1.y)
        GL11.glVertex2f(p2.x, p2.y)
      }
      GL11.glColor4f(1, 1, 1, 1)

    }

    override def drawString(p1: Float, p2: Float, p3: String, p4: Color3f): Unit = {

    }

    override def drawSolidPolygon(p1: Array[Vec2], p2: Int, p3: Color3f): Unit = {
      GL11.glColor4f(p3.x, p3.y, p3.z, 0.5f)
      GLutil.begin(GL11.GL_POLYGON){
        p1.foreach{ vec =>
          if(vec.lengthSquared() != 0) GL11.glVertex2f(vec.x, vec.y)
        }
      }

      GL11.glColor4f(p3.x, p3.y, p3.z, 1f)
      GLutil.begin(GL11.GL_LINE_LOOP){
        p1.foreach{ vec =>
          if(vec.lengthSquared() != 0) GL11.glVertex2f(vec.x, vec.y)
        }
      }
      GL11.glColor4f(1, 1, 1, 1)
    }

    override def drawTransform(p1: Transform): Unit = {

    }

    override def drawPoint(p1: Vec2, p2: Float, p3: Color3f): Unit = {
      GL11.glColor4f(1, p3.y, p3.z, 1f)
      GLutil.begin(GL11.GL_POINTS){
        GL11.glVertex2f(p1.x, p1.y)
      }
      GL11.glColor4f(1, 1, 1, 1)
    }
  }
  debugdraw.setFlags(DebugDraw.e_shapeBit)

  world.setDebugDraw(debugdraw)

  world.setContactFilter(new ContactFilter{
    override def shouldCollide(fixtureA: Fixture, fixtureB: Fixture): Boolean = {
      val b1 = fixtureA.getBody
      val e1 = b1.getUserData.asInstanceOf[Collidable]
      val b2 = fixtureB.getBody
      val e2 = b2.getUserData.asInstanceOf[Collidable]

      if(!e1.should_collide(e2) || !e2.should_collide(e1))
        false
      else
        super.shouldCollide(fixtureA, fixtureB)
    }
  })

  world.setContactListener(new ContactListener {
    override def postSolve(p1: Contact, p2: ContactImpulse): Unit = {
    }

    override def endContact(p1: Contact): Unit = {
    }

    override def beginContact(p1: Contact): Unit = {
      val b1 = p1.getFixtureA.getBody
      val e1 = b1.getUserData.asInstanceOf[Collidable]
      val b2 = p1.getFixtureB.getBody
      val e2 = b2.getUserData.asInstanceOf[Collidable]
      e1.on_collision(e2, p1)
      e2.on_collision(e1, p1)
    }

    override def preSolve(p1: Contact, p2: Manifold): Unit = {
    }
  })

  override def on_collision(otherobj: Collidable, p1: Contact): Unit = {

  }

  def parse(file: File): Unit ={
    val gmd = new Gmd(file)
    //Set gravity
    val gravity = gmd("gravity")
    world.setGravity(new Vec2(gravity.getFloat(1),gravity.getFloat(2)))
    //Parse entities
    val entities = gmd("entities")
    for(entity_ <- entities.getChildren){
      val entity = getNewEntity(entity_.getString(0))
      if(entity_.argCount > 1){
        entity.set_name(entity_.getString(1))
      }
      if(entity != null){
        //entity.setProperties(entity_)
        entity.pre_load(entity_)
        addEntity(entity)
        entity.post_load(entity_)
      }
    }

    gmd("fixtures").getChildren.foreach{fixturedef =>
      val fixture = levelbody.createFixture(ShapeGmd.fixtureFromGmd(fixturedef))
      fixturedef.ifExists("execute"){ tag =>
        fixture.setUserData(new FixtureCommand(tag.getString(1)))
      }
    }
  }

  def addJoint(jointdef: JointDef): Joint ={
    world.createJoint(jointdef)
  }

  def addParticle(s: String, pos: Vec2, scale: Float = 1f, vel: Vec2 = new Vec2(0,0)): Unit ={
    particles += new Particle(s, pos, scale, vel)
  }

  def addEntity(entity: Entity): Unit ={
    val bodydef = entity.getBodyDef
    if(bodydef != null){
      entity.body = world.createBody(bodydef)
      entity.body.setUserData(entity)
      val fixture = entity.getFixture
      if(fixture != null){
        entity.getBody.createFixture(fixture)
        val massdata = entity.getMass
        if(massdata != null){
          entity.getBody.setMassData(massdata)
        }
      }
    }
    entities += entity
  }

  def removeEntity(entity: Entity): Unit ={
    world.destroyBody(entity.getBody)
    entities -= entity
  }

  def getNewEntity(s: String): Entity ={
    s.toLowerCase match {
      case "asteroid" => new EntityAsteroid(this)
      case "ship" => new EntityShip(this)
      case "prop" => new EntityProp(this)
      case "miner" => new EntityAI(this)
      case other => println("Entity \"" + other + "\" doesn't exist."); null
    }
  }

}