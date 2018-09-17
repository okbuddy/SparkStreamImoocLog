package com.imooc.log.utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes

object HbaseUtils {
  var admin:HBaseAdmin=_
  val config=new Configuration()

  {
    config.set("hbase.rootdir","hdfs://zhk-linux:9000/hbase")
    config.set("hbase.zookeeper.quorum","zhk-linux:2181")
    try {
      val admin=ConnectionFactory.createConnection(config).getAdmin
    } catch{
      case e:Exception=>e.printStackTrace()

    }
  }

  def getTable(name:String):HTable={
    var table:HTable=null
    try{
      table=new HTable(config,name)

    }catch {
      case e:Exception=>e.printStackTrace()
    }
    table

  }
  def save(tableName:String,row:String,cf:String,qualifer:String,clickCount:Long)={
    val table=getTable(tableName)
    val put=new Put(row.getBytes())
    put.addColumn(cf.getBytes(),qualifer.getBytes(),Bytes.toBytes(clickCount))
    table.put(put)
  }

  def main(args: Array[String]): Unit = {
    val tableName = "imooc_course_clickcount"
    val rowkey = "20171111_88"
    val cf = "info"
    val column = "click_count"
    val value = "2"

    save(tableName, rowkey, cf, column, value.toLong)
  }


}
