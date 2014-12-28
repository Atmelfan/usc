package renderer

import java.io.File

/**
 * Created by atmelfan on 2014-10-04.
 */
abstract class Model(file: File) {
  val STATIC = 0
  val ANIMATED = 1

  def isAnimated = false

  def parse(file: File): Unit

  def compile(): Unit

  def draw(frame: Float = 0): Unit


}
