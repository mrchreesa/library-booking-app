package com.example.app777777

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Dialog
import android.widget.TextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import android.content.Intent
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Book(
    val bookName: String? = null,
    val bookAuthors: String? = null,
    val bookPublisher: String? = null,
    val bookImage: String? = null
) : Parcelable

@Parcelize
data class Reservation(
    val book: Book,
    val startDate: String,
    val endDate: String
) : Parcelable

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "FirebaseData"
    }

    private lateinit var bookAdapter: BookAdapter
    private val reservations = mutableListOf<Reservation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        bookAdapter = BookAdapter(
            emptyList(),
            reservations,
            onReserveClick = { book -> showDateRangePicker(book) },
            onUnreserveClick = { book -> handleUnreserve(book) }
        )
        recyclerView.adapter = bookAdapter

        val database = Firebase.database("https://bookreserve-9c6ab-default-rtdb.firebaseio.com/")
        val db = database.getReference("/")

        db.get().addOnSuccessListener { dataSnapshot ->
            try {
                if (dataSnapshot.exists()) {
                    val bookList = dataSnapshot.children.mapNotNull { 
                        it.getValue(Book::class.java)
                    }
                    bookAdapter.updateBooks(bookList)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing data: ${e.message}", e)
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Failed to fetch data: ${exception.message}", exception)
        }
    }

    private fun handleUnreserve(book: Book) {
        reservations.removeAll { it.book.bookName == book.bookName }
        bookAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Reservation cancelled for: ${book.bookName}", Toast.LENGTH_SHORT).show()
    }

    private fun showDateRangePicker(book: Book) {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates for ${book.bookName}")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { dateRange ->
            val startDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(Date(dateRange.first))
            val endDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(Date(dateRange.second))
            
            showReservationDialog(book, startDate, endDate)
        }

        dateRangePicker.show(supportFragmentManager, "date_range_picker")
    }

    private fun showReservationDialog(book: Book, startDate: String, endDate: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.reservation_dialog)

        val window = dialog.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
       

        val reservationsList = dialog.findViewById<LinearLayout>(R.id.reservationsList)
        val closeButton = dialog.findViewById<Button>(R.id.closeButton)
        val checkoutButton = dialog.findViewById<Button>(R.id.checkoutButton)

        reservations.add(Reservation(book, startDate, endDate))
        bookAdapter.notifyDataSetChanged()

        reservationsList.removeAllViews()
        
        for (reservation in reservations) {
            val itemView = layoutInflater.inflate(R.layout.reservation_item, reservationsList, false)
            
            val bookNameText = itemView.findViewById<TextView>(R.id.bookNameText)
            val bookAuthorText = itemView.findViewById<TextView>(R.id.bookAuthorText)
            val reservationDatesText = itemView.findViewById<TextView>(R.id.reservationDatesText)

            bookNameText.text = "Book: ${reservation.book.bookName}"
            bookAuthorText.text = "Author: ${reservation.book.bookAuthors}"
            reservationDatesText.text = "Reserved: ${reservation.startDate} to ${reservation.endDate}"

            reservationsList.addView(itemView)
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        checkoutButton.setOnClickListener {
            val intent = Intent(this, ReservedBooksActivity::class.java)
            intent.putParcelableArrayListExtra("reservations", ArrayList(reservations))
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}