package renderer

import java.io.{FileInputStream, File}
import java.nio.ByteBuffer

import de.matthiasmann.twl.utils.PNGDecoder
import de.matthiasmann.twl.utils.PNGDecoder.Format
import org.lwjgl.opengl.GL11

/**
 * Created by atmelfan on 2014-09-23.
 */
class Texture(file: File, param: Int, filter: Int) extends RenderResource{
  var (width, height) = (0, 0)
  var id = GL11.glGenTextures()
  load(file, param, filter)

  def load(file: File, param: Int, filter: Int): Unit ={
    val in = new FileInputStream(file)
    try {
      val decoder = new PNGDecoder(in)

      //System.out.println("width="+decoder.getWidth)
      //System.out.println("height="+decoder.getHeight)

      val buf = ByteBuffer.allocateDirect(4*decoder.getWidth*decoder.getHeight)
      decoder.decode(buf, decoder.getWidth*4, Format.RGBA)
      buf.flip()

      GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
      GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth, decoder.getHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, param)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, param)

      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter)

      width = decoder.getWidth
      height = decoder.getHeight
    }catch{
      case e: Exception => println("Failed to load texture %s: %s".format(file.getPath, e))
    }finally{
      in.close()
    }
  }

  def hotswap(): Unit ={
    destroy()
    load(file, param, filter)
  }

  def bind[T](target: Int = GL11.GL_TEXTURE_2D, unit: Int = 0)(body: => T): T = {
    GL11.glBindTexture(target, id)
    try {
      body
    }finally {
      GL11.glBindTexture(target, 0)
    }
  }

  def destroy(){
    GL11.glDeleteTextures(id)
  }

}
