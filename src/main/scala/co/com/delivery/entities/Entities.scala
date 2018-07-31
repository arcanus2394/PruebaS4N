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
      case 'N' => N()
      case 'S' => S()
      case 'E' => E()
      case 'W' => W()
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}

case class N() extends Orientation
case class S() extends Orientation
case class E() extends Orientation
case class W() extends Orientation


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
case class Drone(x:Int, y:Int, orientation: Orientation)
case class Delivered(drone: Drone)
object Delivered{

}

