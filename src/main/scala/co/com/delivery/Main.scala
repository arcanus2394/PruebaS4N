package co.com.delivery


import java.util.concurrent.Executors

import co.com.delivery.entities._
import co.com.delivery.services.{DroneService, InterRWService}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


object Main extends App {

  implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
  val inFilenames: List[String] = Range(1,9).map(n => s"/in/in0$n.txt").toList
  val idstupla= inFilenames.zip(Range(1,9))
  println(idstupla)
  val fileList: List[Future[Delivered]] = idstupla.map(file=>InterRWService.readPath(file._1)
    .map(path=>DroneService.deliver(path, Right(Drone(Coord(0, 0), N(), 1)),file._2)))
    .map(z=>Future.fromTry(z).flatten)

   // .map(trypath=>trypath.fold(failure=>{throw new Exception("Error en los archivos de entrada")}
   //   , successPath=>InterpetrationDeliveryService.deliver(successPath, Right(Drone(Coord(0, 0), N(), 1)),file._2)))

}
