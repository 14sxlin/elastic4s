package com.sksamuel.elastic4s.requests.update

import com.sksamuel.elastic4s.JsonSugar
import com.sksamuel.elastic4s.requests.script.Script
import org.scalatest.wordspec.AnyWordSpec

class UpdateByQueryBodyFnTest extends AnyWordSpec with JsonSugar {

  import com.sksamuel.elastic4s.ElasticDsl._

  "update by query" should {
    "generate correct body" when {
      "script is specified" in {
        val q = updateIn("test").query(matchQuery("field", 123)).script(Script("script", Some("painless")))
        UpdateByQueryBodyFn(q).string() should matchJson(
          """{"query":{"match":{"field":{"query":123}}},"script":{"lang":"painless","source":"script"}}"""
        )
      }
      "script is not specified" in {
        val q = updateIn("test").query(matchQuery("field", 123))
        UpdateByQueryBodyFn(q).string() should matchJson(
          """{"query":{"match":{"field":{"query":123}}}}"""
        )
      }
    }
  }
}
