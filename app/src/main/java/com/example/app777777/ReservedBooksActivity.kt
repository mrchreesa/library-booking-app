package com.example.app777777

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReservedBooksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserved_books)

        val recyclerView = findViewById<RecyclerView>(R.id.reservedBooksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get reservations from intent
        val reservations = intent.getParcelableArrayListExtra<Reservation>("reservations")
        
        // Set up adapter with reservations
        val adapter = ReservedBooksAdapter(reservations ?: emptyList())
        recyclerView.adapter = adapter
    }
} 