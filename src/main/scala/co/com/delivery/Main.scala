package co.com.delivery

import scala.io.Source

object Main extends App{
  //println("Hello World")
  val fileStream = getClass.getResourceAsStream("/in/in01.txt")
  val lines: Iterator[String] = Source.fromInputStream(fileStream).getLines
  lines.foreach(line => println(line))

}
