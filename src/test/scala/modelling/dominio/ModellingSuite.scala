package modelling.dominio

import co.com.delivery.services.InterpetrationDeliveryService
import org.scalatest.FunSuite

class ModellingSuite extends FunSuite {
  test("Prueba InterpetrationDeliveryService"){
    InterpetrationDeliveryService.deliver("/in/in01.txt")
  }
}
