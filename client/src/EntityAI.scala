import org.jbox2d.callbacks.RayCastCallback
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Fixture
import org.jbox2d.pooling.arrays.Vec2Array
import org.lwjgl.opengl.GL11
import renderer.GLutil

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2015-08-26.
 */
class EntityAI(space: Space) extends EntityShip(space){


  class Path{
    var progress = 0
    var path = new ArrayBuffer[Vec2]()

    def calculatePath(pos: Vec2, target: Vec2): Unit ={
      path.clear()
      pathPoint(pos, target)
      path += target
    }

    def calculatePathArray(pos: Vec2, targets: Array[Vec2]): Unit ={
      path.clear()
      pathPoint(pos, targets(0))
      path += targets(0)
      for(i <- 1 to targets.length){
        pathPoint(targets(i-1), targets(i))
        path += targets(i)
      }
    }

    def pathPoint(pos: Vec2, target: Vec2): Unit ={
      if(path.length > 100)
        return


      var col: Vec2 = null
      space.world.raycast(new RayCastCallback {
        override def reportFixture(p1: Fixture, point: Vec2, normal: Vec2, fraction: Float): Float = {
          col = point.add(normal.mul(10))
          0
        }
      }, pos, target)
      if(col != null){
        path += col
        pathPoint(col, target)
      }
    }

    def execute(): Unit ={
      if(isComplete)
          return

      if(getDistanceTo(path(0)) < 1)
        path.remove(0)

      if(isComplete)
        return

      goto(path(0), 100)
    }

    def drawPath(): Unit ={
      GLutil.begin(GL11.GL_LINE_STRIP){
        GL11.glColor3f(1,0,0)
        GL11.glVertex2f(getPosition.x, getPosition.y)
        path.foreach{ point =>
          GL11.glVertex2f(point.x, point.y)
        }
        GL11.glColor3f(1,1,1)
      }
    }

    def isComplete = progress >= path.length
  }

  def goto(p: Vec2, s: Float): Unit ={
    val t = p.sub(getPosition)
    t.normalize()
    t.mulLocal(100)
    val a = Math.atan2(t.y, t.x)
    setRotation(a.toFloat)
    t.mulLocal(s)
    getBody.applyForceToCenter(t)
  }

  var path: Path = new Path

  override def post_load(props: Gmdtag): Unit = {
    props.ifExists("target"){ tag =>
      val t = new Vec2(tag.getFloat(1), tag.getFloat(2))
      println("Calculating target to " + t)
      path.calculatePath(getPosition, t)
    }
    super.post_load(props)
  }

  override def update(): Unit = {
    path.execute()
    super.update()
  }

  override def draw(): Unit = {
    path.drawPath()
    super.draw()
  }
}
