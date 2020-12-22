package minesweeper
import java.util.*

class MineField(val size: Int, var mines: Int) {
    val realField = Array(size) { CharArray(size) { '.' } }
    private val minesCoordinates = mutableListOf<Pair<Int, Int>>()
    val markedCoordinates = mutableListOf<Pair<Int, Int>>()
init {
    val scanner = Scanner(System.`in`)
    printField()
    val y = scanner.nextInt() - 1
    val x = scanner.nextInt() - 1
    val mineOrFree = scanner.next()
    checkCell(x, y, mineOrFree = "init")
}
fun start(){
        if (mines > size * size) {
            mines = size * size - 1
        }
//        installMines()
        printField()
        if (win()) {
            println("Congratulations! You found all the mines!")
        } else println("You stepped on a mine and failed!")
    }
    fun installMines(x: Int, y: Int) {
        var installedMines = 0
        while (installedMines < mines) {
            val xMine = Random().nextInt(size)
            val yMine = Random().nextInt(size)
            if (!(Pair(xMine, yMine) in minesCoordinates) && Pair(xMine, yMine) != Pair(x,y)) {
                //realField[xMine][yMine] = 'X'
                installedMines++
                minesCoordinates += Pair(xMine, yMine)
            }
        }
    }
    fun showNumbers(x: Int, y: Int) {
        if (Pair(x, y) !in minesCoordinates && realField[x][y] != '/' && realField[x][y] !in '1'..'8') {
            var minesAround = 0
            for (a in x - 1..x + 1) {
                for (b in y - 1..y + 1) {
                    if (a < 0 || b < 0 || a >= size || b >= size) {
                        continue
                    } else {
                        if (Pair(a, b) in minesCoordinates) {
                            minesAround++
                        }
                    }
                }
            }
            when (minesAround) {
                0 -> {
                    realField[x][y] = '/'
                    for (a in x - 1..x + 1) {
                        for (b in y - 1..y + 1) {
                            if (a < 0 || b < 0 || a >= size || b >= size) {
                                continue
                            } else {
                                showNumbers(a, b)
                            }
                        }
                    }
                }
                in 1..8 -> realField[x][y] = minesAround.toString().toCharArray()[0] //???
            }
        }
    }
    fun printField(mode: String = "") {
        if (mode == "showMines"){
            for (x in 0 until size) {
                for (y  in 0 until size) {
                       if (Pair(x, y) in minesCoordinates) {
                            realField[x][y] = 'X'
                       }
                }
            }
        }
        println(" │123456789│")
        println("—│—————————│")
        for (d in realField.indices) {
            print("${d + 1}|")
            print(realField[d])
            print("|")
            println()
        }
        println("—│—————————│")
    }
    fun checkCell(x: Int, y: Int, mineOrFree: String): String {
        if (x in 0 until size && y in 0 until size) {
            when (mineOrFree) {
                "mine" -> {
                    when (realField[x][y]) {
                        '.' -> {
                            realField[x][y] = '*'
                            markedCoordinates += Pair(x, y)
                            printField()
                        }
                        '*' -> {
                            realField[x][y] = '.'
                            markedCoordinates -= Pair(x, y)
                            printField()
                        }
                        in '1'..'8' -> println("There is a number here!")
                        else -> println("Why are you here?")
                    }
                }
                "free" -> {
                    when (Pair(x, y)) {
                        in minesCoordinates.toSet() -> {
                            return "bang!"
                        }
                        else -> {
                            showNumbers(x, y)
                            printField()
                        }
                    }
                }
                "init" -> {
                    installMines(x, y)
                    checkCell(x, y, "free")
                }
                else -> println("Error!!!!!11")
            }
        }
        return "ok"
    }
    fun win(): Boolean {
        val scanner = Scanner(System.`in`)
        var cellsLeft: Int = 0
        var minesPlaced: Int = 0
        while (markedCoordinates.toSet() != minesCoordinates.toSet()) {
            cellsLeft = 0
            minesPlaced = 0
            for (aa in realField){
                for (a in aa) {
                    if (a.equals('.')) {
                        cellsLeft++
                    }
                    if (a.equals('*')){
                        minesPlaced++
                    }
                }
            }
           if (mines == (cellsLeft + minesPlaced)){
                return true
            }
            println("Set/unset mine marks or claim a cell as free:")
            val y = scanner.nextInt() - 1
            val x = scanner.nextInt() - 1
            val mineOrFree = scanner.next()
            if (checkCell(x, y, mineOrFree) == "bang!") {
                printField("showMines")
                return false
            }
        }
        return true
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    //print("Size = ")
    //val size = scanner.nextInt()
    //  println()
    val size = 9
    println("Enter number of mines (0 - ${size * size - 1})")
    print("Mines = ")
    val mines = scanner.nextInt()
    val game = MineField(size, mines)
    game.start()
}
