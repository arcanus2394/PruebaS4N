package modelling.dominio

import java.util.concurrent.Executors

import co.com.delivery.entities._
import co.com.delivery.services.InterpetrationDeliveryService
import org.scalatest.FunSuite

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.reflect.internal.Trees
import scala.util.{Success, Try}

class ModellingSuite extends FunSuite {

  test("Creacion una Orden"){
    val res = Order.newOrder('A')
    assert(res==A())
  }

  test("Prueba de Movimiento hacia la orientacion"){
    val trydrone = Try(new Drone(Coord(0,0),N(),1))
    val res = Order.newOrder('A')
    val x = InterpetrationDeliveryService.changeDroneStatus(res,trydrone)
    assert(x==Success(Drone(Coord(0,1),N(),1)))
  }

  test("Prueba giro derecha"){
    val trydrone = Try(new Drone(Coord(0,0),N(),1))
    val res = Order.newOrder('D')
    val x = InterpetrationDeliveryService.changeDroneStatus(res,trydrone)
    assert(x==Success(Drone(Coord(0,0),E(),1)))
  }

  test("Prueba giro izquierda"){
    val trydrone = Try(new Drone(Coord(0,0),N(),1))
    val res = Order.newOrder('I')
    val x = InterpetrationDeliveryService.changeDroneStatus(res,trydrone)
    assert(x==Success(Drone(Coord(0,0),W(),1)))
  }

  test("Test read one file"){
    val read = InterpetrationDeliveryService.readPath("/in/in01.txt")
    assert(read == Right(Path(List(Deliver(List(A(), A(), A(), A(), I(), A(), A(), D())), Deliver(List(D(), D(), A(), I(), A(), D())), Deliver(List(A(), A(), I(), A(), D(), A(), D()))))))}


  test("Prueba Deliver"){
    val path = InterpetrationDeliveryService.readPath("/in/in01.txt")

    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
    val delivers = InterpetrationDeliveryService.deliver(path.fold[Path](s => {
      Path(List.empty)
    }
      , i => {
        i
      }),Try(Drone(Coord(0,0),N(),1)))

    val res = Await.result(delivers,10 seconds)
    assert(res==Delivered(List(Success(Drone(Coord(-2,4),N(),1)), Success(Drone(Coord(-1,3),S(),1)), Success(Drone(Coord(0,0),W(),1)))))
  }
}
