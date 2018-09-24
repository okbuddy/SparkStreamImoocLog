package com.imooc.log.dao

import com.imooc.log.bean.{CourseClickCount, CourseRefererCount}
import com.imooc.log.utils.HbaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

object CourseClickCountDao {

  val tableName="imooc_course_clickcount"
  val cf="info"
  val qualifer="click_count"

  def save(list:ListBuffer[CourseClickCount]): Unit ={
      val table=HbaseUtils.getTable(tableName)
      for(ele<-list){
        table.incrementColumnValue(ele.course.getBytes(),cf.getBytes(),qualifer.getBytes(),ele.clickCount)

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
    val list=ListBuffer(
      CourseClickCount("course1",2),
      CourseClickCount("course2",3),
      CourseClickCount("course3",5)
    )
    save(list)
    val cc=count("course2")
    println(cc)


  }


}
