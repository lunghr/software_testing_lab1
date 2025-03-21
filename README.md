# Software Testing Course | Lab 1 | Tupichenko Mila | P3309

## Задание

1. Для указанной функции провести модульное тестирование разложения функции в степенной ряд. Выбрать достаточное
   тестовое покрытие.
2. Провести модульное тестирование указанного алгоритма. Для этого выбрать характерные точки внутри алгоритма, и для
   предложенных самостоятельно наборов исходных данных записать последовательность попадания в характерные точки.
   Сравнить последовательность попадания с эталонной.
3. Сформировать доменную модель для заданного текста. Разработать тестовое покрытие для данной доменной модели

## Вариант 330909

1. Функция **arcsin(x)**
2. Программный модуль для обхода неориентированного графе методом поиска в ширину
   *http://www.cs.usfca.edu/~galles/visualization/BFS.html*
3. Описание предметной области:

       После того, как толпа вновь разразилась ликующими криками, Артур обнаружил, что он скользит по воздуху к одному 
       из величественных окон во втором этаже здания, перед которым стоял помост, с которого оратор обращался к народу. 

## Выполнение

### Задание 1

---

#### Код функции

---

**Основная функция разложения** - Тестовое покрытие полное, рассматриваются все случаи входных данных, краевые случае,
неправильный ввод для обарботки ошибок

```kotlin
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
```

**Функция вычисления факториала** - Тестовое покрытие полное, рассматриваются все случаи входных данных, а также ввод
некорректных данных для обработки ошибок

```kotlin

fun factorial(n: Int): Int {
    require(n >= 0) { "n should be non-negative" }
    return if (n == 0) 1 else n * factorial(n - 1)
}
```

**Вспомогательная функция вычисления одного терма ряда** - функция используется исключительно в функции ```decompose```,
которая полностью покрыта тестами, поэтому данная функция напрямую не тестируется, так как формула была вынесена в
отдельную функцию исключительно для лаконичности кода

```kotlin

private fun calculateTerm(n: Int, x: Double): Double =
    (factorial(2 * n)) / (4.0.pow(n.toDouble()) * factorial(n).toDouble()
        .pow(2.0) * (2 * n + 1)) * x.pow((2 * n + 1).toDouble())
```

---

#### Тесты

---

**Тесты для основной функции разложения**

```kotlin
    @Test
fun `decompose should throw IllegalArgumentException when x is not in range from -1 to 1`() {
    val arcsin = Arcsin(2.0, 0.1)
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
        0.0 to 0.1,
        0.5 to 0.001,
        0.3 to 0.01,
        0.7 to 0.0001,
        0.1 to 0.00001,
        0.2 to 0.0001,
        -1.0 to 0.00001,
        1.0 to 0.001
    ).map {
        Arcsin(it.first, it.second)
    }

    val expected = listOf(0.0, 0.5235, 0.3047, 0.7753, 0.10017, 0.2014, -1.5708, 1.5708)
    val deltas = listOf(0.1, 0.001, 0.01, 0.0001, 0.00001, 0.0001, 0.00001, 0.001)
    val results = arcsins.map { arcsinDecompositionService.decompose(it) }

    expected.zip(results).zip(deltas).forEach { (expectedResult, delta) ->
        val (expectedValue, resultValue) = expectedResult
        assertEquals(expectedValue, resultValue, delta)
    }
}
```

**Тесты вспомогательной функции** ```factorial()```

```kotlin
 @Test
fun `factorial should throw IllegalArgumentException when n is negative`() {
    val n = -1
    val exception = assertThrows<IllegalArgumentException> {
        arcsinDecompositionService.factorial(n)
    }
    assertEquals("n should be non-negative", exception.message)
}

@Test
fun `factorial should return 1 when n is 0`() {
    val n = 0
    val result = arcsinDecompositionService.factorial(n)
    assertEquals(1, result)
}


@Test
fun `factorial should correct answers for sequence of n`() {
    val expected = listOf(1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880)
    val results = (0..9).map { arcsinDecompositionService.factorial(it) }
    assertEquals(expected, results)
}
```

### Задание 2

---

#### Код алгоритма

---

