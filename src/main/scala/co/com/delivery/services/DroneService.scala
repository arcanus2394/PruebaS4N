package co.com.delivery.services

import java.util.concurrent.Executors

import co.com.delivery.adapters.InterRWService
import co.com.delivery.dron.entities.{Drone, _}

import scala.concurrent.{ExecutionContext, Future}

// Algebra
sealed trait AlgebraDroneService {
  def changeDroneStatus(order: Order, droneStatus: Drone):Drone
  def deliver(path: Path, drones: Drone, id: Int): Future[Delivered]
}

//Interpretation
sealed trait DroneService extends AlgebraDroneService{

  implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
  override def changeDroneStatus(order: Order, droneStatus: Drone):Drone ={

   val res = order match {
      case A => forward(droneStatus)
      case I => rotateL(droneStatus)
      case D => rotateR(droneStatus)
    }
    val resEither = for{
      x <- res
    } yield x
    resEither.fold(x=>{
      throw new Exception("Error al moverme fuera del grid")
    },right=>right)
  }

  override def deliver(path: Path, drone: Drone, id: Int): Future[Delivered] = {

    //Future(createDelivered(path.path.scanLeft(drone)((acum, deliver)=>address(deliver,acum)).tail))
    Future(createDelivered(path.DeliverList.scanLeft(drone)((acum, deliver)=>address(deliver, acum)).tail, id))
  }

  private def address(deliver: Deliver, drone: Drone):Drone ={
    deliver.OrdersList.foldLeft(drone)((delAcum, order)=>changeDroneStatus(order, delAcum))
  }

  private def createDelivered(list: List[Drone], id: Int):Delivered={
    val del=Delivered(list)
    InterRWService.writeDroneStatus(del,id)
    del
  }

  private def forward(dronePosition: Drone):Either[String,Drone]={


        val x = dronePosition.coord.intX
        val y = dronePosition.coord.intY
        val orientation = dronePosition.orientation
        val id=dronePosition.id

        orientation match {
          case N() => Drone.newDrone(Coord(x,y+1),orientation,id)
          case S() => Drone.newDrone(Coord(x,y-1),orientation,id)
          case E() => Drone.newDrone(Coord(x+1,y),orientation,id)
          case W() => Drone.newDrone(Coord(x-1,y),orientation,id)
        }

  }

  private def rotateL(dronePosition: Drone):Either[String,Drone]={
      val x = dronePosition.coord.intX
      val y = dronePosition.coord.intY
      val orientation = dronePosition.orientation
      val id=dronePosition.id

      orientation match {
        case N() => Drone.newDrone(Coord(x,y),W(),id)
        case E() => Drone.newDrone(Coord(x,y),N(),id)
        case S() => Drone.newDrone(Coord(x,y),E(),id)
        case W() => Drone.newDrone(Coord(x,y),S(),id)
      }
  }

  private def rotateR(dronePosition: Drone):Either[String,Drone]={
      val x = dronePosition.coord.intX
      val y = dronePosition.coord.intY
      val orientation = dronePosition.orientation
      val id=dronePosition.id

      orientation match {
        case N() => Drone.newDrone(new Coord(x,y),E(),id)
        case E() => Drone.newDrone(new Coord(x,y),S(),id)
        case S() => Drone.newDrone(new Coord(x,y),W(),id)
        case W() => Drone.newDrone(new Coord(x,y),N(),id)
      }
  }
}

//trait Object
object DroneService extends DroneService
