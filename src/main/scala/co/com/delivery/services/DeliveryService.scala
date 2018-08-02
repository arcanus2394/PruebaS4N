package co.com.delivery.services

import java.io.{File, InputStream, PrintWriter}
import java.util.concurrent.Executors

import co.com.delivery.entities._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success, Try}

// Algebra
sealed trait AlgebraDeliveryService {
  def deliver(path: Path,drones: Either[String,Drone]): Future[Delivered]
  def writeDroneStatus(droneEntregas: Delivered)
}

// Interpretation
sealed trait InterpetrationDeliveryService extends AlgebraDeliveryService {

  def createDelivered(list: List[Either[String,Drone]]):Delivered={
    Delivered(list)
  }

  override def deliver(path: Path,drone: Either[String,Drone]): Future[Delivered] = {
    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
    //Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail))
    Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail))
  }

  def address(deliver: Deliver,drone: Either[String,Drone]):Either[String,Drone] ={
    deliver.deliver.foldLeft(drone)((delAcum, order)=>changeDroneStatus(order,delAcum))
  }

  def changeDroneStatus(order: Order,droneStatus: Either[String,Drone]):Either[String,Drone] ={

    order match {
      case A() => forward(droneStatus)
      case I() => rotateL(droneStatus)
      case D() => rotateR(droneStatus)
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: ")
    }
  }

  def forward(dronePosition: Either[String,Drone]):Either[String,Drone]={

    dronePosition.map(tryDrone=>{
      val x = tryDrone.coord.intX
      val y = tryDrone.coord.intY
      val orientation = tryDrone.orientation
      val id=tryDrone.id

      orientation match {
        case N() => Drone.newDrone(Coord(x,y+1),orientation,id)
        case S() => Drone(Coord(x,y-1),orientation,id)
        case E() => Drone(Coord(x+1,y),orientation,id)
        case W() => Drone(Coord(x-1,y),orientation,id)
      }
    })
  }

  def rotateL(dronePosition: Either[String,Drone]):Either[String,Drone]={
    dronePosition.map(tryDrone=>{
      val x = tryDrone.coord.intX
      val y = tryDrone.coord.intY
      val orientation = tryDrone.orientation
      val id=tryDrone.id

      orientation match {
        case N() => Drone(Coord(x,y),W(),id)
        case E() => Drone(Coord(x,y),N(),id)
        case S() => Drone(Coord(x,y),E(),id)
        case W() => Drone(Coord(x,y),S(),id)
      }
    })
  }

  def rotateR(dronePosition: Either[String,Drone]):Either[String,Drone]={
    dronePosition.map(tryDrone=>{
      val x = tryDrone.coord.intX
      val y = tryDrone.coord.intY
      val orientation = tryDrone.orientation
      val id=tryDrone.id

      orientation match {
        case N() => Drone(new Coord(x,y),E(),id)
        case E() => Drone(new Coord(x,y),S(),id)
        case S() => Drone(new Coord(x,y),W(),id)
        case W() => Drone(new Coord(x,y),N(),id)
      }
    })

  }

  override def writeDroneStatus(droneEntregas: Delivered) ={
    droneEntregas.delivered.map(x=>x.isRight).map(x=>{
      val pw = new PrintWriter(new File(s"out$x.txt"))
      pw.write(s"== Reporte de entregas ==")
      droneEntregas.delivered.map(tryDrone=>tryDrone.map(drone=>{
        {
          val intx = drone.coord.intX
          val inty = drone.coord.intY
          val orientation = drone.orientation.toString
          pw.write(s"\n($intx,$inty) $orientation"
          )}
      }))
      pw.close
    })
  }

}

// Trait Object
object InterpetrationDeliveryService extends InterpetrationDeliveryService