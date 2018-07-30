package co.com.delivery.entities

sealed trait Order
case class I(s:String) extends Order
case class A(s:String) extends Order
case class D(s:String) extends Order

sealed trait Accion
case class MovementI(order: Order) extends Accion
case class MovementA(order: Order) extends Accion
case class MovementD(order: Order) extends Accion