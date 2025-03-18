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

**Основная функция** - Тестовое покрытие полное, рассматриваются все случаи входных данных, краевые случае,
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

