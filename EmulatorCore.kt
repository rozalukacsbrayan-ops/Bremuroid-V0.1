package com.bremuroid.app.core

/**
 * Punto de integración para futuros núcleos de emulación.
 * V0.1 no contiene implementaciones de emulación.
 */
interface EmulatorCore {
    val systemName: String
    fun isCompatible(gameFileName: String): Boolean
    suspend fun launch(gameUri: String)
}
