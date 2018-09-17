package com.imooc.log.utils

import java.util.Locale

import org.apache.commons.lang3.time.FastDateFormat

object DateUtils {
  //  2018-09-12 15-17-01
  val curFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
  val newFormat = FastDateFormat.getInstance("yyyyMMdd", Locale.CHINA)


  def reformatDate(date: String): String = {
    val str = newFormat.format(curFormat.parse(date))
    str
  }

  def main(args: Array[String]): Unit = {
    val dd = "2018-09-12 15:17:01"
    println(reformatDate(dd))
  }

}
