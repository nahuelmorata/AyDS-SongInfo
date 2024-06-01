package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.ArticleService
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import io.mockk.every
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleRepositoryTest {
    private val articleLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val articleService: ayds.artist.external.lastfm.data.ArticleService = mockk(relaxUnitFun = true)

    private val articleRepository: CardRepository =
        CardRepositoryImpl(articleLocalStorage, articleService)

    @Test
    fun `given existing article by artist name should return article and mark it as local`() {
        val article =
            Card.ArtistBiography(
                "name",
                "biography",
                "url",
                false
            )
        every { articleLocalStorage.getCardByName("name") } returns article

        val result = articleRepository.getCard("name")

        assertEquals(article, result)
        assertTrue(article.isStoredLocally)
    }

    @Test
    fun `given non existing song by term should get the song and store it`() {
        val article =
            Card.ArtistBiography(
                "name",
                "biography",
                "url",
                false
            )
        every { articleLocalStorage.getCardByName("name") } returns null
        every { articleService.getArticle("name") } returns article

        val result = articleRepository.getCard("name")

        assertEquals(article, result)
        assertFalse(article.isStoredLocally)
        verify { articleLocalStorage.insertCard("name", article) }
    }

    @Test
    fun `given non existing song by term should return empty song`() {
        every { articleLocalStorage.getCardByName("name") } returns null
        every { articleService.getArticle("name") } returns null

        val result = articleRepository.getCard("name")

        assertEquals("", result.biography)
    }
}