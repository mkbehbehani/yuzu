import org.scalatest._
import org.scalatest.Matchers._
import yuzu.RandomData

class RandomDataSpec extends FunSpec {
  override def withFixture(test: NoArgTest) = {
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  describe("A random first name") {
    val testFirstName1 = RandomData.firstName
    val testFirstName2 = RandomData.firstName
    it("is unique") {
      testFirstName1 should not equal testFirstName2
    }
    it("is a string") {
      testFirstName1 shouldBe a [String]
    }
    it("is at least 2 characters") {
      testFirstName1.length should be >= 2
    }
  }
  describe("A random last name") {
    val testLastName1 = RandomData.lastName
    val testLastName2 = RandomData.lastName
    it("is unique") {
      testLastName1 should not equal testLastName2
    }
    it("is a string") {
      testLastName1 shouldBe a [String]
    }
    it("is at least 2 characters") {
      testLastName1.length should be >= 2
    }
  }
  describe("A random address") {
    val testAddress1 = RandomData.address
    val testAddress2 = RandomData.address
    it("is unique") {
      testAddress1 should not equal testAddress2
    }
    it("is a string") {
      testAddress1 shouldBe a [String]
    }
    it("is at least 2 characters") {
      testAddress1.length should be >= 2
    }
  }
}