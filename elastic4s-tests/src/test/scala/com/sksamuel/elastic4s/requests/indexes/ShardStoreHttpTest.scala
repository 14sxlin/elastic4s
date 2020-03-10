package com.sksamuel.elastic4s.requests.indexes

import com.sksamuel.elastic4s.requests.indexes.admin.IndexShardStoreResponse.{IndexStoreStatus, ShardStoreStatus}
import com.sksamuel.elastic4s.testkit.DockerTests

import scala.util.Try
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ShardStoreHttpTest extends AnyWordSpec with Matchers with DockerTests {

  Try {
    client.execute {
      deleteIndex("shardstoretest")
    }.await
  }

  client.execute {
    createIndex("shardstoretest").mapping(
      properties(
        textField("name")
      )
    ).shards(1).replicas(0).waitForActiveShards(1)
  }.await

  "shard store request" should {
    "get green shards" in {

      val indexInfo = client.execute {
        indexShardStores("shardstoretest") status "green"
      }.await.result.indices.getOrElse("shardstoretest", IndexStoreStatus(Map.empty))

      val shardInfo: ShardStoreStatus = indexInfo.shards.getOrElse("0", ShardStoreStatus(Seq.empty))
      shardInfo.stores.size should be(1)
    }
  }
}
