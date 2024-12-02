package com.example.app777777

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReservedBooksAdapter(
    private val reservations: List<Reservation>
) : RecyclerView.Adapter<ReservedBooksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.bookNameText.text = "Book: ${reservation.book.bookName}"
        holder.bookAuthorText.text = "Author: ${reservation.book.bookAuthors}"
        holder.reservationDatesText.text = "Reserved: ${reservation.startDate} to ${reservation.endDate}"
    }

    override fun getItemCount() = reservations.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookNameText: TextView = view.findViewById(R.id.bookNameText)
        val bookAuthorText: TextView = view.findViewById(R.id.bookAuthorText)
        val reservationDatesText: TextView = view.findViewById(R.id.reservationDatesText)
    }
} 