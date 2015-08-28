/**
 * Created by atmelfan on 2015-08-15.
 */
class Item {


  def name = "MissingNo"

  def itemType = ItemType.Normal

  def use(): Unit ={

  }

  def attack(): Unit ={

  }
}

object ItemType extends Enumeration {
  type ItemType = Value
  val Normal, Resource, Unit = ItemType
}
