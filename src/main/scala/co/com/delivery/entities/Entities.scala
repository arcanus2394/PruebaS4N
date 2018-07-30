package co.com.delivery.entities

sealed trait Order
case class I(s:String) extends Order
case class A(s:String) extends Order
case class D(s:String) extends Order

sealed trait Accion
case class MovementI(order: Order) extends Accion
case class MovementA(order: Order) extends Accion
case class MovementD(order: Order) extends Accion

sealed trait Orientation
case class North(s:String) extends Orientation
case class South(s:String) extends Orientation
case class East(s:String) extends Orientation
case class West(s:String) extends Orientation

sealed trait Horizon
case class OrientationN(orientation: Orientation) extends Horizon
case class OrientationS(orientation: Orientation) extends Horizon
case class OrientationE(orientation: Orientation) extends Horizon
case class OrientationW(orientation: Orientation) extends Horizon

case class Coord(intX: Int,intY: Int)

case class Position(coord: Coord,horizon: Horizon)