package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.SpotifySong

interface ReleaseDateResolverFactory {
    fun get(song: SpotifySong): ReleaseDateResolver
}

internal class ReleaseDateResolverFactoryImpl : ReleaseDateResolverFactory {
    override fun get(song: SpotifySong): ReleaseDateResolver {
        return when(song.releaseDatePrecision) {
            "year" -> ReleaseDateYearResolverImpl(song)
            "month" -> ReleaseDateMonthResolverImpl(song)
            "day" -> ReleaseDateDayResolverImpl(song)
            else -> ReleaseDateDefaultResolverImpl(song)
        }
    }

}

interface ReleaseDateResolver {
    val song: SpotifySong
    fun format(): String
}

internal class ReleaseDateYearResolverImpl(
    override val song: SpotifySong
) : ReleaseDateResolver {
    override fun format(): String =
        "$song.releaseDate (${if (isLeapYear(song.releaseDate.toInt())) "" else "not "}a leap year)"

    private fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
}

internal class ReleaseDateMonthResolverImpl(
    override val song: SpotifySong
) : ReleaseDateResolver {
    override fun format(): String {
        val splitReleaseDate = song.releaseDate.split('-')
        return "${monthToText(splitReleaseDate[1].toInt())}, ${splitReleaseDate[0]}"
    }

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

internal class ReleaseDateDayResolverImpl(
    override val song: SpotifySong
) : ReleaseDateResolver {
    override fun format(): String {
        val splitReleaseDate = song.releaseDate.split('-')
        return "${splitReleaseDate[2]}/${splitReleaseDate[1]}/${splitReleaseDate[0]}"
    }
}

internal class ReleaseDateDefaultResolverImpl(
    override val song: SpotifySong
) : ReleaseDateResolver {
    override fun format(): String = song.releaseDate

}