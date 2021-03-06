package yuzu

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.source.DocumentSource
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import me.tongfei.progressbar.ProgressBar
import org.elasticsearch.common.settings.Settings
import org.slf4j.LoggerFactory
import yuzu.DataGenerator.generateJson
import scala.concurrent.ExecutionContext

object Main {
  def main(args: Array[String]): Unit = {
    val logger = Logger(LoggerFactory.getLogger("yuzu-logger"))
    val config =  ConfigFactory.parseFile(new File("./yuzu.conf"))
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
    logger.info(s"Utilizing $cores CPU cores")
    val elasticSearchIndex = indexName
    val elasticsearchURL = ElasticsearchClientUri("elasticsearch://" + config.getString("cluster.location"))
    val settings = Settings.settingsBuilder().put("cluster.name", config.getString("cluster.name"))
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
    val pushToElasticsearch: Seq[(Int,String)] => Unit = jsonObjects => {
      client.execute {
        create index elasticSearchIndex
      }
      client.execute {
        bulk(for (jsonObject <- jsonObjects) yield index into elasticSearchIndex / "fakeperson" id jsonObject._1 doc Person(jsonObject))
      }
      reportProgress(jsonObjects.size)
    }
    def jsonGenerationFlow(implicit ec: ExecutionContext): Flow[Int, (Int,String), Unit] = {
      Flow[Int].mapAsyncUnordered(cores)(id => generateJson(id))
    }
    memberRange.via(jsonGenerationFlow(ec)).grouped(config.getInt("batch-size")).runForeach(pushToElasticsearch)
  }
}
