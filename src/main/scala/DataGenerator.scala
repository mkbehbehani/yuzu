package yuzu
import scala.concurrent.{ExecutionContext, Future}

object DataGenerator {
  def generateJson(id:Int)(implicit ec: ExecutionContext): Future[(Int,String)] = Future(id,{
    val first_name = RandomData.firstName
    val last_name = RandomData.lastName
    val streetAddress = RandomData.address
    val city = RandomData.city
    val state = RandomData.state
    val zipCode = RandomData.zipCode
    val phone = RandomData.phone
    val username = RandomData.username(first_name, last_name)
    val email = RandomData.email(first_name, last_name)
    s""" {
           "id": "$id",
           "primary_name": "$first_name $last_name",
           "first_name": "$first_name",
           "last_name": "$last_name",
           "address": "$streetAddress",
           "city": "$city",
           "state": "$state",
           "zip_code": "$zipCode",
           "phone": "$phone",
           "email": "$email",
           "username": "$username"}"""}
  )
}
