import org.jbox2d.collision.shapes.{ChainShape, CircleShape, PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{Fixture, Body, BodyDef, FixtureDef}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2015-08-09.
 */
object ShapeGmd {
  def shapeFromGmd(gmd: Gmdtag): Shape = {
    if(gmd == null)
      return null
    gmd.get(1).toLowerCase match {
      case "circ" | "circle" =>
        val shape = new CircleShape()
        shape.setRadius(gmd.getInt(2))
        shape.m_p.set(gmd.getInt(3),gmd.getInt(4))
        shape
      case "rect" | "rectangle" =>
        val shape = new PolygonShape()
        shape.setAsBox(gmd.getInt(2), gmd.getInt(3))
        shape
      case "poly" | "polygon" =>
        val shape = new PolygonShape()
        val vertices = new ArrayBuffer[Vec2]()
        for(vertex <- gmd.getChildren){
          vertices += new Vec2(vertex.getFloat(0), vertex.getFloat(1))
        }
        shape.set(vertices.toArray, vertices.size)
        shape
      case "chain" =>
        val shape = new ChainShape
        val vertices = new ArrayBuffer[Vec2]()
        for(vertex <- gmd.getChildren){
          vertices += new Vec2(vertex.getFloat(0), vertex.getFloat(1))
        }
        shape.createChain(vertices.toArray, vertices.size)
        shape
      case "loop" =>
        val shape = new ChainShape
        val vertices = new ArrayBuffer[Vec2]()
        for(vertex <- gmd.getChildren){
          vertices += new Vec2(vertex.getFloat(0), vertex.getFloat(1))
        }
        shape.createLoop(vertices.toArray, vertices.size)
        shape
    }
  }

  def fixtureFromGmd(gmd: Gmdtag): FixtureDef = {
    val fixture = new FixtureDef()
    fixture.density = gmd("density").getFloat(1)
    fixture.friction = gmd("friction").getFloat(1)
    fixture.restitution = gmd("restitution").getFloat(1)
    fixture.shape = shapeFromGmd(gmd("shape"))
    fixture
  }


}
