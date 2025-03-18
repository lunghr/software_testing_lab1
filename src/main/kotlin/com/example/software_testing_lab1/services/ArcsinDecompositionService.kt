package com.example.software_testing_lab1.services

import com.example.software_testing_lab1.models.Arcsin
import org.springframework.stereotype.Service
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.pow

@Service
class ArcsinDecompositionService {

    fun decompose(arcsin: Arcsin): Double {
        require(arcsin.x in -1.0..1.0) { "x should be in range [-1, 1]" }
        require(arcsin.accuracy in 0.0..1.0 && arcsin.accuracy != 0.0 && arcsin.accuracy != 1.0) { "accuracy should be in range (0, 1)" }
        if (arcsin.x == 1.0) return PI / 2 else if (arcsin.x == -1.0) return -PI / 2
        return generateSequence(0) { it + 1 }
            .map { n ->
                calculateTerm(n, arcsin.x)
            }
            .takeWhile { abs(asin(arcsin.x) - it) >= arcsin.accuracy / 3 }
            .sum()
    }

    fun factorial(n: Int): Int {
        require(n >= 0) { "n should be non-negative" }
        return if (n == 0) 1 else n * factorial(n - 1)
    }

    // Function used only in decompose function which is tested, so it is not covered by tests because it is not used in any other place
    private fun calculateTerm(n: Int, x: Double): Double =
        (factorial(2 * n)) / (4.0.pow(n.toDouble()) * factorial(n).toDouble()
            .pow(2.0) * (2 * n + 1)) * x.pow((2 * n + 1).toDouble())

}