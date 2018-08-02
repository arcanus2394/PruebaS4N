package modelling.dominio

import java.util.concurrent.Executors
import co.com.delivery.entities._
import co.com.delivery.services.{InterpetrationDeliveryService, InterpetrationReadService}
import org.scalatest.FunSuite
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class ModellingSuite extends FunSuite {

  test("Order creation"){
    val res = Order.newOrder('A')
    assert(res==A())
  }

  test("Dron creation"){
    val coord = Coord(0,0)
    val drone = Drone.newDrone(coord,N(),1)
    assert(drone==Drone(Coord(0,0),N(),1))
  }

  test("Test move forward"){
    val trydrone = Right(new Drone(Coord(0,0),N(),1))
    val res = Order.newOrder('A')
    val x = InterpetrationDeliveryService.changeDroneStatus(res,trydrone)
    assert(x==Right(Drone(Coord(0,1),N(),1)))
  }

  test("Test turn rigth"){
    val trydrone = Right(new Drone(Coord(0,0),N(),1))
    val res = Order.newOrder('D')
    val x = InterpetrationDeliveryService.changeDroneStatus(res,trydrone)
    assert(x==Right(Drone(Coord(0,0),E(),1)))
  }

  test("Test turn left"){
    val trydrone = Right(new Drone(Coord(0,0),N(),1))
    val res = Order.newOrder('I')
    val x = InterpetrationDeliveryService.changeDroneStatus(res,trydrone)
    assert(x==Right(Drone(Coord(0,0),W(),1)))
  }

  test("Test read one file"){
    val read = InterpetrationReadService.readPath("/in/in01.txt")
    assert(read == Right(Path(List(Deliver(List(A(), A(), A(), A(), I(), A(), A(), D())), Deliver(List(D(), D(), A(), I(), A(), D())), Deliver(List(A(), A(), I(), A(), D(), A(), D()))))))}


  test("Deliver Test"){
    val path = InterpetrationReadService.readPath("/in/in01.txt")

    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
    val delivers = InterpetrationDeliveryService.deliver(path.fold[Path](s => {
      Path(List.empty)
    }
      , i => {
        i
      }),Right(Drone(Coord(0,0),N(),1)))

    val res = Await.result(delivers,10 seconds)
    assert(res==Delivered(List(Right(Drone(Coord(-2,4),N(),1)), Right(Drone(Coord(-1,3),S(),1)), Right(Drone(Coord(0,0),W(),1)))))
  }
}
