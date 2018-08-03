package co.com.delivery.entities

sealed trait Order
sealed trait Orientation

case class I() extends Order
case class A() extends Order
case class D() extends Order

case class Coord(intX: Int,intY: Int)
case class Drone(coord: Coord, orientation: Orientation, id:Int)
case class Deliver(OrdersList: List[Order])
case class Path(DeliverList:List[Deliver])
case class Delivered(DroneList: List[Drone])

object Drone{
  def newDrone(coord: Coord,orientation: Orientation,id:Int):Either[String,Drone] ={
    val x = coord.intX
    val y = coord.intY
    if((x>10||y>10)||(x<(-10)||y<(-10))){
      Left(s"El dron con id:$id se salio del grid en la posicion($x,$y)")
    } else {
      Right(Drone(coord,orientation,id))
    }
  }
}

object Orientation {
  def newOrientation(c:Char):Orientation ={
    c.toUpper match {
      case 'N' => N()
      case 'S' => S()
      case 'E' => E()
      case 'W' => W()
    }
  }
}

object Order {
  def newOrder(c:Char):Order ={
    c.toUpper match {
      case 'A' => A()
      case 'D' => D()
      case 'I' => I()
      case _ => throw new IllegalArgumentException(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}

case class N() extends Orientation{
  override def toString(): String ={
    "dirección Norte"
  }
}
case class S() extends Orientation{
  override def toString(): String ={
    "dirección Sur"
  }
}
case class E() extends Orientation{
  override def toString(): String ={
    "dirección Este"
  }
}
case class W() extends Orientation{
  override def toString(): String ={
    "dirección Oeste"
  }
}



