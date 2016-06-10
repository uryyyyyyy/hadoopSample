package com.github.uryyyyyyy.hadoop.spark.mlib.word2vec

import java.io.StringReader

import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.ja.tokenattributes.{BaseFormAttribute, PartOfSpeechAttribute}
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.spark.mllib.feature.Word2Vec
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Main {

  def main(args: Array[String]): Unit = {

    val path = args(0)

    val conf = new SparkConf().setAppName("word2Vector helloWorld")
    val sc = new SparkContext(conf)

    val rdd = sc.textFile(path).map(v => tokenize(v))

    val word2Vec = new Word2Vec()
    word2Vec.setMinCount(1)
    word2Vec.setVectorSize(1)

    val model = word2Vec.fit(rdd)

    val results = model.findSynonyms("蟇", 5)

    results.foreach(println)

  }

  def tokenize(sentence: String): Seq[String] ={
    val word: ArrayBuffer[String] = new ArrayBuffer()

    lazy val stream = new JapaneseTokenizer(
      new StringReader(sentence),
      null,
      false,
      JapaneseTokenizer.Mode.NORMAL
    )

    try{
      while(stream.incrementToken()) {
        val charAtt = stream.getAttribute(
          classOf[CharTermAttribute]
        ).toString

//        println("charAtt")
//        println(charAtt)

        val bfAtt = stream.getAttribute(
          classOf[BaseFormAttribute]
        ).getBaseForm

//        println("bfAtt")
//        println(bfAtt)

        val partOfSpeech = stream.getAttribute(
          classOf[PartOfSpeechAttribute]
        ).getPartOfSpeech.split("_")(0)

//        println("partOfSpeech")
//        println(partOfSpeech)

        (partOfSpeech, bfAtt) match {
          case (s, _) if s.contains("名詞") => word += charAtt
          case (s, null) if s.contains("動詞") && !s.contains("助動詞") => word += charAtt
          case (s, baseForm) if s.contains("動詞") && !s.contains("助動詞") => word += baseForm
          case _ =>
        }
      }
    } finally {
      stream.close()
    }
    word
  }
}
