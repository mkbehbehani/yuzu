import org.scalatest._

class DataGeneratorSpec extends FunSpec {
  val ec = scala.concurrent.ExecutionContext.Implicits.global

  describe("A random address") {
    val testJson = DataGenerator.generateJson(3)
    println(testJson)
  }
}
