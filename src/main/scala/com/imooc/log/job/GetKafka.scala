package com.imooc.log.job

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object GetKafka {

  def main(args: Array[String]): Unit = {
    val sc=new SparkConf().setMaster("local[2]").setAppName("kafka-stream")
    val ssc=new StreamingContext(sc,Seconds(2))

    val topics=Array("topic1","topic2")
    val kafkaParams=Map[String,Object]("bootstrap.servers"->"zhk-linux:9092",
    "key.deserializer"->classOf[StringDeserializer],
      "value.deserializer"->classOf[StringDeserializer],
      "group.id"->"groupid1",
      "auto.offset.reset"->"latest",
      "enable.auto.commit"->(false:java.lang.Boolean)


    )
    val kafkaStream=KafkaUtils.createDirectStream(ssc,LocationStrategies.PreferConsistent,ConsumerStrategies
      .Subscribe[String,String]
      (topics,
      kafkaParams))


    kafkaStream.map(record=>record.value).flatMap(str=>str.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //kafkaStream.map(record=>record.value).print()
    ssc.start()
    ssc.awaitTermination()

  }


}
