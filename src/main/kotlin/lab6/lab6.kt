package lab6

import distribution.UniformDistribution

val productList = listOf(
    Product(R = 70, l = 20),
    Product(R = 50, l = 25),
    Product(R = 30, l = 21)
)

val consumers = 3
val consumerList = Array(consumers) {
    Consumer(UniformDistribution(0.0, (it + 1) * 10.0, seed = it))
}.toList()

/**
 * Период моделирования
 */
val G = 20

fun maink() {
    productList.forEachIndexed { index, product ->
        with(product) { println("Товар $index: b=$b, a=$a, C=$C D=$D, F=$F") }
    }
    println()

    repeat(G) { day ->
        println("День ${day + 1}:")
        productList.forEachIndexed { index, product ->
            product.a = consumerList.map { it.a }.sum()
            with(product) { println("Товар $index: b=$b, a=$a, C=$C D=$D, F=$F") }
        }
        println("H=${Product.H}, A=${Product.A}")
        println()
    }

    productList.forEachIndexed { index, product ->
        with(product) { println("Продукт $index: \n R=$R, l=$l, доля выданного товара = ${1 + K}") }
    }

    println("L=${Product.L}")
}

fun main() {
    val productList = listOf(
        Product(R = 70, l = 20),
        Product(R = 50, l = 25),
        Product(R = 30, l = 21)
    )

    val consumerList = Array(3) {
        Consumer(UniformDistribution(0.0, (it + 1) * 10.0, seed = it))
    }.toList()

    println("Начальное состояние:")
    productList.forEachIndexed { index, product ->
        with(product) { println("Товар $index: b=$b, l=$l a=$a, C=$C D=$D, F=$F") }
    }

    val warehouse = Warehouse(consumerList, productList, 70)
    warehouse.start()

    warehouse.history.forEachIndexed { day, dayHistory ->
        println("День ${day + 1}:")
        dayHistory.forEachIndexed { index, historyProduct ->
            print(" Товар ${index + 1}: ")
            with(historyProduct) {
                println("\t Заявок: $a, остаток: $C")
            }
        }
    }

    println("Конец")

    warehouse.history.last().forEachIndexed { index, history ->
        println("Товар ${index + 1}: Выполненные заявки: ${1 + history.K}")
    }

    println()
    println("Доля выполненных заявок: ${1 + Product.L}")

}

class Warehouse(
    val consumerList: List<Consumer>,
    val productList: List<Product>,
    val days: Int
) {
    val history = mutableListOf<MutableList<HistoryProduct>>()

    fun start() {
        history.clear()
        Product.A = 0
        Product.H = 0

        repeat(days) { day ->
            history += mutableListOf<HistoryProduct>()

            productList.forEachIndexed { index, product ->
                product.a = consumerList.map { it.a }.sum()
                history[day].add(product.toHistory())
            }
        }
    }
}