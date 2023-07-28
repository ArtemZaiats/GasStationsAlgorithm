import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Double, val y: Double)

class Tree(points: List<Point>) {
    private data class Node(val point: Point, val left: Node?, val right: Node?)

    private var root: Node? = null

    init {
        root = buildTree(points, 0)
    }

    private fun buildTree(points: List<Point>, depth: Int): Node? {
        if (points.isEmpty()) return null

        val axis = depth % 2 // 0 - ось x, 1 - ось y
        points.sortedBy { if (axis == 0) it.x else it.y }

        val medianIndex = points.size / 2
        val medianPoint = points[medianIndex]

        val leftPoints = points.subList(0, medianIndex)
        val rightPoints = points.subList(medianIndex + 1, points.size)

        return Node(
            medianPoint,
            buildTree(leftPoints, depth + 1),
            buildTree(rightPoints, depth + 1)
        )
    }

    fun findPointsWithinRadius(target: Point, radius: Double): List<Point> {
        val result = mutableListOf<Point>()

        fun searchPoints(node: Node?, depth: Int) {
            if (node == null) return

            val axis = depth % 2
            val currentPoint = node.point

            val currentDistance = distance(target, currentPoint)
            if (currentDistance <= radius) {
                result.add(currentPoint)
            }

            val nextAxis = axis + 1

            val nearerNode: Node?
            val furtherNode: Node?

            if ((if (nextAxis == 0) target.x < currentPoint.x else target.y < currentPoint.y)) {
                nearerNode = node.left
                furtherNode = node.right
            } else {
                nearerNode = node.right
                furtherNode = node.left
            }

            searchPoints(nearerNode, depth + 1)

            if ((if (nextAxis == 0) target.x - radius <= currentPoint.x else target.y - radius <= currentPoint.y)) {
                searchPoints(furtherNode, depth + 1)
            }
        }

        searchPoints(root, 0)
        return result
    }

    private fun distance(point1: Point, point2: Point): Double {
        return sqrt((point1.x - point2.x).pow(2) + (point1.y - point2.y).pow(2))
    }
}

fun main() {
    val stations = listOf(
        Point(2.0, 3.0),
        Point(4.0, 5.0),
        Point(1.0, 2.0),
        Point(7.0, 8.0),
        Point(5.0, 6.0),
        Point(5.0, 4.0),
        Point(4.0, 4.0),
        Point(2.0, 6.0),
        Point(7.0, 7.0)
    )

    val route = listOf(
        Point(3.0, 4.0),
        Point(3.0, 5.0),
        Point(6.0, 7.0)
    )

    val radius = 3.0

    val tree = Tree(stations)

    for (point in route) {
        val nearbyStations = tree.findPointsWithinRadius(point, radius)
        println("For point $point with radius $radius nearby stations: $nearbyStations")
    }
}