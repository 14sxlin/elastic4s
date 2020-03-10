package com.sksamuel.elastic4s.cat

import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.testkit.DockerTests
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CatNodesTest extends AnyFlatSpec with Matchers with DockerTests {

  client.execute {
    bulk(
      indexInto("catnodes1").fields("name" -> "hampton court palace"),
      indexInto("catnodes2").fields("name" -> "westminster abbey")
    ).refresh(RefreshPolicy.Immediate)
  }.await

  "cats nodes" should "return all nodes" in {
    val result = client.execute {
      catNodes()
    }.await.result.head
    result.load_1m > 0 shouldBe true
    result.cpu > 0 shouldBe true
    result.heapPercent > 0 shouldBe true
    result.ramPercent > 0 shouldBe true
  }
}
