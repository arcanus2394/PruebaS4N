package modelling.dominio

import co.com.delivery.entities.{Drone, N}
import co.com.delivery.services.InterpetrationDeliveryService
import org.scalatest.FunSuite

class ModellingSuite extends FunSuite {

  test("Prueba creacion dron"){
    //val order = new Order('A')
  }

  test("Prueba InterpetrationDeliveryService"){
    InterpetrationDeliveryService.deliver("/in/in01.txt",new Drone(0,0,N(),1))
  }
}
