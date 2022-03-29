@file:Suppress("UNCHECKED_CAST")

package hr.fer.help193.vehicle.utils

import org.koin.core.parameter.DefinitionParameters

fun <T> DefinitionParameters.get(index: Int, default: T) = if (index >= values.size) default else values[index] as T
