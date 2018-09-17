package com.imooc.zhk

import org.apache.log4j.Logger
;

object LoggerGenerator {
      val logger=Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    var index=0
      while(true){
        Thread.sleep(2000)
        logger.info("it is message "+
          index)
        index=index+1

      }
  }
}
