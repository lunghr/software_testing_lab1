package com.example.software_testing_lab1

import com.example.software_testing_lab1.models.Arcsin
import com.example.software_testing_lab1.services.ArcsinDecompositionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test

class ArcsinDecompositionServiceTests {
    private var arcsinDecompositionService = ArcsinDecompositionService()


    @Test
    fun `factorial should throw IllegalArgumentException when n is negative`() {
        val n = -1
        val exception = assertThrows<IllegalArgumentException> {
            arcsinDecompositionService.factorial(n)
        }
        assertEquals("n should be non-negative", exception.message)
    }

    @ParameterizedTest
    @ValueSource(ints = [0])
    fun `factorial should return 1 when n is 0`(n: Int) {
        val result = arcsinDecompositionService.factorial(n)
        assertEquals(1, result)
    }


    //Parametrized version of the test above
    @ParameterizedTest
    @CsvSource(
        "0, 1",
        "1, 1",
        "2, 2",
        "3, 6",
        "4, 24",
        "5, 120",
        "6, 720",
        "7, 5040",
        "8, 40320",
        "9, 362880"
    )
    fun `factorial should return correct values`(n: Int, expected: Int) {
        val result = arcsinDecompositionService.factorial(n)
        assertEquals(expected, result)
    }

    //Parametrized version of the test above
    @ParameterizedTest
    @CsvSource(
        "2.0, 0.1",
        "-1.5, 0.05",
        "1.2, 0.001"
    )
    fun `decompose should throw IllegalArgumentException when x is out of range`(x: Double, accuracy: Double) {
        val arcsin = Arcsin(x, accuracy)
        val exception = assertThrows<IllegalArgumentException> {
            arcsinDecompositionService.decompose(arcsin)
        }
        assertEquals("x should be in range [-1, 1]", exception.message)
    }

    @Test
    fun `decompose should return IllegalAgumentException when accuracy is negative`() {
        val arcsin = Arcsin(0.5, -0.1)
        val exception = assertThrows<IllegalArgumentException> {
            arcsinDecompositionService.decompose(arcsin)
        }
        assertEquals("accuracy should be in range (0, 1)", exception.message)
    }



    @Test
    fun `decompose should return IllegalAgumentException when accuracy is 0`() {
        val arcsin = Arcsin(0.5, 0.0)
        val exception = assertThrows<IllegalArgumentException> {
            arcsinDecompositionService.decompose(arcsin)
        }
        assertEquals("accuracy should be in range (0, 1)", exception.message)
    }

    @Test
    fun `decompose should return IllegalAgumentException when accuracy is 1`() {
        val arcsin = Arcsin(0.5, 1.0)
        val exception = assertThrows<IllegalArgumentException> {
            arcsinDecompositionService.decompose(arcsin)
        }
        assertEquals("accuracy should be in range (0, 1)", exception.message)
    }

    @ParameterizedTest
    @CsvSource(
        "0.5, -0.1",
        "0.5, 0.0",
        "0.5, 1.0"
    )
    fun `decompose should throw IllegalArgumentException for invalid accuracy`(x: Double, accuracy: Double) {
        val arcsin = Arcsin(x, accuracy)
        val exception = assertThrows<IllegalArgumentException> {
            arcsinDecompositionService.decompose(arcsin)
        }
        assertEquals("accuracy should be in range (0, 1)", exception.message)
    }

    @Test
    fun `decompose should return correct answer for x = 0 and accuracy = 0,1`() {
        val arcsin = Arcsin(0.0, 0.1)
        val result = arcsinDecompositionService.decompose(arcsin)
        assertEquals(0.0, result, 0.1)
    }

    @Test
    fun `decompose should return correct answer for x = 0,5 and accuracy = 0,001`() {
        val arcsin = Arcsin(0.5, 0.001)
        val result = arcsinDecompositionService.decompose(arcsin)
        assertEquals(0.5235, result, 0.001)
    }

    @Test
    fun `decompose should return correct answers for list of x and accuracy with unique deltas`() {
        val arcsins = listOf(
            0.1 to 0.00001,
            -0.1 to 0.00001,
            -1.0 to 0.00001,
            1.0 to 0.001
        ).map {
            Arcsin(it.first, it.second)
        }

        val expected = listOf( 0.10017, -0.10017, -1.5708, 1.5708)
        val deltas = listOf( 0.00001, 0.0001, 0.00001, 0.001)
        val results = arcsins.map { arcsinDecompositionService.decompose(it) }

        expected.zip(results).zip(deltas).forEach { (expectedResult, delta) ->
            val (expectedValue, resultValue) = expectedResult
            assertEquals(expectedValue, resultValue, delta)
        }
    }

}