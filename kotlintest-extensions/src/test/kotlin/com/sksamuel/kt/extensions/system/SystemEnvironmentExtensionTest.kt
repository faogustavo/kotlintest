package com.sksamuel.kt.extensions.system

import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.system.SystemEnvironmentTestListener
import io.kotlintest.extensions.system.SystemOverrideMode
import io.kotlintest.extensions.system.SystemOverrideMode.ALLOW_OVERRIDE
import io.kotlintest.extensions.system.SystemOverrideMode.DENY_OVERRIDE
import io.kotlintest.extensions.system.withEnvironment
import io.kotlintest.inspectors.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.AbstractFreeSpec
import io.kotlintest.specs.FreeSpec
import io.kotlintest.specs.ShouldSpec

class SystemEnvironmentExtensionFunctionTest : FreeSpec() {
  
  init {
    "The system environment configured with a custom value" - {
      "Should contain the custom variable" - {
        val allResults = executeOnAllSystemEnvironmentOverloads("foo", "bar") {
          System.getenv("foo") shouldBe "bar"
          "RETURNED"
        }
        
        allResults.forAll { it shouldBe "RETURNED" }
      }
    }
    
    "The system environment already with a specified value" - {
      "Should become null when I set it to null" - {
        System.getenv("foo") shouldBe null  // Enforcing pre conditions
        
        withEnvironment("foo", "booz") {
          val allResults = executeOnAllSystemEnvironmentOverloads("foo", null) {
            System.getenv("foo") shouldBe null
            "RETURNED"
          }
  
          allResults.forAll { it shouldBe "RETURNED" }
  
        }
      }

      "Should stay with specified value if mode is DENY_OVERRIDE" - {
        System.getenv("foo") shouldBe null  // Enforcing pre conditions

        withEnvironment("foo", "booz") {
          val allResults = executeOnAllSystemEnvironmentOverloads("foo", "bar", DENY_OVERRIDE) {
            System.getenv("foo") shouldBe "booz"
            "RETURNED"
          }

          allResults.forAll { it shouldBe "RETURNED" }
        }
      }

      "Should override specified value if mode is ALLOW_OVERRIDE" - {
        System.getenv("foo") shouldBe null  // Enforcing pre conditions

        withEnvironment("foo", "booz") {
          val allResults = executeOnAllSystemEnvironmentOverloads("foo", null, ALLOW_OVERRIDE) {
            System.getenv("foo") shouldBe null
            "RETURNED"
          }

          allResults.forAll { it shouldBe "RETURNED" }

        }
      }
    }
  }
  
  override fun afterSpec(spec: Spec) {
    verifyFooIsUnset()
  }
  
}

private suspend fun AbstractFreeSpec.FreeSpecScope.executeOnAllSystemEnvironmentOverloads(
        key: String,
        value: String?,
        mode: SystemOverrideMode = ALLOW_OVERRIDE,
        block: suspend () -> String
): List<String> {
  val results = mutableListOf<String>()
  
  "String String overload" {
    results += withEnvironment(key, value, mode) {
      block()
    }
  }
  
  "Pair overload" {
    results += withEnvironment(key to value, mode) { block() }
  }
  
  "Map overload" {
    results += withEnvironment(mapOf(key to value), mode) { block() }
  }
  
  return results
}

class SystemEnvironmentTestListenerOverwriteTest : ShouldSpec() {
  
  override fun listeners() = listOf(SystemEnvironmentTestListener("foo", "bar", ALLOW_OVERRIDE))
  
  init {
    should("Get extra extension from environment") {
      verifyFooIsBar()
    }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    // The environment must be reset afterwards
    verifyFooIsUnset()
  }
  
}

class SystemEnvironmentTestListenerCreateTest : ShouldSpec() {

  private val alreadyInEnvironment = System.getenv().entries.first()

  override fun listeners() = listOf(SystemEnvironmentTestListener(
          alreadyInEnvironment.key, alreadyInEnvironment.value + "FOOBARFOO", DENY_OVERRIDE
  ))

  init {
    should("Not override environment") {
      System.getenv(alreadyInEnvironment.key) shouldBe alreadyInEnvironment.value
    }
  }
}

private fun verifyFooIsBar() {
  System.getenv("foo") shouldBe "bar"
}

private fun verifyFooIsUnset() {
  System.getenv("foo") shouldBe null
}