package renderer

/**
 * Created by atmelfan on 2014-09-23.
 */
trait RenderResource {
  
  def hotswap(): Unit

  def destroy(): Unit

}
