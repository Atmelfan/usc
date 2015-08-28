/**
 * Created by atmelfan on 2015-08-14.
 */
class CommandSay extends Command{
  override def execute(args: Array[String], space: Space, actor: Entity, target: Entity): Unit = {
    if(args.length > 1)
      USC_client.client.chat.add(args(1))
  }

  override def help: String = "Prints [text] to chat"

  override def helpShort: String = "say [text]"
}
