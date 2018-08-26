package modelling.dominio

import java.util.concurrent.Executors

import co.com.delivery.adapters.InterRWService
import co.com.delivery.dron.entities._
import co.com.delivery.services._
import org.scalatest.FunSuite

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Success, Try}

class ModellingSuite extends FunSuite {

  test("Order creation"){
    val res = Order.newOrder('A')
    assert(res==A())
  }

  test("Drone creation"){
    val coord = Coord(0,0)
    val drone = Drone.newDrone(coord,N(),1)
    assert(drone==Right(Drone(Coord(0,0),N(),1)))
  }

  test("Drone creation failure"){
    val coord = Coord(11,0)
    val drone = Drone.newDrone(coord,N(),1)
    assert(drone.isLeft==true)
  }

  test("Test move forward"){
    val trydrone = new Drone(Coord(0,0),N(),1)
    val res = Order.newOrder('A')
    val x = DroneService.changeDroneStatus(res, trydrone)
    assert(x==Drone(Coord(0,1),N(),1))
  }

  test("Test turn rigth"){
    val trydrone = new Drone(Coord(0,0),N(),1)
    val res = Order.newOrder('D')
    val x = DroneService.changeDroneStatus(res, trydrone)
    assert(x==Drone(Coord(0,0),E(),1))
  }

  test("Test turn left"){
    val trydrone = new Drone(Coord(0,0),N(),1)
    val res = Order.newOrder('I')
    val x = DroneService.changeDroneStatus(res, trydrone)
    assert(x==Drone(Coord(0,0),W(),1))
  }

  test("Test read one file"){
    val read = InterRWService.readPath("/in/in01.txt")
    assert(read == Success(Path(List(Deliver(List(A(), A(), A(), A(), I(), A(), A(), D())), Deliver(List(D(), D(), A(), I(), A(), D())), Deliver(List(A(), A(), I(), A(), D(), A(), D()))))))
  }

  test("Test read one file non existent") {
    val read = InterRWService.readPath("/in/in00.txt")
    assert(read.isFailure)
  }

  test("Test read one file con un caracter diferente a los especificados para las ordenes"){
    val read = InterRWService.readPath("/in/in01fail.txt")
    assert(read.isFailure)
  }

  test("Deliver Test para obtener el Futuro de delivered(lista de drones que hicieron entrega)"){
    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

    val x: Future[Delivered] = Future.fromTry(InterRWService.readPath("/in/in01.txt")
      .map(path => DroneService.deliver(path, Drone(Coord(0, 0), N(), 1), 1))).flatten
    val res = Await.result(x, 10 seconds)

    assert(res==Delivered(List(Drone(Coord(-2,4),N(),1), Drone(Coord(-1,3),S(),1), Drone(Coord(0,0),W(),1))))
  }

  test("Prueba Drones multiples"){
    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
    val inFilenames: List[String] = Range(1,3).map(n => s"/in/in0$n.txt").toList
    val idstupla= inFilenames.zip(Range(1,3))
    println(idstupla)
    val fileList: List[Future[Delivered]] = idstupla.map(file=>InterRWService.readPath(file._1)
      .map(path=>DroneService.deliver(path, Drone(Coord(0, 0), N(), 1), file._2)))
      .map(z=>Future.fromTry(z).flatten)
    val res: List[Delivered] = fileList.map(x=>Await.result(x,10 seconds))
    assert(res.size==2)
  }

  /*
  test("Prueba 20 drones con uno de ellos fallido, se entregan 19 pedidos"){
    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
    //val inFilenames: List[String] = Range(1,9).map(n => s"/in/in0$n.txt").toList
    val inFilesTest = List("/in/in00.txt","/in/in02.txt","/in/in03.txt","/in/in04.txt","/in/in05.txt",
      "/in/in06.txt","/in/in07.txt","/in/in08.txt","/in/in09.txt","/in/in10.txt",
      "/in/in11.txt","/in/in12.txt","/in/in13.txt","/in/in14.txt","/in/in15.txt",
      "/in/in16.txt","/in/in17.txt","/in/in18.txt","/in/in19.txt","/in/in20.txt")
    val idstupla= inFilesTest.zip(Range(1,20+1))
    val fileList: List[Future[Delivered]] = idstupla.map(file=>InterRWService.readPath(file._1)
      .map(path=>DroneService.deliver(path, Drone(Coord(0, 0), N(), 1), file._2)))
      .map(z=>Future.fromTry(z).flatten)
    val res: List[Delivered] = fileList.map(x=>Await.result(x,10 seconds))
    assert(res.size==19)
  }*/

}
