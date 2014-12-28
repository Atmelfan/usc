import javax.vecmath.Vector3f

import com.bulletphysics.collision.broadphase.{Dispatcher, DbvtBroadphase, BroadphaseInterface}
import com.bulletphysics.collision.dispatch.{CollisionConfiguration, DefaultCollisionConfiguration, CollisionDispatcher}
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.dynamics.{RigidBody, DynamicsWorld, InternalTickCallback, DiscreteDynamicsWorld}
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2014-09-23.
 */

object Space {
  val ENTITY: Short = (1 << 0).toShort
  val PARTICLE: Short = (1 << 1).toShort
  val OBJECT: Short = (1 << 2).toShort
  val PLAYER: Short = (1 << 2).toShort
}

class Space(dispatcher: Dispatcher, broadphase: BroadphaseInterface, solver: ConstraintSolver, config: CollisionConfiguration)
            extends DiscreteDynamicsWorld(dispatcher, broadphase, solver, config){
  setGravity(new Vector3f(0,-9.8f,0))
  var entities = new ArrayBuffer[Entity]()

  setInternalTickCallback(new InternalTickCallback {
   override def internalTick(world: DynamicsWorld, p2: Float): Unit = {
     val dispatcher = world.getDispatcher
     val manifolds = dispatcher.getNumManifolds
     for(i <- 0 to manifolds){
       val manifold = dispatcher.getManifoldByIndexInternal(i)
       var hit = false
       var normal: Vector3f = null
       var j = 0
       while(manifold != null && j < manifold.getNumContacts && !hit){
         val contactpoint = manifold.getContactPoint(j)
         if(contactpoint.getDistance < 0f){
           hit = true
           normal = contactpoint.normalWorldOnB
         }
         j += 1
       }
       if(hit){
         //
       }
     }
   }
  }, null)

  def update(): Unit ={
    stepSimulation(1f/60)
    val deadEntities = new ArrayBuffer[Entity]()
    val iterator = entities.iterator//I use a iterator so that i can remove entities while iterating
    for(entity <- entities){
      entity.update()//Update entity
      if(!entity.isAlive){//Check if entity is dead and should be removed from world
        deadEntities += entity
      }
    }
    //Remove dead entities
    for(dead <- deadEntities){
      removeEntity(dead)
    }
  }

  def removeEntity(entity: Entity): Unit ={
    entities -= entity
    if(entity.getBody != null)
      removeRigidBody(entity.getBody)
  }

  def addEntity(entity: Entity): Unit ={
    entities += entity
    if(entity.getBody != null)
      addRigidBody(entity.getBody, entity.getGroup, entity.getMask)
  }

  def draw(): Unit ={
    for(entity <- entities){
      entity.debugDraw()
    }
  }
}

trait PhysicalObject{
  def onCollision(body: PhysicalObject, userpointer: AnyRef): Unit

  def getMass: Float

  def getGroup: Short

  def getMask: Short

  def getBody: RigidBody
}
