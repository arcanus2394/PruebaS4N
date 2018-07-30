package co.com.delivery.services
import java.nio.file.Path

import co.com.delivery.entities._

import scala.concurrent.Future
import scala.io.Source

// Algebra
sealed trait AlgebraDeliveryService {
  def readFile():Either[String, List[String]]
  def move(ord:List[String])
}

// Interpretation
sealed trait InterpetrationDeliveryService extends AlgebraDeliveryService {

  def validateLength(file:List[String]):Either[String, List[String]]={

    val readString :Either[String,List[String]] = {
      if(file.length<=10){
        Right(file)}
      else Left(s"El archivo supera el numero de ordenes maximo")
    }
    readString
  }

  override def readFile():Either[String, List[String]] = {
    val fileStream = getClass.getResourceAsStream("/in/in01.txt")
    val lines: List[String]  = Source.fromInputStream(fileStream).getLines.toList
    val linesEither: Either[String, List[String]] = validateLength(lines)
    val interm = linesEither.map(x=>move(x))

    println(s"el valor de interm  es $interm")
    linesEither
  }

  override def move(ord:List[String])= {
    val drone = new DroneStatus(new Coord(0,0),North())
    val rs: List[String] = ord.foldLeft()(x => changeDroneStatus(x))
    /*
    println(rs)
    println(s"el valor del Lista es: $ord")
    println(s"el valor del drone es: $drone")*/
  }


  def changeDroneStatus(order: String):DroneStatus ={
    val chain=order.toList
    val res = chain.foreach(x=>x match {
      case 'A' => forward(x)
      case 'I' => rotateL(x)
      case 'D' => rotateR(x)
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: ")
    })

  }

  /*
  def forward(x:Char):DroneStatus={

    val newdDrone: DroneStatus = drone.orientation match {
      case North() => new DroneStatus(new Coord(x,y+1),orientation)
      case South() => new DroneStatus(new Coord(x,y-1),orientation)
      case East() => new DroneStatus(new Coord(x+1,y),orientation)
      case West() => new DroneStatus(new Coord(x-1,y),orientation)
    }
    //val resDron = DroneStatus(newCoord,drone.orientation)
    println(s"el estado del drone es $newdDrone")
    newdDrone
  }*/

  def rotateL(x:Char)={

  }

  def rotateR(x:Char)={

  }

  /*
  override def path(list: List[String]): List[Future[String]] = {
    val listaFuturos: List[Future[String]] = list.map(x=>Future(x))
    listaFuturos
  }*/
}

// Trait Object
object InterpetrationDeliveryService extends InterpetrationDeliveryService