**Основная функция** - Тестовое покрытие полное, рассматриваются все случаи входных данных, краевые случаи,
неправильный. В функцию включеная вспомоагтельная хвостовая рекурсия ```bfs()```, которая, по идее и является алгоритмом
**bfs**

```kotlin
 fun breadthFirstSearch(graph: Map<Int, List<Int>>, start: Int): List<Int> {
    require(start in graph.keys) { "Start vertex should be in graph" }
    require(graph.isNotEmpty()) { "Graph should not be empty" }
    tailrec fun bfs(queue: List<Int>, visited: List<Int>, result: List<Int>): List<Int> {
        if (queue.isEmpty()) return result
        val (head, tail) = queue.first() to queue.drop(1)
        val newVisited = graph[head]?.filterNot { it in visited } ?: emptyList()
        return bfs(tail + newVisited, visited + newVisited, result + head)
    }
    return bfs(listOf(start), listOf(start), emptyList())
}
```

**Функция генерации графа** - Тестовое покрытие полное, рассматриваются все случаи входных данных, краевые
случаи,неправильный ввод для обработки ошибок. Функция генерирует граф, представленный в виде словаря, где ключ - это
вершина, а значения - список смежных с ней вершин.

```kotlin
    fun generateGraph(vertices: Int, edgeProbability: Double = 0.3): Map<Int, List<Int>> {
    require(vertices > 0) { "Graph must have at least one vertex" }
    require(edgeProbability in 0.0..1.0) { "Edge probability must be between 0 and 1" }
    val graph = mutableMapOf<Int, MutableSet<Int>>().apply {
        (1..vertices).forEach { this[it] = mutableSetOf() }
    }
    (1..vertices).forEach { v ->
        val possibleNeighbors = (1..vertices).filter { it != v && it !in graph[v]!! }
        val neighborsToAdd = possibleNeighbors.filter { Random.nextDouble() < edgeProbability }
        neighborsToAdd.forEach { neighbor ->
            graph[v]?.add(neighbor)
            graph[neighbor]?.add(v)
        }
    }
    return graph.mapValues { it.value.toList() }
}
```

---

#### Тесты

---

**Базовый тест**

```kotlin

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
```

**Тесты на неккоректный ввод**

```kotlin

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
```

**Краевой случай:** граф с одной вершиной

```kotlin
@Test
fun `breadthFirstSearch should correct answer for graph with one vertex`() {
    val graph = mapOf(0 to emptyList<Int>())
    val start = 0
    val expected = listOf(0)
    val result = bfsService.breadthFirstSearch(graph, start)
    assert(expected == result)
}
```

**Краевой случай:** несвязный граф

```kotlin
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
```

**Краевой случай:** граф с циклом

```kotlin
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
```

**Краевой случай:** линейный граф

```kotlin
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
```

**Тесты для генерации графа**

```kotlin
@Nested
inner class GenerateGraphFunctionTest {
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
```

**Тесты для совместной работы функций**

```kotlin
@Nested
inner class GeneralWorkflowTest {
    @Test
    fun `breadthFirstSearch should return all reachable nodes in generated graph`() {
        val graph = bfsService.generateGraph(10, 0.5)
        val startNode = graph.keys.random()
        val visitedNodes = bfsService.breadthFirstSearch(graph, startNode)

        assertEquals(visitedNodes.toSet().size, visitedNodes.size)
        assert(visitedNodes.all { it in graph.keys })
    }

    @Test
    fun `breadthFirstSearch should visit all nodes in fully connected graph`() {
        val graph = bfsService.generateGraph(5, 1.0)
        val startNode = graph.keys.first()
        val visitedNodes = bfsService.breadthFirstSearch(graph, startNode)

        assertEquals(graph.keys.size, visitedNodes.size)
    }

    @Test
    fun `breadthFirstSearch should return only start node for isolated vertices`() {
        val graph = bfsService.generateGraph(5, 0.0)
        graph.keys.forEach { node ->
            val visitedNodes = bfsService.breadthFirstSearch(graph, node)
            assertEquals(listOf(node), visitedNodes)
        }
    }

    @Test
    fun `breadthFirstSearch should return single node for graph with one vertex`() {
        val graph = bfsService.generateGraph(1)
        val visitedNodes = bfsService.breadthFirstSearch(graph, 1)
        assertEquals(listOf(1), visitedNodes)
    }
}
```

