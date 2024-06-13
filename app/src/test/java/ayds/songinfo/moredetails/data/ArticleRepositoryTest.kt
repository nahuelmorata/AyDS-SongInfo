package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.card.CardBroker
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.Cards
import ayds.songinfo.moredetails.domain.CardRepository
import io.mockk.mockk
import org.junit.Assert.assertEquals
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleRepositoryTest {
    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val cardBroker: CardBroker = mockk(relaxUnitFun = true)

    private val articleRepository: CardRepository =
        CardRepositoryImpl(cardLocalStorage, cardBroker)

    @Test
    fun `given existing article by artist name should return article and mark it as local`() {
        val article =
            Cards.Card(
                "name",
                "biography",
                "url",
            )
        every { cardLocalStorage.getCards("name") } returns listOf(article)

        val result = articleRepository.getCards("name")

        assertEquals(result.size, 1)
        assertEquals(result.first(), article)
        assertTrue(article.isStoredLocally)
    }

    @Test
    fun `given non existing song by term should get the song and store it`() {
        val articles = listOf(Cards.Card(
            "name",
            "biography",
            "url",
        ))
        every { cardLocalStorage.getCards("name") } returns listOf()
        every { cardBroker.getCards("name") } returns articles

        val result = articleRepository.getCards("name")

        assertEquals(result.size, 1)
        assertFalse(result.first().isStoredLocally)
        verify { cardLocalStorage.saveCards("name", articles) }
    }

    @Test
    fun `given non existing song by term should return empty song`() {
        every { cardLocalStorage.getCards("name") } returns listOf()
        every { cardBroker.getCards("name") } returns listOf()

        val result = articleRepository.getCards("name")

        assertEquals(result.size, 0)
    }
}