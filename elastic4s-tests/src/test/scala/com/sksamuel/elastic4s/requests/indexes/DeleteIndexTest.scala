package com.sksamuel.elastic4s.requests.indexes

import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.testkit.DockerTests
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DeleteIndexTest extends AnyWordSpec with Matchers with DockerTests {

  "delete index request" should {
    "delete index" in {

      client.execute {
        createIndex("languages").mapping(
          properties(
            textField("type")
          )
        ).shards(1).waitForActiveShards(1)
      }.await

      client.execute {
        indexExists("languages")
      }.await.result.exists shouldBe true

      client.execute {
        ElasticDsl.deleteIndex("languages")
      }.await.result.acknowledged shouldBe true

      client.execute {
        indexExists("languages")
      }.await.result.exists shouldBe false
    }

    "support multiple indexes" in {
      client.execute {
        createIndex("languages1").mapping(
          properties(
            textField("type")
          )
        )
      }.await

      client.execute {
        createIndex("languages2").mapping(
          properties(
            textField("type")
          )
        )
      }.await

      client.execute {
        indexExists("languages1")
      }.await.result.exists shouldBe true

      client.execute {
        indexExists("languages2")
      }.await.result.exists shouldBe true

      client.execute {
        ElasticDsl.deleteIndex("languages1", "languages2")
      }.await.result.acknowledged shouldBe true

      client.execute {
        indexExists("languages1")
      }.await.result.exists shouldBe false

      client.execute {
        indexExists("languages2")
      }.await.result.exists shouldBe false
    }
  }
}