### Задание 3

---

#### Код модели

---

**Доменная модель**

```kotlin
data class Arthur(
    val action: Action = Action.STAY
) {

    fun fly() = Arthur(Action.FLY)

    fun stay() = Arthur(Action.STAY)
}

data class Window(
    val appearance: WindowAppearance = WindowAppearance.ORDINARY,
    val floor: Int = 1,
    val isOpen: Boolean = false
) {
    fun changeAppearance(newAppearance: WindowAppearance) = Window(newAppearance, floor, isOpen)

    fun changeFloor(newFloor: Int) = Window(appearance, newFloor, isOpen)

    fun openWindow() = Window(appearance, floor, true)

    fun closeWindow() = Window(appearance, floor, false)

}

data class Building(
    val floors: Int = 5,
    val location: Location = Location.BEHIND,
) {
    fun addFloor() = Building(floors + 1, location)

    fun removeFloor() = Building(floors - 1, location)

    fun changeLocation(newLocation: Location) = Building(floors, newLocation)

    fun isTall() = floors > 5
}

data class Platform(
    val location: Location = Location.IN_FRONT_OF,
) {
    fun changeLocation(newLocation: Location) = Platform(newLocation)
}

data class Speaker(
    var state: SpeakerState = SpeakerState.SILENT
) {

    fun startSpeaking() = Speaker(SpeakerState.SPEAKING)

    fun pauseSpeaking() = Speaker(SpeakerState.PAUSED)

    fun stopSpeaking() = Speaker(SpeakerState.SILENT)

    fun isSpeaking() = state == SpeakerState.SPEAKING || state == SpeakerState.PAUSED
}

data class Crowd(
    var people: Int = 5,
    val reaction: CrowdReaction = CrowdReaction.SILENT
) {
    fun addPerson() = Crowd(people + 1, reaction)

    fun removePerson() = Crowd(people - 1, reaction)

    fun isCrowdExist() = people > 0

    private fun changeReaction(newReaction: CrowdReaction) = Crowd(people, newReaction)

    fun reactToSpeaker(speaker: Speaker, reaction: CrowdReaction) =
        (speaker.isSpeaking()).takeIf { it }?.let { changeReaction(reaction) } ?: changeReaction(CrowdReaction.SILENT)
}
```

---

#### Тесты

---


**Тесты для класса** ```Arthur```

```kotlin
@Nested
inner class TestArthur {
    @BeforeEach
    fun setUp() {
        arthur = Arthur()
    }

    @Test
    fun `startFlight should start flight`() {
        assertEquals(Action.FLY, domainModelService.startFlight(arthur).action)
    }

    @Test
    fun `stopFlight should stop flight`() {
        arthur = arthur.fly()
        assertEquals(Action.STAY, domainModelService.stopFlight(arthur).action)
    }

    @Test
    fun `startFlight should throw exception if arthur is already flying`() {
        arthur = arthur.fly()
        assertThrows<IllegalArgumentException> {
            domainModelService.startFlight(arthur)
        }
    }

    @Test
    fun `stopFlight should throw exception if arthur is already staying`() {
        assertThrows<IllegalArgumentException> {
            domainModelService.stopFlight(arthur)
        }
    }
}
```

**Тесты для класа** ```Window```

```kotlin
@Nested
inner class TestWindow {
    @BeforeEach
    fun setUp() {
        window = Window()
        building = Building()
    }

    @Test
    fun `changeWindowAppearance should change appearance of window`() {
        val appearance = WindowAppearance.MAJESTIC
        assertEquals(appearance, domainModelService.changeWindowAppearance(window, appearance).appearance)
    }

    @Test
    fun `changeWindowFloor should change floor of window`() {
        val floor = 2
        assertEquals(floor, domainModelService.changeWindowFloor(window, floor, building).floor)
    }

    @Test
    fun `changeWindowFloor should throw exception if floor is not in building`() {
        val floor = building.floors + 1
        assertThrows<IllegalArgumentException> {
            domainModelService.changeWindowFloor(window, floor, building)
        }
    }

    @Test
    fun `openWindow should open window`() {
        assertEquals(true, domainModelService.openWindow(window).isOpen)
    }

    @Test
    fun `closeWindow should close window`() {
        window = window.openWindow()
        assertEquals(false, domainModelService.closeWindow(window).isOpen)
    }

    @Test
    fun `openWindow should throw exception if window is already open`() {
        window = window.openWindow()
        assertThrows<IllegalArgumentException> {
            domainModelService.openWindow(window)
        }
    }

    @Test
    fun `closeWindow should throw exception if window is already closed`() {
        assertThrows<IllegalArgumentException> {
            domainModelService.closeWindow(window)
        }
    }
}
```

