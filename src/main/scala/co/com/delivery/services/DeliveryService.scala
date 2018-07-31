package co.com.delivery.services

import java.io.{File, InputStream, PrintWriter}

import co.com.delivery.entities._

import scala.io.Source

// Algebra
sealed trait AlgebraDeliveryService {
  def deliver(string: String)
}

// Interpretation
sealed trait InterpetrationDeliveryService extends AlgebraDeliveryService {

  override def deliver(string: String) = {
    val droneStatus = new Drone(0,0,N())
    val res: Either[String, Drone] = validateLength(Source.fromInputStream(readFile(string)).getLines.toList)
      .map(x=>x.foldLeft(droneStatus)((acum, string) => deliver(string,acum)))
    res
  }

  def readFile(string: String): InputStream ={
    getClass.getResourceAsStream(s"$string")
  }
  //s"$string"

  def validateLength(file:List[String]):Either[String, List[String]]={

    val readString :Either[String,List[String]] = {
      if(file.length<=10){
        Right(file)}
      else Left(s"El archivo supera el numero de ordenes maximo")
    }
    readString
  }

  def changeDroneStatus(order: Order,droneStatus: Drone):Drone ={

    order match {
      case A() => forward(droneStatus)
      case I() => rotateL(droneStatus)
      case D() => rotateR(droneStatus)
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: ")
    }

  }

  def forward(dronePosition: Drone):Drone={

    val x = dronePosition.x
    val y = dronePosition.y
    val orientation = dronePosition.orientation

    orientation match {
      case N() => new Drone(x,y+1,orientation)
      case S() => new Drone(x,y-1,orientation)
      case E() => new Drone(x+1,y,orientation)
      case W() => new Drone(x-1,y,orientation)
    }

  }

  def rotateL(dronePosition: Drone):Drone={
    val x = dronePosition.x
    val y = dronePosition.y
    val orientation = dronePosition.orientation

    orientation match {
      case N() => new Drone(x,y,W())
      case E() => new Drone(x,y,N())
      case S() => new Drone(x,y,E())
      case W() => new Drone(x,y,S())
    }
  }

  def rotateR(dronePosition: Drone):Drone={
    val x = dronePosition.x
    val y = dronePosition.y
    val orientation = dronePosition.orientation

    orientation match {
      case N() => new Drone(x,y,E())
      case E() => new Drone(x,y,S())
      case S() => new Drone(x,y,W())
      case W() => new Drone(x,y,N())
    }
  }

  def deliver(string: String, acum: Drone):Drone ={

    val res =string.toList.foldLeft(acum)((delAcum,char)=>changeDroneStatus(Order.newOrder(char),delAcum))
    println(res)
    val out= res.x.toString
    writeDroneStatus(res,out)
    res
  }

  def writeDroneStatus(dronePosition: Drone, out: String): Unit ={
    val x = dronePosition.x.toString
    val y = dronePosition.y.toString

    val pw = new PrintWriter(new File("out1.txt"))
    pw.write(s"== Reporte de entregas ==\n($x,$y) direccion")
    pw.close
  }

}

// Trait Object
object InterpetrationDeliveryService extends InterpetrationDeliveryService

/*
def positionRecurs(lista:List[String],drones_Status: List[DronePosition]): Unit ={
  Tail recursivity search
}*/

/*
val res: Either[String, List[DronePosition]] = validateLength(Source.fromInputStream(fileStream).getLines.toList)
  .map(x=>x
    .map(y=>y.toList
      .foldLeft(droneStatus)((z, c) => changeDroneStatus(Order.newOrder(c),z))))
*/
