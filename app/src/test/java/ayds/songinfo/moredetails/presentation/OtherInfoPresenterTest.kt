package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class OtherInfoPresenterTest {
    private val articleRepository: ArticleRepository = mockk()
    private val otherInfoDescriptionHelper: OtherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

    private val otherInfoDescriptionPresenter = OtherInfoPresenterImpl(
        articleRepository, otherInfoDescriptionHelper
    )

    @Test
    fun `get artist by name should notify by observable`() {
        val article = Article.ArtistBiography(
            "name",
            "biography",
            "article",
            false,
        )
        every { articleRepository.getArticleByArtistName("name") } returns article

        otherInfoDescriptionPresenter.getArtistBiography("name")
        otherInfoDescriptionPresenter.otherInfoObservable.subscribe { state ->
            Assert.assertEquals("name", state.artistName)
            Assert.assertEquals("article", state.artistUrl)
        }
    }
}