package co.com.delivery.dron.entities

sealed trait Orientation


object Orientation {


  case object N extends Orientation{
    override def toString(): String ={
      "dirección Norte"
    }
  }
  case object S extends Orientation{
    override def toString(): String ={
      "dirección Sur"
    }
  }
  case object E extends Orientation{
    override def toString(): String ={
      "dirección Este"
    }
  }
  case object W extends Orientation{
    override def toString(): String ={
      "dirección Oeste"
    }
  }

  def newOrientation(c:Char):Orientation ={
    c.toUpper match {
      case 'N' => N
      case 'S' => S
      case 'E' => E
      case 'W' => W
    }
  }
}