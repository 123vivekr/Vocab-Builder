package com.example.vocabbuilder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_add_item.*
import java.io.PrintStream

class AddItemActivity : AppCompatActivity() {
    private val WORDS_FILE_NAME = "extrawords.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
    }

    fun addTheWord(view: View) {
        val word = word_to_add.toString()
        val defn = definition_to_add.toString()
        val line = "$word $defn"

        val outStream = PrintStream(openFileOutput(WORDS_FILE_NAME, MODE_PRIVATE))
        outStream.use {
            outStream.println(line)
        }

        val myIntent = Intent()
        myIntent.putExtra("word", word)
        myIntent.putExtra("defn", defn)
        Log.i("return")
        setResult(RESULT_OK, myIntent)
        finish()
    }
}
