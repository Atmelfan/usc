import java.io._
import java.util.regex.Pattern
import javax.vecmath.{Matrix4f, Vector3f}


import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2014-09-23.
 */
class ModelSMD(file: File) {

  val pattern = Pattern.compile("\\s+(?=(([^'\"]*['\"]){2})*[^'\"]*$)")
  val nodes = ArrayBuffer[SMDnode]()
  val bones = mutable.Map[Int, ArrayBuffer[SMDbone]]()
  val vertices = mutable.Map[String, ArrayBuffer[SMDvertex]]()
  parse(file)

  class SMDnode{
    var id = 0
    var name = ""
    var parent = -1

    def this(s: String) = {
      this()
      val args = pattern.split(s)
      id = args(0).toInt
      name = args(1).replace("\"", "")
      parent = args(0).toInt
    }
  }

  class SMDbone{
    var id: Int = 0
    val matrix = new Matrix4f()

    def this(s: String) = {
      this()
      val args = pattern.split(s)
      id = args(0).toInt
      matrix.setTranslation(new Vector3f(args(1).toFloat, args(2).toFloat, args(3).toFloat))
      matrix.rotX(args(4).toFloat)
      matrix.rotY(args(5).toFloat)
      matrix.rotZ(args(6).toFloat)
    }
  }

  class SMDvertex{
    var parent = 0
    var position: (Float, Float, Float) = (0, 0 ,0)
    var normal: (Float, Float, Float) = (0, 0 ,0)
    var texcoords: (Float, Float) = (0, 0)

    def this(s: String) = {
      this()
      val args = pattern.split(s)
      parent = args(0).toInt
      position = (args(1).toFloat, args(2).toFloat, args(3).toFloat)
      normal = (args(4).toFloat, args(5).toFloat, args(6).toFloat)
      texcoords = (args(7).toFloat, args(8).toFloat)
    }
  }

  def draw(frame: Int = 0): Unit ={

  }

  def compile(): Unit ={
    for(material <- vertices.keys){
      //Load material
      //1 vertex = 1 int + 8 floats = 1*4 + 8*4 bytes
      
    }
  }

  def parseNodes(reader: BufferedReader): Unit ={
    //println("ModelSMD: Parsing nodes...")
    var line = reader.readLine()
    while (line != null && !line.equalsIgnoreCase("end")){
      line = line.trim
      if(!line.startsWith("#") && !line.isEmpty){
        val args = pattern.split(line)
        //println(args.mkString(","))
        if(args.length >= 3){
          nodes += new SMDnode(line)
        }
      }
      line = reader.readLine()
    }
  }

  def parseBones(reader: BufferedReader): Unit ={
    //println("ModelSMD: Parsing bones...")
    var line = reader.readLine()
    var time = 0
    while (line != null && !line.equalsIgnoreCase("end")){
      line = line.trim
      if(!line.startsWith("#") && !line.isEmpty){
        val args = pattern.split(line)
        if(line.startsWith("time") && args.length >= 2){
          time = args(1).toInt
        }else if(args.length >= 7){
          bones.getOrElseUpdate(time, new ArrayBuffer[SMDbone]()) += new SMDbone(line)
        }
      }
      line = reader.readLine()
    }
  }

  def parseTriangles(reader: BufferedReader): Unit ={
    //println("ModelSMD: Parsing triangles...")
    var line = reader.readLine()
    var material = "default.smt"
    while (line != null && !line.equalsIgnoreCase("end")){
      line = line.trim
      if(!line.startsWith("#") && !line.isEmpty){
        val args = pattern.split(line)
        if(args.length >= 9){
          vertices.getOrElseUpdate(material, new ArrayBuffer[SMDvertex]()) += new SMDvertex(line)
        }else{
          material = args(0)
        }
      }
      line = reader.readLine()
    }
  }

  def parse(file: File): Unit ={
    val in = new BufferedReader(new FileReader(file))
    var line = ""
    println("ModelSMD: Loading \"" + file.getName + "\"")
    try {
      line = in.readLine().trim
      while (line != null){
        //println(">" + line)
        if(line.startsWith("nodes")){
          parseNodes(in)
        }else if(line.startsWith("skeleton")){
          parseBones(in)
        }else if(line.startsWith("triangles")){
          parseTriangles(in)
        }else if(line.startsWith("version")){
          //println("ModelSMD: Header ")
        }else if(!line.isEmpty && !line.startsWith("#")){
          throw new Exception("What?")
        }
        line = in.readLine()
      }
    }catch{
      case e: IOException =>
        println("ModelSMD: Unexpected error: " + e)
    }
    println("ModelSMD: Done!")
  }


}
