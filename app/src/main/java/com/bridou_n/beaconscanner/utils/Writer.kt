package com.bridou_n.beaconscanner.utils

import android.os.Environment.getExternalStorageDirectory
import java.io.File
import java.io.FileOutputStream


class Writer(var fileName: String) {
    var mode = Mode.TEXT

    /**
     * インスタンス生成時に，指定されたファイル名が，すでに作られたファイルであれば，元のファイルを削除する
     */
    init {
        if (File("${getExternalStorageDirectory().path}/${this.fileName}").exists()) {
            File("${getExternalStorageDirectory().path}/${this.fileName}").delete()
        }
    }

    companion object {
        enum class Mode {
            CSV, TEXT, JSON
        }
    }

    fun write(message: String): Boolean {
        return when (mode) {
            Mode.CSV -> {
                writeCSV(message)
            }
            Mode.TEXT -> {
                writeText(message)
            }
            Mode.JSON -> {
                writeJson(message)
            }
        }
    }

    fun write(message: ArrayList<String>): Boolean {
        return when (mode) {
            Mode.CSV -> {
                writeCSV(message)
            }
            Mode.TEXT -> {
                writeText(message)
            }
            Mode.JSON -> {
                writeJson(message)
            }
        }
    }

    fun modeSelect(newMode: Mode): Writer {
        mode = newMode
        fileName = fileName.split(".")
                .let { it.take(it.size - 1) }
                .reduce { s1, s2 -> "$s1.$s2" }
                .toString()
                .let {
                    when (mode) {
                        Mode.TEXT -> "$it.txt"
                        Mode.JSON -> "$it.json"
                        Mode.CSV -> "$it.csv"
                    }
                }
        return this
    }

    fun addInitialColumn(column: ArrayList<String>): Writer {
        val initialColumn = column.reduce { s1, s2 -> "$s1,$s2" }.toString()
        writeCSV(initialColumn)
        return this
    }

    fun changeFilename(fileName: String) {
        this.fileName = fileName
    }

    private fun writeCSV(message: String): Boolean {
        if (!this.fileName.endsWith(".csv")) {
            return false
        }
        val fos: FileOutputStream?
        fos = FileOutputStream("${getExternalStorageDirectory().path}/${this.fileName}", true)
        fos.write(message.toByteArray())
        fos.close()
        return true
    }

    private fun writeCSV(elements: ArrayList<String>): Boolean {
        if (!this.fileName.endsWith(".csv")) {
            return false
        }
        val message = "\n" + elements.reduce { s1, s2 -> "$s1,$s2" }.toString()
        val fos: FileOutputStream?
        fos = FileOutputStream("${getExternalStorageDirectory().path}/${this.fileName}", true)
        fos.write(message.toByteArray())
        fos.close()
        return true
    }

    private fun writeJson(message: String): Boolean = TODO("JSON文字列でファイルに出力するやつ　あとで実装する")
    private fun writeJson(message: ArrayList<String>): Boolean = TODO("JSON文字列でファイルに出力するやつ　あとで実装する")


    private fun writeText(message: String): Boolean {
        val fos: FileOutputStream?
        fos = FileOutputStream("${getExternalStorageDirectory().path}/${this.fileName}", true)
        fos.write(message.toByteArray())
        fos.close()
        return true
    }

    private fun writeText(message: ArrayList<String>): Boolean = TODO("あとで実装するかもしれない")
}