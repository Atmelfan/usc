import java.io.{FileFilter, File}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2015-08-28.
 */
object Server {
  private val serverdir = new File("servers")

  def getServers: Array[Server] ={
    val servs = serverdir.listFiles(new FileFilter {
      override def accept(file: File): Boolean = {

        file.isDirectory
      }
    })
    val servers = new ArrayBuffer[Server]()
    servs.foreach{ root =>
      println("Reading " + root + "...")
      val s = new Server(root)
      s.parse()
      servers += s
    }
    servers.toArray
  }

}

class Server(root: File) {
  var name = "MissingNo"
  var description = ""
  var ip = ""
  var local = false

  def getRoot = root

  def parse(): Unit ={
    try {
      val t = new File(root, "server.gmd")
      println(t)
      val gmd = new Gmd(t)
      gmd.ifExists("name", 1){ tag =>
        name = tag.getString(1)
      }

      gmd.ifExists("description", 1){ tag =>
        description = tag.getString(1)
      }

      gmd.ifExists("ip", 1){ tag =>
        ip = tag.getString(1)
      }

      gmd.ifExists("local", 1){ tag =>
        local = tag.getBoolean(1)
      }
    }catch {
      case e: Exception =>
        e.printStackTrace()
        name = "[INVALID]"
    }

  }
}
