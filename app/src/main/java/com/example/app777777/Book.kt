import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val bookName: String? = null,
    val bookAuthors: String? = null,
    val bookPublisher: String? = null,
    val bookImage: String? = null
) : Parcelable