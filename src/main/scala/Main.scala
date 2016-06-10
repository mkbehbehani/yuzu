package yuzu

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.source.DocumentSource
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import com.typesafe.scalalogging.Logger
import me.tongfei.progressbar.ProgressBar
import org.elasticsearch.common.settings.Settings
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

object Main {
  def main(args: Array[String]): Unit = {
    val logger = Logger(LoggerFactory.getLogger("yuzu-logger"))
    implicit val system = ActorSystem("reactive-yuzu")
    implicit val materializer = ActorMaterializer()
    case class Person(docTuple: (Int,String)) extends DocumentSource {
      override def json = docTuple._2
    }
    println("What index would you like to create?")
    var processedItems = 1
    val indexName = readLine()
    println("How many documents do you want to generate?")
    val documentCount = readInt()
    val startTime = System currentTimeMillis()
    val memberRange = Source(processedItems to documentCount)
    val ec = scala.concurrent.ExecutionContext.Implicits.global
    val cores = Runtime.getRuntime.availableProcessors()
    println(s"Utilizing $cores")
    val elasticSearchIndex = indexName
    val elasticsearchURL = ElasticsearchClientUri("elasticsearch://localhost:9300")
    val settings = Settings.settingsBuilder().put("cluster.name", "es_mk1")
    val docsRemaining = documentCount - processedItems
    val progressBar = new ProgressBar("Populating elasticsearch", documentCount)
    progressBar.start()
    val client = ElasticClient.remote(settings.build, elasticsearchURL)
    def reportProgress(count: Int) = {
      processedItems += count
      progressBar.stepBy(count)
      if(processedItems == documentCount){
        logger.info("Population complete.")
        system.terminate()
      }
    }
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
           "username": "$username"}"""})
    val pushToElasticsearch: Seq[(Int,String)] => Unit = jsonObjects => {
      client.execute {
        create index elasticSearchIndex
      }
      client.execute {
        bulk(for (jsonObject <- jsonObjects) yield index into elasticSearchIndex / "fakeperson" id jsonObject._1 doc Person(jsonObject))
      }.await
      reportProgress(jsonObjects.size)
    }
    def jsonGenerationFlow(implicit ec: ExecutionContext): Flow[Int, (Int,String), Unit] = {
      Flow[Int].mapAsyncUnordered(cores)(id => generateJson(id))
    }
    memberRange.via(jsonGenerationFlow(ec)).grouped(10000).runForeach(pushToElasticsearch)
  }
}
