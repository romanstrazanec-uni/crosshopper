package sk.romanstrazanec.crosshopper

import kotlin.math.abs

class Car(var x: Float, val y: Float, val width: Int, val height: Int, val color: Int, private val speed: Int, direction: Int) {
    private val direction: Int = direction / abs(direction)
    fun move() {
        x += (speed * direction).toFloat()
    }
}