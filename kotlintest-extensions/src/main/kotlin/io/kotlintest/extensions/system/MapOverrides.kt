package io.kotlintest.extensions.system


enum class SystemOverrideMode { ALLOW_OVERRIDE, DENY_OVERRIDE }

@PublishedApi
internal fun calculateMapToSet(original: Map<String, String>, desired: Map<String, String?>, mode: SystemOverrideMode): Map<String, String?> {
  return if (mode == SystemOverrideMode.ALLOW_OVERRIDE) {
    original overridenWith desired
  } else {
    original completedWith desired
  }
}

private infix fun Map<String,String>.overridenWith(map: Map<String, String?>): MutableMap<String, String> {
  return toMutableMap().apply { putReplacingNulls(map) }
}

@PublishedApi
internal fun MutableMap<String,String>.putReplacingNulls(map: Map<String, String?>) {
  map.forEach { (key, value) ->
    if(value == null) remove(key) else put(key, value)
  }
}

private infix fun Map<String, String>.completedWith(map: Map<String, String?>): MutableMap<String, String> {
  return toMutableMap().apply { putWithoutReplacements(map) }
}

private fun MutableMap<String,String>.putWithoutReplacements(map: Map<String, String?>) {
  map.forEach { (key, value) ->
    value?.let { this.putIfAbsent(key, it) }
  }
}
