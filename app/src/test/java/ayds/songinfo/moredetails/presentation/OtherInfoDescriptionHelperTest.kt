package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards
import org.junit.Assert
import org.junit.Test

class OtherInfoDescriptionHelperTest {
    private val otherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

    @Test
    fun `given a article it should has header and footer of html`() {
        val article = Cards.Card(
            "name",
            "biography",
            "article",
            CardSource.LastFm,
        )

        val result = otherInfoDescriptionHelper.getDescription(article)

        Assert.assertTrue(result.startsWith("<html>"))
        Assert.assertTrue(result.endsWith("</html>"))
        Assert.assertFalse(result.contains("[*]"))
    }

    @Test
    fun `given a local article it should has local mark, header and footer of html`() {
        val article = Cards.Card(
            "name",
            "biography",
            "article",
            CardSource.LastFm,
        )

        val result = otherInfoDescriptionHelper.getDescription(article)

        Assert.assertTrue(result.contains("[*]"))
        Assert.assertTrue(result.startsWith("<html>"))
        Assert.assertTrue(result.endsWith("</html>"))
    }
}