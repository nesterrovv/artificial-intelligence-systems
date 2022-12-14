import GraphsAlgorithms.A_StarSearch
import GraphsAlgorithms.bestFirstSearch
import GraphsAlgorithms.bidirectionalSearch
import GraphsAlgorithms.breadthFirstSearch
import GraphsAlgorithms.depthFirstSearch
import GraphsAlgorithms.depthLimitSearch
import GraphsAlgorithms.iterativeDeepeningDepthFirstSearch
import java.io.File
import java.util.*

object GraphsAlgorithms {
    /**
     * Алгоритм поиск в глубину на графах.
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun depthFirstSearch(start: Vertex, finish: Vertex, limit: Int = Int.MAX_VALUE): Pair<List<Vertex>, Int> {
        val path = Stack<Vertex>()
        //необходимо использовать вложенную функцию, чтобы создать замыкания для переменной path
        fun innerDFS(current: Vertex, limit: Int, visited: Set<Vertex> = setOf()): Boolean =
            when {
                current == finish -> true
                limit == 0 -> false
                else -> {
                    current.getNeighbors().keys.filter { it !in visited }.forEach {
                        if (innerDFS(it, limit - 1, visited + current)) {
                            path.push(it)
                            return true
                        } }
                    false
                }
            }
        innerDFS(start, limit)
        path.push(start) //добавляем в начало пути вершину, из которой начали поиск
        return path.reversed() to path.size - 1 //вычитаем из длины пути вершину, из которой начали поиск
    }

    /**
     * Алгоритм поиска в ширину на графах.
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun breadthFirstSearch(start: Vertex, finish: Vertex): Pair<List<Vertex>, Int> {
        val queue = Stack<Vertex>().apply { this.push(start) }
        val parents = mutableMapOf<Vertex, Vertex?>(start to null) //ключ - вершина, значение - родитель
        val visited = mutableSetOf(start)
        while (queue.isNotEmpty()) {
            val next = queue.pop()
            visited.add(next)
            if (next == finish) {
                val path = generateSequence(next) { parents[it] }.toList().reversed()
                return path to path.size - 1
            }
            next.getNeighbors().keys.filter { it !in visited }.forEach {
                queue.add(it)
                parents[it] = next
            } }
        return listOf(start) to 0
    }

    /**
     * Алгоритм поиск в глубину на графах с ограничением глубины [limit].
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun depthLimitSearch(start: Vertex, finish: Vertex, limit: Int) = depthFirstSearch(start, finish, limit)

    /**
     * Алгоритм поиск с итеративным углублением. Использует в своей реализации [breadthFirstSearch].
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun iterativeDeepeningDepthFirstSearch(start: Vertex, finish: Vertex): Pair<List<Vertex>, Int> =
        generateSequence(1) { it + 1 }.map { depthFirstSearch(start, finish, it) }.find { it.first.size > 1 }?: listOf(start) to 0
    // it.first.size > 1 потому что мы должны проверить не длину пути, а больше ли вершин в пути чем 1

    /**
     * Алгоритм двунаправленного поиск в графе.
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun bidirectionalSearch(start: Vertex, finish: Vertex): Pair<List<Vertex>, Int> {
        val sData = Triple(
            mutableListOf(start),                               //очередь
            mutableSetOf(start),                                //посещённые вершины
            mutableMapOf<Vertex, Vertex?>(start to null)        //родители
        )
        val fData = Triple(
            mutableListOf(finish),
            mutableSetOf(finish),
            mutableMapOf<Vertex, Vertex?>(finish to null)
        )
        fun innerBFS(vertexData: Triple<MutableList<Vertex>, MutableSet<Vertex>, MutableMap<Vertex, Vertex?>>) {
            val current = vertexData.first.first()
            vertexData.first.removeFirst()
            current.getNeighbors().keys.filter { it !in vertexData.second }.forEach {
                vertexData.third[it] = current
                vertexData.second.add(it)
                vertexData.first.add(it)
            }
        }
        while (sData.first.isNotEmpty() && fData.first.isNotEmpty()) {
            innerBFS(sData)
            innerBFS(fData)
            val intersectVertices = sData.second intersect fData.second
            if (intersectVertices.isNotEmpty()) {
                val path = mutableListOf(intersectVertices.first())
                fun buildHalfPath(v: Vertex, data: Triple<MutableList<Vertex>, MutableSet<Vertex>, MutableMap<Vertex, Vertex?>>) {
                    var intersect = intersectVertices.first()
                    while (intersect != v) {
                        val parent = data.third[intersect]?: break
                        path.add(parent)
                        intersect = parent
                    }
                }
                buildHalfPath(start, sData)
                path.reverse()
                buildHalfPath(finish, fData)
                return path to path.size - 1
            }
        }
        return listOf(start) to 0
    }

    private fun buildInformPath(parents: Map<Vertex, Vertex?>, lastVertex: Vertex?): Pair<List<Vertex>, Int> {
        var current = lastVertex
        val path = mutableListOf<Vertex>()
        var roadLength = 0
        while (current != null) {
            path.add(current)
            val parent = parents[current]
            roadLength += current.getNeighbors()[parent]?: 0
            current = parent
        }
        return path.reversed() to roadLength
    }

    /**
     * Алгоритм жадного поиска на графах по первому наилучшему соответствию.
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun bestFirstSearch(start: Vertex, finish: Vertex): Pair<List<Vertex>, Int> {
        val queue = mutableListOf(start)
        val visited = mutableSetOf<Vertex>()
        val parents = mutableMapOf<Vertex, Vertex?>(start to null)
        while (queue.isNotEmpty()) {
            queue.sortByDescending { it.h }
            /*
            Перевернуть очередь необходимо, т.к. я по ошибке определил эвристику в виде пути по прямой от Риги до Уфы,
            а не от Уфы до Риги. Без него алгоритм всё-равно отработает верно, но с большим количеством шагов
             */
            val current = queue.removeFirstOrNull()!!
            visited.add(current)
            if (current == finish) return buildInformPath(parents, current)
            current.getNeighbors().keys.filter { it !in visited }.forEach { v ->
                if (queue.none { v == it && v.h >= it.h }) {
                    queue.add(v)
                    parents[v] = current
                }
            }
        }
        return listOf(start) to 0
    }

    /**
     * Поиск методом минимизации суммарной оценки А* на графах.
     * @return путь от [start] до [finish] и его длину (если веса рёбер не заданы, то они = 1).
     */
    fun A_StarSearch(start: Vertex, finish: Vertex): Pair<List<Vertex>, Int> {
        //используем очередь с приоритетом, чтобы автоматически сортировать очередь по параметру Vertex.f
        val queue = PriorityQueue<Vertex>().apply { this.add(start) }
        val visited = mutableSetOf<Vertex>()
        val parents = mutableMapOf<Vertex, Vertex?>(start to null)
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            visited.add(current)
            if (current == finish) return buildInformPath(parents, current)
            current.getNeighbors().keys.filter { it !in visited }.forEach { v ->
                v.g = current.getNeighbors()[v]?: 0
                if (queue.none { v == it && v.g >= it.g }) {
                    queue.add(v)
                    parents[v] = current
                }
            }
        }
        return listOf(start) to 0
    }
}

fun main(args: Array<String>) {
    val vertices = File("${args[0]}heuristics.txt").readLines()
        .map { it.split(" ") }
        .map { Vertex(it[0]).apply { this.h = it[1].toInt() } }
        .toSet()
    File("${args[0]}graphData.txt").readLines().map { it.split(" ") }.forEach { r ->
        val (a, b) = vertices.filter { it.name == r[0] || it.name == r[1] }
        a.link(b, r[2].toInt())
    }
    val (a, b) = vertices.filter { it.name == "Брест" || it.name == "Казань" }
    println("""
        DFS ${depthFirstSearch(a, b)}
        BFS ${breadthFirstSearch(a, b)}
        DLS ${depthLimitSearch(a, b, 5)}
        IDDFS ${iterativeDeepeningDepthFirstSearch(a, b)}
        BDS ${bidirectionalSearch(a, b)}
        BFS ${bestFirstSearch(a, b)}
        A* ${A_StarSearch(a, b)}
    """.trimIndent()
    )
}