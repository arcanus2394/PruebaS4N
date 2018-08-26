package co.com.delivery.adapters

import java.io._

import co.com.delivery.dron.entities.Order

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
  getfile(string).map(x=>
      if (x.length <= 10) {
        Try(createPath(x))
      } else {Try(throw new Exception("El archivo de entrada tenia mas de 10 pedidos"))}
    ).flatten
  }

  private def getfile(str:String):Try[List[String]]={
    Try(Source.fromFile(s"src/main/resources/$str").getLines().toList)
  }

  override def writeDroneStatus(droneEntregas: Delivered,id:Int) ={
    val pw = new PrintWriter(new File(s"out$id.txt"))
    pw.write(s"== Reporte de entregas ==")
    droneEntregas.DroneList.map(r=>{
      val intx = r.coord.intX
      val inty = r.coord.intY
      val orientation = r.orientation.toString
      pw.write(s"\n($intx,$inty) $orientation")})
    pw.close
  }

  private def createPath(list: List[String]): Path = {
    Path(list.map(string => createDeliver(string)))
  }

  private def createDeliver(string: String): Deliver = {
    Deliver(string.toList.map(char => Order.newOrder(char)))
  }
}

//Trait Object
object InterRWService extends InterRWService

