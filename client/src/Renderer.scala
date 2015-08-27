import java.awt.Font
import java.io.File

import org.jbox2d.common.Vec2
import org.lwjgl.LWJGLException
import org.lwjgl.opengl._
import org.newdawn.slick.{Color, TrueTypeFont}
import renderer.{GLutil, RenderResource}


class Renderer {
  var checkresources = false
  fixLwjgl()

  val wfont = new Font("Liberation Mono", Font.PLAIN, 24)
  var font: TrueTypeFont = null

  def drawText(x: Float, y: Float, s: String, color: Color = Color.green): Unit ={
    font.drawString(x,y,s,1f,1f)
  }

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
    GL11.glEnable(GL11.GL_TEXTURE_2D) // Enable Texture Mapping
    GL11.glClearColor(0f,0f,0f,0f); // Black Background
    GL11.glDisable(GL11.GL_DITHER)
    GL11.glDepthFunc(GL11.GL_LESS) // Depth function less or equal
    GL11.glEnable(GL11.GL_NORMALIZE) // calculated normals when scaling
    GL11.glEnable(GL11.GL_CULL_FACE) // prevent render of back surface
    GL11.glEnable(GL11.GL_BLEND) // Enabled blending
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA) // selects blending method
    GL11.glEnable(GL11.GL_ALPHA_TEST) // allows alpha channels or transperancy
    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f) // sets aplha function
    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST) // High quality visuals
    GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST) //  Really Nice Perspective Calculations
    GL11.glShadeModel(GL11.GL_SMOOTH) // Enable Smooth Shading'
    font = new TrueTypeFont(wfont, false)
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
