import java.io._
import java.util.regex.Pattern

import scala.collection.mutable.ArrayBuffer

/**
 * Created by atmelfan on 2014-12-19.
 */
class Gmd extends Gmdtag(Array()){

  def this(s: String){
    this()
    this.parse(new File(s))
  }

  def this(f: File){
    this()
    this.parse(f)
  }

  def parse(file: File): Unit ={

    args = Array(file.getName)
    val reader = new BufferedReader(new FileReader(file))
    println("Parsing \"" + file.getName + "\"...")
    parse(reader)
    //println("Done!")
    reader.close()
  }

  def dump(file: File): Unit ={
    val writer = new BufferedWriter(new FileWriter(file))
    for(child <- getChildren){
      child.dump(writer, 0)
    }
    writer.close()
  }

}

class Gmdtag(var args: Array[String]){
  val pattern = Pattern.compile("\\s+(?=(([^'\"]*['\"]){2})*[^'\"]*$)")
  //println("Args: " + args.mkString(","))
  private var children:ArrayBuffer[Gmdtag] = new ArrayBuffer[Gmdtag]()

  def getChildren = children

  def getName = args(0)

  def add(tag: Gmdtag): Unit ={
    children += tag
  }

  def add(s: String): Gmdtag ={
    val tag = new Gmdtag(pattern.split(s.substring(0, s.length)))
    children += tag
    tag
  }

  def apply(i: Int): Gmdtag  = children(i)

  def apply(s: String): Gmdtag  = {
    for(tag <- children){
      if(tag.getName == s)
        return tag
    }
    null
  }

  def ifExists(s: String, minArgs: Int = 0)(f: Gmdtag => Unit): Unit = {
    val t = apply(s)
    if(t != null && t.argCount >= (minArgs + 1)){
      f(t)
    }
  }

  def set(i: Int, s: String): Unit ={
    args(i) = s
  }

  def get(i: Int): String ={
    args(i)
  }

  def setString(i: Int, s: String): Unit ={
    args(i) = "\"" + s + "\""
  }

  def getString(i: Int): String ={
    args(i).replace("\"", "")
  }

  def setInt(i: Int, x: Int): Unit ={
    args(i) = x.toString
  }

  def getInt(i: Int): Int ={
    args(i).toInt
  }

  def setFloat(i: Int, x: Float): Unit ={
    args(i) = x.toString
  }

  def getFloat(i: Int): Float ={
    args(i).toFloat
  }

  def setBoolean(i: Int, x: Boolean): Unit ={
    args(i) = x.toString
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

  def dump(writer: BufferedWriter, level: Int = 0): Unit ={

    if(getChildren.isEmpty){
      println("t!")
      writer.write("\t"*level + args.mkString(" ") + "\n")
    }else{
      println("g!")
      writer.write("\t"*level + args.mkString(" ") + ":\n")
      for(child <- getChildren){
        child.dump(writer, level + 1)
      }
      writer.write("\t"*level + "end\n")
    }
  }
}