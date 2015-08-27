package renderer

/**
 * Created by atmelfan on 2015-01-03.
 */
trait Model extends RenderResource{
  def draw(frame: Float): Unit

  def draw(animation: ModelAnimation): Unit

  def newAnimation: ModelAnimation

  class Frame {
    var start = 0
    var end = 0
    var framerate = 0
  }

  class ModelAnimation{

    def reset(): Unit ={
    }

    def triggerAnimation(s: String): Unit ={

    }

    def update(): Unit ={

    }

    def getFrame: Int ={
      0
    }
  }
}

