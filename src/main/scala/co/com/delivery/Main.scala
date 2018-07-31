package co.com.delivery

import scala.io.Source
import co.com.delivery.entities._
import co.com.delivery.services.InterpetrationDeliveryService

import scala.util.Try

object Main extends App {

  val read = InterpetrationDeliveryService.deliver("/in/in01.txt")

}
