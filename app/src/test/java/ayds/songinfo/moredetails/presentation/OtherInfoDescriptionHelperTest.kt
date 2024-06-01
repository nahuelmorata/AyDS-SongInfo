package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import org.junit.Assert
import org.junit.Test

class OtherInfoDescriptionHelperTest {
    private val otherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

    @Test
    fun `given a article it should has header and footer of html`() {
        val article = Card.ArtistBiography(
            "name",
            "biography",
            "article",
            false,
        )

        val result = otherInfoDescriptionHelper.getDescription(article)

        Assert.assertTrue(result.startsWith("<html>"))
        Assert.assertTrue(result.endsWith("</html>"))
        Assert.assertFalse(result.contains("[*]"))
    }

    @Test
    fun `given a local article it should has local mark, header and footer of html`() {
        val article = Card.ArtistBiography(
            "name",
            "biography",
            "article",
            true,
        )

        val result = otherInfoDescriptionHelper.getDescription(article)

        Assert.assertTrue(result.contains("[*]"))
        Assert.assertTrue(result.startsWith("<html>"))
        Assert.assertTrue(result.endsWith("</html>"))
    }
}