package co.com.delivery.services

import java.util.concurrent.Executors

import co.com.delivery.adapters.InterRWService
import co.com.delivery.dron.entities.{Coord, Delivered, Drone, N}

import scala.concurrent.{ExecutionContext, Future}

// Algebra
sealed trait AlgebraOrchestrator {
  def deliverOrders(numDrones:Int)
}

//Interpretation
sealed trait DeliverService extends AlgebraOrchestrator{
  override def deliverOrders(numDrones: Int): Unit = {
    //implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(numDrones))
    //val inFilenames: List[String] = Range(1,9).map(n => s"/in/in0$n.txt").toList
    val inFilesTest = List("/in/in01.txt","/in/in02.txt","/in/in03.txt","/in/in04.txt","/in/in05.txt",
      "/in/in06.txt","/in/in07.txt","/in/in08.txt","/in/in09.txt","/in/in10.txt",
      "/in/in11.txt","/in/in12.txt","/in/in13.txt","/in/in14.txt","/in/in15.txt",
      "/in/in16.txt","/in/in17.txt","/in/in18.txt","/in/in19.txt","/in/in20.txt")
    val idstupla= inFilesTest.zip(Range(1,numDrones+1))
    val fileList: List[Future[Delivered]] = idstupla.map(file=>InterRWService.readPath(file._1)
      .map(path=>DroneService.deliver(path, Drone(Coord(0, 0), N(), 1), file._2)))
      .map(z=>Future.fromTry(z).flatten)
  }
}

//Trait Object
object DeliverService extends DeliverService