package com.example.gulbit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gulbit.R
import com.example.gulbit.model.Word

class WordPagerAdapter(private val words: List<Word>) : RecyclerView.Adapter<WordPagerAdapter.WordViewHolder>() {

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordText: TextView = view.findViewById(R.id.word_text)
        val meaningText: TextView = view.findViewById(R.id.meaning_text)
        val exampleText: TextView = view.findViewById(R.id.example_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_card, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.wordText.text = word.word
        holder.meaningText.text = word.meaning
        holder.exampleText.text = word.example
    }

    override fun getItemCount(): Int = words.size
}
