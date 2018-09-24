package com.imooc.log.dao

import com.imooc.log.bean.{CourseClickCount, CourseRefererCount}
import com.imooc.log.utils.HbaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

object CourseRefererCountDao {

  val tableName="imooc_course_referercount"
  val cf="info"
  val qualifer="count"


  def saveReferer(list:ListBuffer[CourseRefererCount]): Unit ={
    val table=HbaseUtils.getTable(tableName)
    for(ele<-list){
      table.incrementColumnValue(ele.referer.getBytes(),cf.getBytes(),qualifer.getBytes(),ele.count)

    }
  }


  def count(day_course:String):Long={
    val table=HbaseUtils.getTable(tableName)
    val count=table.get(new Get(day_course.getBytes())).getValue(cf.getBytes(),qualifer.getBytes())
    if (count==null){
      0l
    }else{
      Bytes.toLong(count)
    }
  }

  def main(args: Array[String]): Unit = {



  }


}
