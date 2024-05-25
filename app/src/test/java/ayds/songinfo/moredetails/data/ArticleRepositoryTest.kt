package ayds.songinfo.moredetails.data

import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.moredetails.data.article.ArticleService
import ayds.songinfo.moredetails.data.article.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import io.mockk.every
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleRepositoryTest {
    private val articleLocalStorage: ArticleLocalStorage = mockk(relaxUnitFun = true)
    private val articleService: ArticleService = mockk(relaxUnitFun = true)

    private val articleRepository: ArticleRepository =
        ArticleRepositoryImpl(articleLocalStorage, articleService)

    @Test
    fun `given existing article by artist name should return article and mark it as local`() {
        val article =
            Article.ArtistBiography(
                "name",
                "biography",
                "url",
                false
            )
        every { articleLocalStorage.getArticleByArtistName("name") } returns article

        val result = articleRepository.getArticleByArtistName("name")

        assertEquals(article, result)
        assertTrue(article.isStoredLocally)
    }

    @Test
    fun `given non existing song by term should get the song and store it`() {
        val article =
            Article.ArtistBiography(
                "name",
                "biography",
                "url",
                false
            )
        every { articleLocalStorage.getArticleByArtistName("name") } returns null
        every { articleService.getArticle("name") } returns article

        val result = articleRepository.getArticleByArtistName("name")

        assertEquals(article, result)
        assertFalse(article.isStoredLocally)
        verify { articleLocalStorage.insertArticle("name", article) }
    }

    @Test
    fun `given non existing song by term should return empty song`() {
        every { articleLocalStorage.getArticleByArtistName("name") } returns null
        every { articleService.getArticle("name") } returns null

        val result = articleRepository.getArticleByArtistName("name")

        assertEquals("", result.biography)
    }
}