package co.com.delivery.dron.entities

sealed trait Order

object Order {

  case object I extends Order
  case object A extends Order
  case object D extends Order

  def newOrder(c:Char):Order ={
    c.toUpper match {
      case 'A' => A
      case 'D' => D
      case 'I' => I
      case _ => throw new IllegalArgumentException(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}




