package co.com.delivery.entities

sealed trait Order

object Order {
  def newOrder(c:Char):Order ={
    c.toUpper match {
      case 'A' => A()
      case 'D' => D()
      case 'I' => I()
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}

case class I() extends Order
case class A() extends Order
case class D() extends Order

sealed trait Orientation

object Orientation {
  def newOrientation(c:Char):Orientation ={
    c.toUpper match {
      case 'N' => North()
      case 'S' => South()
      case 'E' => East()
      case 'W' => West()
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}

case class North() extends Orientation
case class South() extends Orientation
case class East() extends Orientation
case class West() extends Orientation


case class Coord(intX: Int,intY: Int)
/* todo init coordinate
object Coord{
  def newCoord(intX:Int,intY: Int):Coord={
    intX match {
      case intX<=10 => 2
      case 'S' => South()
      case 'E' => East()
      case 'W' => West()
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}*/

sealed trait Position{
  val coord:Coord
  val orientation:Orientation
}

case class DroneStatus(coord: Coord, orientation:Orientation) extends Position