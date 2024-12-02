import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Reservation(
    val book: Book,
    val startDate: String,
    val endDate: String
) : Parcelable