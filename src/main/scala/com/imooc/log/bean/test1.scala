package com.imooc.log.bean

//class test1 {
//  val a=11
//}

object test1{
  def f(): Int = try { return 1 } finally { return 2 }
  def g(): Int = try { 1 } finally { 2 }
  def main(args: Array[String]): Unit = {
    val aa=test1
    val bb=test1
    println("f()="+f()+"g()"+g())
  }

}
