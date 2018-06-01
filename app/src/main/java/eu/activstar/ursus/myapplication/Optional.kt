package eu.activstar.ursus.myapplication

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 04-Apr-18.
 */
fun <T> optionalOf(t: T) = Optional(t)

fun optionalOfEmpty() = Optional(null)

data class Optional<out T>(
        private val value: T?
) {
    fun isEmpty() = value == null
    fun isNotEmpty() = value != null
    fun get(): T? = value
}