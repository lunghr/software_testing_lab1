package com.example.software_testing_lab1

import com.example.software_testing_lab1.services.BFSService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BFSServiceTests {
    private val bfsService = BFSService()

    @Test
    fun `breadthFirstSearch should correct answers for graph`() {
        val graph = mapOf(
            0 to listOf(1, 2),
            1 to listOf(0, 3, 4),
            2 to listOf(0, 5),
            3 to listOf(1),
            4 to listOf(1),
            5 to listOf(2)
        )
        val start = 0
        val expected = listOf(0, 1, 2, 3, 4, 5)
        val result = bfsService.breadthFirstSearch(graph, start)
        assert(expected == result)
    }

    @Test
    fun `breadthFirstSearch should throw IllegalArgumentException when start is not in graph`() {
        val graph = mapOf(
            0 to listOf(1, 2),
            1 to listOf(0, 3, 4),
            2 to listOf(0, 5),
            3 to listOf(1),
            4 to listOf(1),
            5 to listOf(2)
        )
        val start = 6
        assertThrows<IllegalArgumentException> {
            bfsService.breadthFirstSearch(graph, start)
        }
    }

    @Test
    fun `breadthFirstSearch should throw IllegalArgumentException when graph is empty`() {
        val graph = mapOf<Int, List<Int>>()
        val start = 0
        assertThrows<IllegalArgumentException> {
            bfsService.breadthFirstSearch(graph, start)
        }
    }

    @Test
    fun `breadthFirstSearch should correct answer for graph with one vertex`() {
        val graph = mapOf(0 to emptyList<Int>())
        val start = 0
        val expected = listOf(0)
        val result = bfsService.breadthFirstSearch(graph, start)
        assert(expected == result)
    }

    @Test
    fun `breadthFirstSearch should correct answer for incoherent graph`() {
        val graph = mapOf(
            1 to listOf(2),
            2 to listOf(1),
            3 to listOf(4),
            4 to listOf(3)
        )
        val start = 2
        val expected = listOf(2, 1)
        val result = bfsService.breadthFirstSearch(graph, start)
        assert(expected == result)
    }

    @Test
    fun `breadthFirstSearch should correct answer for graph with cycle`() {
        val graph = mapOf(
            0 to listOf(1),
            1 to listOf(0, 2),
            2 to listOf(1)
        )
        val start = 0
        val expected = listOf(0, 1, 2)
        val result = bfsService.breadthFirstSearch(graph, start)
        assert(expected == result)
    }

    @Test
    fun `breadthFirstSearch should correct answer for linear graph`() {
        val graph = mapOf(
            0 to listOf(1),
            1 to listOf(2),
            2 to listOf(3),
            3 to listOf(4),
            4 to listOf(5),
            5 to listOf(6)
        )
        val start = 0
        val expected = listOf(0, 1, 2, 3, 4, 5, 6)
        val result = bfsService.breadthFirstSearch(graph, start)
        assert(expected == result)
    }

    @Test
    fun `generateGraph should throw IllegalArgumentException when vertices is not positive`() {
        val vertices = 0
        assertThrows<IllegalArgumentException> {
            bfsService.generateGraph(vertices)
        }
    }


    @Test
    fun `generateGraph should throw IllegalArgumentException when edgeProbability is not in range from 0 to 1`() {
        val vertices = 5
        val edgeProbability = 1.1
        assertThrows<IllegalArgumentException> {
            bfsService.generateGraph(vertices, edgeProbability)
        }
    }

    @Test
    fun `generateGraph should return symmetric graph for vertices = 10 and edgeProbability = 0,8`() {
        val graph = bfsService.generateGraph(10, 0.8)

        // Проверка симметричности рёбер
        graph.forEach { (vertex, neighbors) ->
            neighbors.forEach { neighbor ->
                assert(graph[neighbor]?.contains(vertex) == true) {
                    "Graph is not symmetric: Vertex $vertex is connected to $neighbor, but not the other way around"
                }
            }
        }
    }


    @Test
    fun `generateGraph should return empty graph for vertices = 10 and edgeProbability = 0`() {
        val graph = bfsService.generateGraph(10, 0.0)
        assert(graph.all { it.value.isEmpty() })
    }

    @Test
    fun `generateGraph should return fully connected graph for vertices = 10 and edgeProbability = 1`() {
        val graph = bfsService.generateGraph(10, 1.0)
        assert(graph.all { (v, neighbors) ->
            neighbors.size == 9 && neighbors.all { it != v }
        })
    }

    @Test
    fun `generateGraph should return graph with no self-loops`() {
        val graph = bfsService.generateGraph(10, 0.5)
        assert(graph.all { (v, neighbors) -> v !in neighbors })
    }

    @Test
    fun `generateGraph should return graph with correct number of vertices`() {
        val graph = bfsService.generateGraph(10, 0.5)
        assert(graph.size == 10)
    }

}