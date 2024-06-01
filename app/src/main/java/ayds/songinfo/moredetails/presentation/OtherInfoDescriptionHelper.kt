package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Cards
import java.util.Locale

interface OtherInfoDescriptionHelper {
    fun getDescription(artistBiography: Cards.Card): String
}

private const val HEADER_HTML = "<html><div width=400><font face=\"arial\">"

private const val FOOTER_HTML = "</font></div></html>"

internal class OtherInfoDescriptionHelperImpl : OtherInfoDescriptionHelper {
    override fun getDescription(artistBiography: Cards.Card): String {
        val text = getFormattedDescription(artistBiography)
        return textToHtml(text, artistBiography.name)
    }

    private fun getFormattedDescription(artistBiography: Cards.Card): String {
        val prefix = if (artistBiography.isStoredLocally) "[*]" else ""
        val text = artistBiography.description.replace("\\n", "\n")
        return "$prefix$text"
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HEADER_HTML)
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(FOOTER_HTML)
        return builder.toString()
    }
}