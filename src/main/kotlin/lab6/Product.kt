package lab6

/**
 * @param R заказ оптового всех потребителей на товар на текущий день
 * @param l минимальное число мест на складе под товар, при котором еще не требуется дополнительных закупок на предприятии
 */
data class Product(
    val R: Int,
    val l: Int
) {

    /**
     * общее число заявок потребителей на товар с первого дня моделирования по текущий день
     */
    var F: Int = 0

    /**
     * суммарные заявки производство товаров предприятия
     */
    var a: Int = 0
    set(value) {
        field = value
        F += value
        Product.A += value
        C = b - value
    }

    /**
     * остаток товара после удовлетворения всех заявок покупателей на текущий день
     */
    var C: Int = 0
    set(value) {
        field = value
        when {
            field < 0 -> {
                b = R
                D += field
                Product.H += field
            }
            field < l -> b = field + R
            else -> b = field
        }
    }

    /**
     * кол-во товара, которого не хватило для удовлетворения заявок потребителей на товар, начиная с первого дня моделирования на текущий день
     */
    var D: Int = 0

    /**
     * кол-во товара на начало дня после произведения закупок всеми потребителями в предыдущий день и выполнения заказов на склада предприятиям на следующий день
     */
    var b: Int = R

    /**
     * доля товара, невыданного потребителям к общему числу заявок за весь период моделирования
     */
    val K: Double
        get() = D.toDouble() / F

    companion object {
        /**
         * суммарная заявка потребителей на все виды товара с первого дня моделирования по текущий день
         */
        var A: Int = 0

        /**
         * общее кол-во неудовлетворенных заявок потребителей за весь период моделирования
         */
        var H: Int = 0

        /**
         * доля всех не выполненных заявок потребителей к общему числу заявок на весь период моделирования
         */
        val L: Double
            get() = H.toDouble() / A
    }
}