package ayds.songinfo.home.model.entities

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val spotifyUrl: String,
        val imageUrl: String,
        val releaseDatePrecision: String,
        var isLocallyStored: Boolean = false
    ) : Song() {

        fun releaseDate(): String {
            return when(releaseDatePrecision) {
                "year" -> "$releaseDate (${if (isLeapYear(releaseDate.toInt())) "" else "not "}a leap year)"
                "month" -> {
                    val splitReleaseDate = releaseDate.split('-')
                    "${monthToText(splitReleaseDate[1].toInt())}, ${splitReleaseDate[0]}"
                }
                "day" -> {
                    val splitReleaseDate = releaseDate.split('-')
                    "${splitReleaseDate[2]}/${splitReleaseDate[1]}/${splitReleaseDate[0]}"
                }
                else -> releaseDate
            }
        }

        private fun isLeapYear(year: Int): Boolean =
            (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))

        private fun monthToText(month: Int): String = when(month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
    }

    object EmptySong : Song()
}

