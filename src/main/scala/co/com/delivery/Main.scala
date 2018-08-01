package co.com.delivery

import java.util.concurrent.Executors

import scala.io.Source
import co.com.delivery.entities._
import co.com.delivery.services.InterpetrationDeliveryService

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.Try

object Main extends App {

  implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  val path: Either[String, Path] = InterpetrationDeliveryService.readPath("/in/in01.txt")

  val delivers: Future[Delivered] = InterpetrationDeliveryService.deliver(path.fold[Path](s => {
    Path(List.empty)
  }
    , i => {
      i
    }),Try(Drone(Coord(0,0),N(),1)))


  delivers.map(x=>InterpetrationDeliveryService.writeDroneStatus(x))
}
