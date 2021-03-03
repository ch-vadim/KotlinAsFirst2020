package lesson11.task1

import java.lang.StringBuilder

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная, общая ценность в баллах -- 32.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
class UnsignedBigInteger : Comparable<UnsignedBigInteger> {
    private val listOfDigits = mutableListOf<Int>()

    /**
     * Конструктор из строки
     */
    constructor(s: String) {
        if (!s.contains(Regex("""\d+"""))) throw IllegalStateException("Invalid format")
        if (s.matches(Regex("""0*"""))) {
            listOfDigits.add(0)
            return
        }
        val result = mutableListOf<Int>()
        for (element in s) {
            result.add(element.toString().toInt())
        }
        listOfDigits.addAll(result.dropWhile { it == 0 })
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
        if (i < 0) throw IllegalStateException("Invalid format")
        if (i == 0) {
            listOfDigits.add(0)
            return
        }
        var num = i
        while (num != 0) {
            listOfDigits.add(0, (num % 10))
            num /= 10
        }
    }

    constructor(l: MutableList<Int>) {
        val result = l.dropWhile { it == 0 }
        if (result.isEmpty()) listOfDigits.add(0) else listOfDigits.addAll(result)
        return
    }

    private constructor(digit: Int, zeros: Int) {
        listOfDigits.add(digit)
        repeat(zeros) {
            listOfDigits.add(0)
        }
    }

    companion object {
        val maxValueInt = UnsignedBigInteger(Int.MAX_VALUE)
    }

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        val result = mutableListOf<Int>()
        var rem = 0
        val ls = listOfDigits.size
        val ols = other.listOfDigits.size
        val m = maxOf(ls, ols) + 1
        for (i in 1..m) {
            val r = listOfDigits.getOrElse(ls - i) { 0 } + other.listOfDigits.getOrElse(ols - i) { 0 } + rem
            result.add(0, r % 10)
            rem = r / 10
        }
        return UnsignedBigInteger(result)
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other > this) throw ArithmeticException("negative number")
        val result = mutableListOf<Int>()
        val dif = listOfDigits.size - other.listOfDigits.size
        var flag = false
        val l = listOfDigits.subList(dif, listOfDigits.size)
        for (i in other.listOfDigits.size - 1 downTo 0) {
            if (l[i] < other.listOfDigits[i]) {
                val d = if (flag) 9 else 10
                result.add(0, l[i] + d - other.listOfDigits[i])
                flag = true
            } else {
                val p = if (flag) -1 else 0
                result.add(0, l[i] + p - other.listOfDigits[i])
                flag = false
            }
        }
        for (i in dif - 1 downTo 0) {
            if (flag) {
                if (listOfDigits[i] == 0) {
                    result.add(0, 9)
                } else {
                    result.add(0, listOfDigits[i] - 1)
                    flag = false
                }
            } else result.add(0, listOfDigits[i])
        }
        return UnsignedBigInteger(result)
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        var rem = 0
        var result = UnsignedBigInteger(0)
        for (i in other.listOfDigits.size - 1 downTo 0) {
            for (j in listOfDigits.size - 1 downTo 0) {
                result += UnsignedBigInteger(
                    (other.listOfDigits[i] * listOfDigits[j] + rem) % 10,
                    listOfDigits.size - 1 - j + other.listOfDigits.size - 1 - i
                )
                rem = (other.listOfDigits[i] * listOfDigits[j] + rem) / 10
            }
            result += UnsignedBigInteger(rem, listOfDigits.size + other.listOfDigits.size - 1 - i)
            rem = 0
        }
        return result
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException("/0")
        if (other > this) return UnsignedBigInteger(0)
        var rem = UnsignedBigInteger(0)
        val t = UnsignedBigInteger(10)
        var num = 0
        var i = 0
        val result = mutableListOf<Int>()
        while (rem <= other) {
            rem = rem * t + UnsignedBigInteger(listOfDigits[i])
            i++
        }
        while (i <= (listOfDigits.size - 1)) {
            while (rem >= other * UnsignedBigInteger(num)) {
                num++
            }
            num--
            result.add(num)
            rem = (rem - UnsignedBigInteger(num) * other) * t + UnsignedBigInteger(listOfDigits[i])
            num = 0
            i++
        }
        while (rem >= other * UnsignedBigInteger(num)) {
            num++
        }
        num--
        result.add(num)
        return UnsignedBigInteger(result)
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException("%0")
        return this - (this / other) * other
    }

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean {
        if (other is UnsignedBigInteger && listOfDigits == other.listOfDigits) {
            return true
        }
        return false
    }

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        if (listOfDigits.size > other.listOfDigits.size) return 1
        if (listOfDigits.size < other.listOfDigits.size) return -1
        for (i in 0 until listOfDigits.size) {
            if (listOfDigits[i] > other.listOfDigits[i]) return 1
            if (listOfDigits[i] < other.listOfDigits[i]) return -1
        }
        return 0
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String {
        val result = StringBuilder()
        for (i in 0 until listOfDigits.size)
            result.append(listOfDigits[i].toString())
        return result.toString()
    }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (this > maxValueInt) throw ArithmeticException("number > MaxValue")
        var result = 0
        for (i in listOfDigits) {
            result = result * 10 + i
        }
        return result
    }

    override fun hashCode(): Int = listOfDigits.hashCode()
}