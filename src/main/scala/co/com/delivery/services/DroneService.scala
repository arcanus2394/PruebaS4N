package co.com.delivery.services

import java.util.concurrent.Executors

import co.com.delivery.entities._

import scala.concurrent.{ExecutionContext, Future}

// Algebra
sealed trait AlgebraDroneService {
  def changeDroneStatus(order: Order,droneStatus: Either[String,Drone]):Either[String,Drone]
  def deliver(path: Path,drones: Either[String,Drone],id:Int): Future[Delivered]
}

//Interpretation
sealed trait DroneService extends AlgebraDroneService{
  override def changeDroneStatus(order: Order,droneStatus: Either[String,Drone]):Either[String,Drone] ={

    order match {
      case A() => forward(droneStatus)
      case I() => rotateL(droneStatus)
      case D() => rotateR(droneStatus)
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: ")
    }
  }

  override def deliver(path: Path,drone: Either[String,Drone],id:Int): Future[Delivered] = {
    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
    //Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail))
    Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail,id))
  }


  def createDelivered(list: List[Either[String,Drone]],id:Int):Delivered={
    val del=Delivered(list)
    InterRWService.writeDroneStatus(del,id)
    del
  }

  def address(deliver: Deliver,drone: Either[String,Drone]):Either[String,Drone] ={
    deliver.deliver.foldLeft(drone)((delAcum, order)=>changeDroneStatus(order,delAcum))
  }

  def forward(dronePosition: Either[String,Drone]):Either[String,Drone]={

    dronePosition.fold(l=>
      throw new Exception(s"El dron ya esta por fuera del grid")
      ,tryDrone=>{
        val x = tryDrone.coord.intX
        val y = tryDrone.coord.intY
        val orientation = tryDrone.orientation
        val id=tryDrone.id

        orientation match {
          case N() => Drone.newDrone(Coord(x,y+1),orientation,id)
          case S() => Drone.newDrone(Coord(x,y-1),orientation,id)
          case E() => Drone.newDrone(Coord(x+1,y),orientation,id)
          case W() => Drone.newDrone(Coord(x-1,y),orientation,id)
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
}

//trait Object
object DroneService extends DroneService
