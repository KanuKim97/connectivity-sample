package com.kanukim97.nearbyshare_basic.model

/**
 * 2024.11.09
 *
 * EndPoint
 *
 * @param id Endpoint id
 * @param name Endpoint Name
 *
 * @author KanuKim97
 */
data class Endpoint(val id: String, val name: String) {
    override fun toString(): String {
        return String.format("Endpoint(id = $id, name = $name)")
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Endpoint) {
            return id == other.id
        }

        return false
    }

}