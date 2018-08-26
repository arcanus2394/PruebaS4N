package co.com.delivery.dron.entities

import co.com.delivery.dron.entities.Drone.Coord

case class Drone(coord: Coord, orientation: Orientation, id:Int)

object Drone {

  case class Coord(intX: Int,intY: Int)
  case class Deliver(OrdersList: List[Order])
  case class Path(DeliverList:List[Deliver])
  case class Delivered(DroneList: List[Drone])

  def newDrone(coord: Coord,orientation: Orientation,id:Int) : Either[String, Drone] = {
    val x = coord.intX
    val y = coord.intY
    if((x > 10 || y > 10) || ( x < (-10) || y < (-10))){
      Left(s"El dron con id:$id se salio del grid en la posicion($x,$y)")
    } else {
      Right(Drone(coord,orientation,id))
    }
  }
}
