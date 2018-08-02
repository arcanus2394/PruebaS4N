package co.com.delivery.services

import java.io._

import co.com.delivery.entities.{Deliver, Delivered, Order, Path}

import scala.io.Source
import scala.util.Try

//Algebra
sealed trait AlgRWService{
  def readPath(string: String):Try[Path]
  def writeDroneStatus(droneEntregas: Delivered,id:Int)
}

//Interpretation
sealed trait InterRWService extends AlgRWService{
  override def readPath(string: String):Try[Path] = {

    val file: List[String] = Source.fromInputStream(getClass.getResourceAsStream(s"$string")).getLines.toList
    val readString = {
      if (file.length <= 10) {
        Try(createPath(file))
      }
      else throw new Exception("El archivo de entrada tenia mas de 10 pedidos")
    }
    readString
  }

  override def writeDroneStatus(droneEntregas: Delivered,id:Int) ={
    val pw = new PrintWriter(new File(s"out$id.txt"))
    droneEntregas.delivered.map(x=> x.fold(l=>"El Dron se salio del grid"
      ,r=>{
        pw.write(s"== Reporte de entregas ==")
        droneEntregas.delivered.map(tryDrone=>tryDrone.map(drone=>{
          {
            val intx = drone.coord.intX
            val inty = drone.coord.intY
            val orientation = drone.orientation.toString
            pw.write(s"\n($intx,$inty) $orientation"
            )}
        }))
        pw.close
      }))
    droneEntregas
  }

  def createPath(list: List[String]): Path = {
    Path(list.map(string => createDeliver(string)))
  }

  def createDeliver(string: String): Deliver = {
    Deliver(string.toList.map(char => Order.newOrder(char)))
  }
}

//Trait Object
object InterRWService extends InterRWService

