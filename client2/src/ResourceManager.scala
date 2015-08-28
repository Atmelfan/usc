import java.io.File


/**
 * Created by atmelfan on 2015-08-09.
 */
trait Resource {
  def name: String

  def reload()

  def destroy()
}

case class ResourceType(dir: String)

object ResourceType {
  object Texture extends ResourceType("textures")
  object Sprite extends ResourceType("sprites")
  object Item extends ResourceType("items")
  object Setting extends ResourceType("settings")
  object Particle extends ResourceType("particles")
}

class ResourceManager(server: File) {
  assert(server.isDirectory, "Server '%s' is not a directory!".format(server.getPath))

  var resources = new File("resources")

  var loadedResources = Map[String, Resource]()

  def findFile(name: String, resourcetype: ResourceType): File ={
    var r = new File(server, resourcetype.dir + File.separator + name)
    //println(r.getPath)
    if(!r.exists()){
      r = new File(resources, resourcetype.dir + File.separator + name)
      //println(r.getPath)
      if(!r.exists()){
        System.err.println("File '" + resourcetype.dir + File.separator + name + "' exist in neither 'resources/' or '" + server.getPath +"'!")
      }
    }
    r
  }

  def getResource[T <: Resource](name: String, resourcetype: ResourceType)(body: (String) => T): T = {
    val s = resourcetype.dir + ':' + name
    var tmp = loadedResources.getOrElse(s, null)
    if(tmp == null){
      println("[ResourceManager] Loading resource '" + resourcetype.dir + ':' + name + "'")
      tmp = body(findFile(name, resourcetype).getPath)
      loadedResources += s -> tmp
    }
    tmp.asInstanceOf[T]
  }

//  def getResource[T <: Resource](s: String)(body: (String) => T): T = {
//    var tmp = loadedResources.getOrElse(s, null)
//    if(tmp == null){
//      tmp = body(s)
//      loadedResources += s -> tmp
//    }
//    tmp.asInstanceOf[T]
//  }

  def reload(): Unit ={
    var i = 1
    println("[ResourceManager] Reloading resources...")
    loadedResources.values.foreach{ resource =>
      println("\tReloading %s (%d/%d)".format(resource.name, i, loadedResources.size))
      resource.reload()
      i += 1
    }
    println("[ResourceManager] Done!")
  }

  def destroy(): Unit ={
    println("[ResourceManager] Destroying resources...")
    loadedResources.values.foreach{ resource =>
      resource.destroy()
    }
    println("[ResourceManager] Done!")
  }

}
