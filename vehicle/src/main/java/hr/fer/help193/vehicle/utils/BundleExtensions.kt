package hr.fer.help193.vehicle.utils

import android.os.Bundle

fun Bundle.getSerializableOrThrow(key: String): Any {
    if (!containsKey(key)) {
        throw IllegalStateException("Key $key is missing in the bundle!")
    }
    return getSerializable(key) as Any
}
