 package com.example.app777777

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    private var books: List<Book>,
    private val reservations: List<Reservation>,
    private val onReserveClick: (Book) -> Unit,
    private val onUnreserveClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookName: TextView = view.findViewById(R.id.bookName)
        val bookAuthor: TextView = view.findViewById(R.id.bookAuthor)
        val bookPublisher: TextView = view.findViewById(R.id.bookPublisher)
        val bookImage: ImageView = view.findViewById(R.id.bookImage)
        val reserveButton: Button = view.findViewById(R.id.reserveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_card, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        
        holder.bookName.text = book.bookName
        holder.bookAuthor.text = book.bookAuthors
        holder.bookPublisher.text = book.bookPublisher
        
        // Load book image using Glide
        Glide.with(holder.bookImage.context)
            .load(book.bookImage)

            .into(holder.bookImage)

        // Check if book is reserved
        val isReserved = reservations.any { it.book.bookName == book.bookName }
        
        // Update button text and click listener based on reservation status
        if (isReserved) {
            holder.reserveButton.text = "Cancel Reservation"
            holder.reserveButton.setOnClickListener { onUnreserveClick(book) }
        } else {
            holder.reserveButton.text = "Reserve"
            holder.reserveButton.setOnClickListener { onReserveClick(book) }
        }
    }

    override fun getItemCount() = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}