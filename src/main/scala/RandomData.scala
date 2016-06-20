package yuzu

import java.sql.Timestamp
import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.Date

import yuzu.SourceData._

object RandomData {
  val randomGenerator = scala.util.Random
  val firstNameCount = firstNames.length
  val lastNameCount = lastNames.length
  val streetCount = streets.length
  val cityCount = cities.length
  val stateCount = states.length
  val zipCodesCount = zipCodes.length
  val emailDomainsCount = emailDomains.length
  val beginTime = Timestamp.valueOf("1920-01-01 00:00:00").getTime
  val endTime = Timestamp.valueOf("2015-10-31 00:58:00").getTime
  val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
  val timeRange = endTime - beginTime + 1

  def healthPlanId = randomGenerator nextInt 99999 toString

  def firstName:String = {
    val selection = randomGenerator.nextInt(firstNameCount)
    firstNames(selection)
  }

  def lastName:String = {
    val selection = randomGenerator.nextInt(lastNameCount)
    lastNames(selection)
  }

  def street = {
    try {
      val selection = randomGenerator.nextInt(streetCount)
      streets(selection)
    }catch{
      case e: Exception => println("Unable to generate last name")
    }
  }
  def city = {
    try {
      val selection = randomGenerator.nextInt(cityCount)
      cities(selection)
    }catch{
      case e: Exception => println("Unable to generate last name")
    }
  }
  def state = {
    try {
      val selection = randomGenerator.nextInt(stateCount)
      states(selection)
    }catch{
      case e: Exception => println("Unable to generate last name")
    }
  }
  def address:String = {
    val number = randomGenerator.nextInt(999)
    number.toString + " " + street
  }

  def zipCode = {
    try{
      val selection = randomGenerator.nextInt(zipCodesCount)
      zipCodes(selection)
    }catch{
      case e: Exception => println("Unable to generate zipcode")
    }
  }
  def phone = {
    val num1 = (randomGenerator.nextInt(7) + 1) * 100 + (randomGenerator.nextInt(8) * 10) + randomGenerator.nextInt(8)
    val num2 = randomGenerator.nextInt(743)
    val num3 = randomGenerator.nextInt(10000)
    val threeDigitFormat = new DecimalFormat("000")
    val fourDigitFormat = new DecimalFormat("0000")
    threeDigitFormat.format(num1) + threeDigitFormat.format(num2) + fourDigitFormat.format(num3)
  }
  def email(firstName: String, lastName: String) = {
    try{
      val selection = randomGenerator.nextInt(emailDomainsCount)
      val emailDomain = emailDomains(selection)
      val emailNumbers = randomGenerator.nextInt(99999).toString
      firstName.take(1).toLowerCase + lastName.toLowerCase + emailNumbers + "@" + emailDomain
    }catch{
      case e: Exception => println("Unable to generate email")
    }
  }
  def username(firstName: String, lastName: String) = {
    try{
      val randomNumbers = randomGenerator.nextInt(99999).toString
      firstName.take(1).toLowerCase + lastName.toLowerCase + randomNumbers
    }catch{
      case e: Exception => println("Unable to generate username")
    }
  }
  def dateOfBirth = {
    try {
      val randomTimeBetweenTwoDates = beginTime + (Math.random() * timeRange).toLong
      val randomDate = new Date(randomTimeBetweenTwoDates)
      dateFormat.format(randomDate)
    }
    catch {
      case e: Exception => println("Unable to generate date of birth")
    }
  }
}
