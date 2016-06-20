import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.Matchers._
import yuzu.DataGenerator
import scala.concurrent.ExecutionContext.Implicits.global

class DataGeneratorSpec extends FunSpec with ScalaFutures {
  describe("Generated JSON") {
    val testJson = DataGenerator.generateJson(3).futureValue
    describe("the first item in the return tuple") {
      it("matches the id passed into the generateJson function") {
        assert(testJson._1 == 3)
      }
    }
    describe("the second item in the return tuple") {
      it("matches the template") {
        testJson._2 should include("first_name")
      }
    }
  }
}
