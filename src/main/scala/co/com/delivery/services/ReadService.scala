package co.com.delivery.services

import co.com.delivery.entities._

import scala.concurrent.Future
import scala.io.Source
import scala.util.Try


// Algebra
sealed trait AlgebraReadService {
  def readPath(string: String):Try[Path]
}

// Interpretation
sealed trait InterpetrationReadService extends AlgebraReadService {
  override def readPath(string: String): Try[Path] = {

    val file: List[String] = Source.fromInputStream(getClass.getResourceAsStream(s"$string")).getLines.toList
    val readString = {
      if (file.length <= 10) {
        Try(createPath(file))
      }
      else throw new Exception("El archivo de entrada tenia mas de 10 pedidos")
    }
    readString
  }

  def createPath(list: List[String]): Path = {
    Path(list.map(string => createDeliver(string)))
  }

  def createDeliver(string: String): Deliver = {
    Deliver(string.toList.map(char => Order.newOrder(char)))
  }
}

// Trait Object
object InterpetrationReadService extends InterpetrationReadService