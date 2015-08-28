/**
 * Created by atmelfan on 2015-08-14.
 */
trait Command {

  def execute(args: Array[String], space: Space, actor: Entity, target: Entity): Unit

  def help = ""

  def helpShort = ""

}
