package com.example.vocabbuilder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val ADD_WORD_CODE = 301

    private val defns = ArrayList<String>()
    private lateinit var myAdapter : ArrayAdapter<String>
    private val words = ArrayList<String>()
    private val wordToDefn = HashMap<String, String>()
    private var points = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val reader1 = Scanner(resources.openRawResource(R.raw.words))
        readFromFile(reader1)
        val reader2 = Scanner(resources.openRawResource(R.raw.extrawords))
        readFromFile(reader2)
        setupList()
        updatePoints()
        definitions_list.setOnItemClickListener { _, _, index, _ ->
            if(wordToDefn[the_word.text] == defns[index]) {
                defns.removeAt(index)
                words.remove(the_word.text)
                myAdapter.notifyDataSetChanged()
                points++
                setupList()
            } else {
                points--
                Toast.makeText(applicationContext,"Wrong Option. Please Try Again", Toast.LENGTH_SHORT).show()
            }
            updatePoints()
        }
    }

    private fun updatePoints() {
        points_box.text = String.format("Points: %d", points)
    }

    private fun readFromFile(reader: Scanner) {
        while(reader.hasNextLine()) {
            val line = reader.nextLine()
            val pieces = line.split(" ")
            wordToDefn.put(pieces[0], pieces[1])
            words.add(pieces[0])
        }
    }

    private fun setupList() {
        val rand = Random()
        val index = rand.nextInt(words.size)
        val word = words[index]
        the_word.text = word
        defns.clear()
        defns.add(wordToDefn[word]!!)
        words.shuffle()
        for(otherWord in words) {
            if(otherWord == word) {
                continue
            }
            defns.add(wordToDefn[otherWord]!!)
        }
        defns.shuffle()
        myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, defns)
        definitions_list.adapter = myAdapter
    }

    fun addItemOnClick(view: View) {
        val myIntent = Intent(this, AddItemActivity::class.java)
        startActivityForResult(myIntent, ADD_WORD_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_WORD_CODE) {
            val word = data?.getStringExtra("word")
            val defn = data?.getStringExtra("defn")
            defns.add(defn!!)
            words.add(word!!)
            wordToDefn.put(word!!, defn!!)
        }
    }

    override fun onResume() {
        super.onResume()
        setupList()
    }
}
