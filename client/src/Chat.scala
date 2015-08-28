import org.lwjgl.opengl.{Display, GL11}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2015-08-14.
 */
class Chat(renderer: Renderer){
  case class ChatObject(s: String, var life: Int = 30*20)

  var chat = ArrayBuffer[ChatObject]()

  def add(s: String): Unit ={
    chat.insert(0,new ChatObject(s))
  }

  def update(): Unit ={
    chat = chat.take(100)
    chat.foreach{ o =>
      o.life = Math.max(0, o.life - 1)
    }
  }

  def draw(): Unit ={
    var i = 0
    val c = chat.take(32).takeWhile{ obj =>
      obj.life > 1
    }

    c.foreach{ s =>
      if(s.life < 100){
        val a = s.life.toFloat/200f
        GL11.glColor4f(0,1,0, a)
      }else{
        GL11.glColor4f(0,1,0, 0.5f)
      }
      renderer.drawText(-Display.getWidth/2, -Display.getHeight/2 + i*24, s.s)
      i += 1
    }
    GL11.glColor4f(1,1,1,1)

  }
}
