import org.scalatest.Matchers._
import org.scalatest._
import yuzu.RandomData

class RandomDataSpec extends FunSpec {
  override def withFixture(test: NoArgTest) = {
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  describe("A random first name") {
    val testFirstNames = Tuple2(RandomData.firstName, RandomData.firstName)
    val testFirstName = testFirstNames._1
    it("is unique") {
      testFirstName should not equal testFirstNames._2
    }
    it("is a string") {
      testFirstName shouldBe a [String]
    }
    it("is at least 2 characters") {
      testFirstName.length should be >= 2
    }
  }
  describe("A random last name") {
    val testLastNames = Tuple2(RandomData.firstName, RandomData.firstName)
    val testLastName = testLastNames._1
    it("is unique") {
      testLastName should not equal testLastNames._2
    }
    it("is a string") {
      testLastName shouldBe a [String]
    }
    it("is at least 2 characters") {
      testLastName.length should be >= 2
    }
  }
}