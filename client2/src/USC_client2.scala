import java.io.File

/**
 * Created by atmelfan on 2015-08-28.
 */
object USC_client2 {
  var client: USC_client2 = null
}

class USC_client2(server: File, ip: String) extends Thread{
  USC_client2.client = this
  var resources: ResourceManager = new ResourceManager(server)

  override def start(): Unit ={
    //Connect to server before returning
    super.start()
  }
  override def run(): Unit = {

  }
}