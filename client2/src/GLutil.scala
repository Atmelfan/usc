import org.lwjgl.opengl.GL11

/**
 * Created by atmelfan on 2014-09-23.
 */

object GLutil {

  def begin[T](mode: Int)(body: => T): T = {
    GL11.glBegin(mode)
    try {
      body
    }finally {
      GL11.glEnd()
    }

  }

  def push[T]()(body: => T): T = {
    GL11.glPushMatrix()
    try {
      body
    }finally {
      GL11.glPopMatrix()
    }
  }

  def glVertex(x: Float, y: Float, z: Float, s: Float, t: Float): Unit ={
    GL11.glTexCoord2f(s, t); GL11.glVertex3f(x, y, z)
  }

  def glDrawSquare(height: Float, width: Float): Unit ={
    begin(GL11.GL_QUADS){
      glVertex(-1*width, -1*height, 0, 0, 0)
      glVertex( 1*width, -1*height, 0, 1, 0)
      glVertex( 1*width,  1*height, 0, 1, 1)
      glVertex(-1*width,  1*height, 0, 0, 1)
    }
  }
}
