import org.jbox2d.common.Vec2

/**
 * Created by atmelfan on 2015-01-03.
 */
class Packets {
  case class entityCreate(var id: Int){

  }

  case class entityUpdate(var id: Int, var position: Vec2, var angle: Float){

  }

  case class entityAnimation(var id: Int, var name: String, typ: Int){

  }


  case class spaceCreate(var id: Int, var gravity: Vec2){

  }

  case class message(var msg: String){

  }

}
