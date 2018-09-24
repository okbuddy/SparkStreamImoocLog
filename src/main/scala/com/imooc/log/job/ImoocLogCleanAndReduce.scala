package com.imooc.log.job

import com.imooc.log.bean.{ClickLog, CourseClickCount, CourseRefererCount}
import com.imooc.log.dao.{CourseClickCountDao, CourseRefererCountDao}
import com.imooc.log.utils.DateUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import scala.collection.mutable.ListBuffer

object ImoocLogCleanAndReduce {

  def main(args: Array[String]): Unit = {
    val sc = new SparkConf().setMaster("local[2]").setAppName("kafka-stream")
    val ssc = new StreamingContext(sc, Seconds(2))

    val topics = Array("topic1", "topic2")
    val kafkaParams = Map[String, Object]("bootstrap.servers" -> "zhk-linux:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "groupid1",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)


    )
    val kafkaStream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies
      .Subscribe[String, String]
      (topics,
        kafkaParams))
    //    145.234.156.45	2018-09-12 15-17-01	"GET class/131.html HTTP/1.1"	404	baidu?q=hive
    val cleanStream = kafkaStream.map(
      line => {
        val arr = line.value().split("\t")
        val date = DateUtils.reformatDate(arr(1))
        val url = arr(2).split(" ")(1)
        var courseId = 0
        if (url.startsWith("class")) {
          val endUrl = url.split("/")(1)
          courseId = endUrl.substring(0, endUrl.indexOf(".")).toInt
        }
        ClickLog(arr(0), date, courseId, arr(3).toInt, arr(4))
      }
    ).filter(_.courseId != 0)


    cleanStream.map(log => (log.date.substring(0, 8) + "_" + log.courseId, 1)).reduceByKey(_ + _).foreachRDD(
      rdd => {
        rdd.foreachPartition(
          partition => {
            val list = new ListBuffer[CourseClickCount]
            partition.foreach(
              row => {
                list.append(CourseClickCount(row._1, row._2))
              }
            )
            CourseClickCountDao.save(list)
          }
        )
      }
    )
  //baidu?q=dd
    cleanStream.map(log => {

      val end=log.referer.indexOf("?")
      val date=log.date.substring(0, 8)
      if(end == -1){
        (date,1)
      }else{

        (date + "_" + log.referer.substring(0,end), 1)
      }

    }).reduceByKey(_ + _).foreachRDD(
      rdd => {
        rdd.foreachPartition(
          partition => {
            val list = new ListBuffer[CourseRefererCount]
            partition.foreach(
              row => {
                list.append(CourseRefererCount(row._1, row._2))
              }
            )
            CourseRefererCountDao.saveReferer(list)
          }
        )
      }
    )




    ssc.start()
    ssc.awaitTermination()

  }


}
