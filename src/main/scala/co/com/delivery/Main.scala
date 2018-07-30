package co.com.delivery

import scala.io.Source
import co.com.delivery.entities._
import co.com.delivery.services.InterpetrationDeliveryService

import scala.util.Try

object Main extends App {
  println("Hello World")
  /*
  val fileStream = getClass.getResourceAsStream("/in/in01.txt")
  val lines: Iterator[String] = Source.fromInputStream(fileStream).getLines
  lines.foreach(line => println(line))
  */
  val ordenA = Try(Order.newOrder('A'))
  val ordenD = Try(Order.newOrder('D'))
  val ordenI = Try(Order.newOrder('I'))
  val ordenIf = Try(Order.newOrder('E'))

  /*
  println(ordenA)
  println(ordenD)
  println(ordenI)
  println(ordenIf)*/

  val orientationN = Try(Orientation.newOrientation('N'))
  val orientationS = Try(Orientation.newOrientation('S'))
  val orientationE = Try(Orientation.newOrientation('E'))
  val orientationW = Try(Orientation.newOrientation('W'))
  val orientationWf = Try(Orientation.newOrientation('x'))

  /*
  println(orientationN)
  println(orientationS)
  println(orientationE)
  println(orientationW)
  println(orientationWf)*/

  val read = InterpetrationDeliveryService.readFile()

}
