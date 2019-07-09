package io.kotlintest.matchers.reflection

import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.should
import io.kotlintest.shouldNot
import kotlin.reflect.*
import kotlin.reflect.full.*

fun KClass<*>.shouldHaveAnnotations() = this should haveClassAnnontations()
fun KClass<*>.shouldNotHaveAnnotations() = this shouldNot haveClassAnnontations()
infix fun KClass<*>.shouldHaveAnnotations(count: Int) = this should haveClassAnnontations(count)
infix fun KClass<*>.shouldNotHaveAnnotations(count: Int) = this shouldNot haveClassAnnontations(count)
fun haveClassAnnontations(count: Int = -1) = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = if (count < 0) {
    Result(
        value.annotations.size > 0,
        "Class $value should have annotations",
        "Class $value should not have annotations"
    )
  } else {
    Result(
        value.annotations.size == count,
        "Class $value should have $count annotations",
        "Class $value should not have $count annotations"
    )
  }
}

inline fun <reified T : Annotation> KClass<*>.shouldBeAnnotatedWith(block: (T) -> Unit = {}) {
  this should beClassAnnotatedWith<T>()
  findAnnotation<T>()?.let(block)
}

inline fun <reified T : Annotation> KClass<*>.shouldNotBeAnnotatedWith() = this shouldNot beClassAnnotatedWith<T>()
inline fun <reified T : Annotation> beClassAnnotatedWith() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.findAnnotation<T>() != null,
      "Class $value should have annotation ${T::class}",
      "Class $value should not have annotation ${T::class}"
  )
}

fun KClass<*>.shouldHaveFunction(name: String, block: (KFunction<*>) -> Unit = {}) {
  this should haveFunction(name)
  findFunction(name)?.let(block)
}

infix fun KClass<*>.shouldHaveFunction(name: String) = this should haveFunction(name)
infix fun KClass<*>.shouldNotHaveFunction(name: String) = this shouldNot haveFunction(name)
fun haveFunction(name: String) = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.findFunction(name) != null,
      "Class $value should have function $name",
      "Class $value should not have function $name"
  )
}

fun KClass<*>.shouldHaveMemberProperty(name: String, block: (KProperty<*>) -> Unit) {
  this should haveMemberProperty(name)
  findMemberProperty(name)?.let(block)
}
infix fun KClass<*>.shouldHaveMemberProperty(name: String) = this should haveMemberProperty(name)
infix fun KClass<*>.shouldNotHaveMemberProperty(name: String) = this shouldNot haveMemberProperty(name)
fun haveMemberProperty(name: String) = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.findMemberProperty(name) != null,
      "Class $value should have a member property $name",
      "Class $value should not have a member property $name"
  )
}

inline fun <reified T> KClass<*>.shouldBeSubtypeOf() = this should beSubtypeOf<T>()
inline fun <reified T> KClass<*>.shouldNotBeSubtypeOf() = this shouldNot beSubtypeOf<T>()
inline fun <reified T> beSubtypeOf() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.isSubclassOf(T::class),
      "Class $value should be subtype of ${T::class}",
      "Class $value should not be subtype of ${T::class}"
  )
}

inline fun <reified T> KClass<*>.shouldBeSupertypeOf() = this should beSuperTypeOf<T>()
inline fun <reified T> KClass<*>.shouldNotBeSupertypeOf() = this shouldNot beSuperTypeOf<T>()
inline fun <reified T> beSuperTypeOf() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.isSuperclassOf(T::class),
      "Class $value should be subtype of ${T::class}",
      "Class $value should not be subtype of ${T::class}"
  )
}

fun KClass<*>.shouldBeData() = this should beData()
fun KClass<*>.shouldNotBeData() = this shouldNot beData()
fun beData() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.isData,
      "Class $value should be a data class",
      "Class $value should not be a data class"
  )
}

fun KClass<*>.shouldBeSealed() = this should beSealed()
fun KClass<*>.shouldNotBeSealed() = this shouldNot beSealed()
fun beSealed() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.isSealed,
      "Class $value should be a sealed class",
      "Class $value should not be a sealed class"
  )
}

fun KClass<*>.shouldBeCompanion() = this should beCompanion()
fun KClass<*>.shouldNotBeCompanion() = this shouldNot beCompanion()
fun beCompanion() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.isCompanion,
      "Class $value should be a companion object",
      "Class $value should not be a companion object"
  )
}

fun KClass<*>.shouldHavePrimaryConstructor() = this should havePrimaryConstructor()
fun KClass<*>.shouldNotHavePrimaryConstructor() = this shouldNot havePrimaryConstructor()
fun havePrimaryConstructor() = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.primaryConstructor != null,
      "Class $value should have a primary constructor",
      "Class $value should not have a primary constructor"
  )
}

infix fun KClass<*>.shouldHaveVisibility(expected: KVisibility) = this should haveClassVisibility(expected)
infix fun KClass<*>.shouldNotHaveVisibility(expected: KVisibility) = this shouldNot haveClassVisibility(expected)
fun haveClassVisibility(expected: KVisibility) = object : Matcher<KClass<*>> {
  override fun test(value: KClass<*>) = Result(
      value.visibility == expected,
      "Class $value should have visibility ${expected.humanName()}",
      "Class $value should not have visibility ${expected.humanName()}"
  )
}