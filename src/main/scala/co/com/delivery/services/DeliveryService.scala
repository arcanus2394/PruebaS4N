package co.com.delivery.services

import java.io.{File, InputStream, PrintWriter}
import java.util.concurrent.Executors

import co.com.delivery.entities._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success, Try}

// Algebra
sealed trait AlgebraDeliveryService {
  //def deliver(string: String,drone: Drone): Either[String, Drone]
  def readPath(string: String):Either[String, Path]
  def deliver(path: Path,drones: Try[Drone]): Future[Delivered]
  def writeDroneStatus(droneEntregas: Delivered)
}

// Interpretation
sealed trait InterpetrationDeliveryService extends AlgebraDeliveryService {

  override def readPath(string: String):Either[String, Path]={

    val file: List[String] = Source.fromInputStream(getClass.getResourceAsStream(s"$string")).getLines.toList
    val readString = {
      if(file.length<=10){
        Right(createPath(file))}
      else Left(s"El archivo supera el numero de ordenes maximo")
    }
    readString
  }

  def createPath(list: List[String]):Path ={
    Path(list.map(string=>createDeliver(string)))
  }

  def createDeliver(string: String):Deliver ={
    Deliver(string.toList.map(char=>Order.newOrder(char)))
  }

  def createDelivered(list: List[Try[Drone]]):Delivered={
    Delivered(list)
  }

  override def deliver(path: Path,drone: Try[Drone]): Future[Delivered] = {
    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
    //Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail))
    Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail))
  }

  def address(deliver: Deliver,drone: Try[Drone]):Try[Drone] ={
    deliver.deliver.foldLeft(drone)((delAcum, order)=>changeDroneStatus(order,delAcum))
  }

  def changeDroneStatus(order: Order,droneStatus: Try[Drone]):Try[Drone] ={

    order match {
      case A() => forward(droneStatus)
      case I() => rotateL(droneStatus)
      case D() => rotateR(droneStatus)
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: ")
    }
  }

/*
  def verifyForward(drone:Try[Drone]):Drone ={

    for{
      v <- drone.map(x=>x.coord.intX)
    } yield
  }
*/
  def forward(dronePosition: Try[Drone]):Try[Drone]={

    dronePosition.map(tryDrone=>{
      val x = tryDrone.coord.intX
      val y = tryDrone.coord.intY
      val orientation = tryDrone.orientation
      val id=tryDrone.id

      orientation match {
        case N() => Drone(Coord(x,y+1),orientation,id)
        case S() => Drone(Coord(x,y-1),orientation,id)
        case E() => Drone(Coord(x+1,y),orientation,id)
        case W() => Drone(Coord(x-1,y),orientation,id)
      }
    })

  }

  def rotateL(dronePosition: Try[Drone]):Try[Drone]={
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

  def rotateR(dronePosition: Try[Drone]):Try[Drone]={
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
    droneEntregas.delivered.map(x=>x.isSuccess).map(x=>{

      val pw = new PrintWriter(new File(s"out$x.txt"))
      pw.write(s"== Reporte de entregas ==")
      droneEntregas.delivered.map(tryDrone=>tryDrone.map(drone=>{
        {
          val intx = drone.coord.intX
          val inty = drone.coord.intY
          val orientation = drone.orientation.toString
          pw.write(s"\n($intx,$inty) $orientation"
          )}
      })
      )
      pw.close
    })
  }

}

// Trait Object
object InterpetrationDeliveryService extends InterpetrationDeliveryService