**Тесты для класса** ```Building``` **и** ```Platform```

```kotlin
    @Nested
inner class TestBuildingAndPlatform {

    @BeforeEach
    fun setUp() {
        building = Building()
        platform = Platform()
    }

    @Test
    fun `addFloorToBuilding should add floor to building`() {
        assertEquals(building.floors + 1, domainModelService.addFloorToBuilding(building).floors)
    }

    @Test
    fun `removeFloorFromBuilding should remove floor from building`() {
        assertEquals(building.floors - 1, domainModelService.removeFloorFromBuilding(building).floors)
    }

    @Test
    fun `changeLocationOfBuilding should change location of building and platform`() {
        val location = toggleLocation(building.location)
        val result = domainModelService.changeLocationOfBuilding(location, building, platform)
        assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
    }

    @Test
    fun `changeLocationOfPlatform should change location of platform and building`() {
        val location = toggleLocation(platform.location)
        val result = domainModelService.changeLocationOfPlatform(location, building, platform)
        assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
    }

    @Test
    fun `changeLocationOfBuilding should not change location of building and platform if location is the same`() {
        val location = building.location
        val result = domainModelService.changeLocationOfBuilding(location, building, platform)
        assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
    }

    @Test
    fun `changeLocationOfPlatform should not change location of platform and building if location is the same`() {
        val location = platform.location
        val result = domainModelService.changeLocationOfPlatform(location, building, platform)
        assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
    }
}
```

**Тесты для класса** ```Speaker``` **и** ```Crowd```

```kotlin
@Nested
inner class TestCrowdAndSpeaker {
    @BeforeEach
    fun setUp() {
        crowd = Crowd()
        speaker = Speaker()
    }

    @ParameterizedTest
    @CsvSource(
        "SILENT, SILENT",
        "SAD, PAUSED",
        "JUBILANT, SPEAKING"
    )
    fun `changeCrowdReaction should change reaction of crowd`(reaction: CrowdReaction, speakerState: SpeakerState) {
        speaker.state = speakerState
        assertEquals(reaction, domainModelService.changeCrowdReaction(crowd, speaker, reaction).reaction)
    }

    @Test
    fun `changeSpeakerState should change state of speaker`() {
        assertEquals(SpeakerState.SPEAKING, domainModelService.changeSpeakerState(speaker, crowd).state)
    }

    @Test
    fun `speakerSaidBadSpeech should change reaction of crowd and state of speaker`() {
        val result = domainModelService.speakerSaidBadSpeech(speaker.startSpeaking(), crowd)
        assertEquals(CrowdReaction.ANGRY, result.first.reaction)
        assertEquals(SpeakerState.SILENT, result.second.state)
    }

    @Test
    fun `speakerSaidGoodSpeech should change reaction of crowd and state of speaker`() {
        val result = domainModelService.speakerSaidGoodSpeech(speaker.startSpeaking(), crowd)
        assertEquals(CrowdReaction.JUBILANT, result.first.reaction)
        assertEquals(SpeakerState.SPEAKING, result.second.state)
    }


    @Test
    fun `addPersonToCrowd should add person to crowd`() {
        assertEquals(crowd.people + 1, domainModelService.addPersonToCrowd(crowd).people)
    }

    @Test
    fun `removePersonFromCrowd should remove person from crowd`() {
        assertEquals(crowd.people - 1, domainModelService.removePersonFromCrowd(crowd).people)
    }

    @Test
    fun `removePersonFromCrowd should throw exception if crowd is empty`() {
        crowd.people = 0
        assertThrows<IllegalArgumentException> {
            domainModelService.removePersonFromCrowd(crowd)
        }
    }
}
```

##UML и Domain Model Diagram

### UML

![UML](images/uml.png)


### Domain Model Diagram

![Domain Model Diagram](images/dml.png)




