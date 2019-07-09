package io.kotlintest.matchers.reflection

import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.should
import io.kotlintest.shouldNot
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

fun KFunction<*>.shouldHaveAnnotations() = this should haveFunctionAnnotations()
fun KFunction<*>.shouldNotHaveAnnotations() = this shouldNot haveFunctionAnnotations()
infix fun KFunction<*>.shouldHaveAnnotations(count: Int) = this should haveFunctionAnnotations(count)
infix fun KFunction<*>.shouldNotHaveAnnotations(count: Int) = this shouldNot haveFunctionAnnotations(count)
fun haveFunctionAnnotations(count: Int = -1) = object : Matcher<KFunction<*>> {
  override fun test(value: KFunction<*>) = if (count < 0) {
    Result(
        value.annotations.size > 0,
        "Function $value should have annotations",
        "Function $value should not have annotations"
    )
  } else {
    Result(
        value.annotations.size == count,
        "Function $value should have $count annotations",
        "Function $value should not have $count annotations"
    )
  }
}

inline fun <reified T : Annotation> KFunction<*>.shouldBeAnnotatedWith(block: (T) -> Unit = {}) {
  this should beAnnotatedWith<T>()
  findAnnotation<T>()?.let(block)
}

inline fun <reified T : Annotation> KFunction<*>.shouldNotBeAnnotatedWith() = this shouldNot beAnnotatedWith<T>()
inline fun <reified T : Annotation> beAnnotatedWith() = object : Matcher<KFunction<*>> {
  override fun test(value: KFunction<*>) = Result(
      value.findAnnotation<T>() != null,
      "Function $value should have annotation ${T::class}",
      "Function $value should not have annotation ${T::class}"
  )
}

inline fun <reified T> KFunction<*>.shouldHaveReturnType() = this.returnType.shouldBeOfType<T>()
inline fun <reified T> KFunction<*>.shouldNotHaveReturnType() = this.returnType.shouldNotBeOfType<T>()