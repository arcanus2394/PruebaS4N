package co.com.delivery

import java.util.concurrent.Executors
import co.com.delivery.entities._
import co.com.delivery.services.{InterpetrationDeliveryService, InterpetrationReadService}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

object Main extends App {

  implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  InterpetrationReadService.readPath("/in/in01.txt")
    .map(path => InterpetrationDeliveryService.deliver(path, Right(Drone(Coord(0, 0), N(), 1)))
    .map(deli => InterpetrationDeliveryService.writeDroneStatus(deli)))

}
