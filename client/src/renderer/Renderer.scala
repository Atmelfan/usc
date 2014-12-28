package renderer

import java.io.File
import javax.vecmath.{Matrix4f, Vector3f}

import org.lwjgl.util.vector
import org.lwjgl.{BufferUtils, LWJGLException}
import org.lwjgl.opengl._

import scala.collection
import scala.collection.parallel.mutable

/**
 * Created by atmelfan on 2014-09-23.
 */

object GLutil {
  val renderer = new Renderer()

  def getRenderer = renderer

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

class Camera {
  var position = new Vector3f()

  def set(vector: Vector3f): Unit ={
    position.set(vector)
  }

  def translate(vector: Vector3f): Unit ={
    position.add(vector)
  }

  def translateWorld(): Unit ={
    GL11.glTranslatef(position.x, position.y, position.z)
  }
}

class Renderer {
  fixLwjgl()

  def init(width: Int, height: Int, title: String): Unit ={
    try {
      Display.setDisplayMode(new DisplayMode(width, height))
      Display.setTitle(title)
      Display.setResizable(true)
      Display.create()
      System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION))
    } catch {
      case lwjgle: LWJGLException => lwjgle.printStackTrace()
        System.exit(-1)
    }
    resize(Display.getWidth, Display.getHeight)
    GL11.glEnable(GL11.GL_COLOR_MATERIAL)
    //GL11.glEnable(GL11.GL_DEPTH_TEST)
    GL11.glEnable(GL11.GL_TEXTURE_2D) // Enable Texture Mapping
    GL11.glDisable(GL11.GL_DITHER)
    GL11.glDepthFunc(GL11.GL_LESS) // Depth function less or equal
    GL11.glEnable(GL11.GL_NORMALIZE) // calculated normals when scaling
    //GL11.glEnable(GL11.GL_CULL_FACE) // prevent render of back surface
    GL11.glEnable(GL11.GL_BLEND) // Enabled blending
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA) // selects blending method
    GL11.glEnable(GL11.GL_ALPHA_TEST) // allows alpha channels or transparency
    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST) // High quality visuals
    GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST) //  Really Nice Perspective Calculations
    GL11.glShadeModel(GL11.GL_SMOOTH) // Enable Smooth Shading
    GL11.glClearColor(0f, 0f, 0f, 0f)
  }

  def resize(width: Int, height: Int): Unit = {
    GL11.glMatrixMode(GL11.GL_PROJECTION)
    GL11.glLoadIdentity()
    GL11.glViewport(0,0, width, height)
    GL11.glOrtho(-width/2, width/2, -height/2, height/2, 100, -1)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    println("Screen resized to " + width + "x" + height + "px!")
  }

  def destroy(): Unit = {
    Display.destroy()
  }

  def update(): Boolean = {
    if(Display.wasResized()){
      resize(Display.getWidth, Display.getHeight)
    }

    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
    GL11.glLoadIdentity()
    !Display.isCloseRequested
  }

  val textures = collection.mutable.Map[String, Texture]()
  def getTexture(name: String, param: Int = GL11.GL_REPEAT, filter: Int = GL11.GL_LINEAR): Texture = {
    textures.getOrElseUpdate(name, new Texture(new File(name), param, filter))
  }

  def fixLwjgl(): Unit = {
    val os = getOsNatives
    System.setProperty("org.lwjgl.librarypath", new File(os).getAbsolutePath)
  }

  def getOsNatives: String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("win")){
      "libraries/natives/windows"
    }else if (os.contains("nix") || os.contains("nux")){
      "libraries/natives/linux"
    }else if (os.contains("mac")){
      "libraries/natives/macosx"
    }else if (os.contains("sol") || os.contains("sun")){
      "libraries/natives/solaris"
    }else{
      println("I have no idea what operating system this is! Hopefully linux natives work...")
      "libraries/natives/linux"
    }
  }

}
