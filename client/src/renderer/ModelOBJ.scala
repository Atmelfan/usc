package renderer

import java.io._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2014-09-30.
 */
class ModelOBJ(file: File) {

  val vertices  = new ArrayBuffer[Array[Float]]()
  val texcoords = new ArrayBuffer[Array[Float]]()
  val normals   = new ArrayBuffer[Array[Float]]()
  val faces = mutable.Map[String, Array[Int]]()


  def parse(file: File): Unit ={
    val in = new BufferedReader(new FileReader(file))
    var line = ""
    try {
      line = in.readLine()
      while (line != null){
        if(line.startsWith("#")){

        }else if(line.startsWith("v")){

        }else if(line.startsWith("f")){

        }else if(line.startsWith("vt")){

        }else if(line.startsWith("vn")){

        }



        line = in.readLine()
      }
    }
  }

  def draw(): Unit ={

  }
}
