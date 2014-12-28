import java.io.{FileReader, BufferedReader, File}
import java.util.regex.Pattern

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2014-12-19.
 */
class Gmd extends Gmdtag(Array()){
  def parse(file: File): Unit ={
    args = Array(file.getName)
    val reader = new BufferedReader(new FileReader(file))
    println("Parsing \"" + file.getName + "\"...")
    parse(reader)
    println("Done!")
  }

}

class Gmdtag(var args: Array[String]){
  val pattern = Pattern.compile("\\s+(?=(([^'\"]*['\"]){2})*[^'\"]*$)")
  //println("Args: " + args.mkString(","))
  var children:ArrayBuffer[Gmdtag] = new ArrayBuffer[Gmdtag]()

  def getName = args(0)

  def apply(i: Int): Gmdtag  = children(i)

  def apply(s: String): Gmdtag  = {
    for(tag <- children){
      if(tag.getName == s)
        return tag
    }
    null
  }

  def get(i: Int): String ={
    args(i)
  }

  def getString(i: Int): String ={
    args(i).replace("\"", "")
  }

  def getInt(i: Int): Int ={
    args(i).toInt
  }

  def getFloat(i: Int): Float ={
    args(i).toFloat
  }

  def getBoolean(i: Int): Boolean ={
    args(i).toBoolean
  }

  def argCount = args.length

  def tagCount = children.length

  def parse(reader: BufferedReader): Unit ={
    var s = reader.readLine()
    while(s != null){
      s = s.trim
      if(s.startsWith("end")){
        //println("end " + getName)
        return
      }else if(s.startsWith("#") || s.isEmpty){
      }else if(s.endsWith(":")){
        val tags = new Gmdtag(pattern.split(s.substring(0, s.length-1)))
        tags.parse(reader)
        children += tags
      }else{
        val tag = new Gmdtag(pattern.split(s))
        children += tag
      }
      s = reader.readLine()
    }

  }
}