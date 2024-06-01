package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class OtherInfoPresenterTest {
    private val articleRepository: CardRepository = mockk()
    private val otherInfoDescriptionHelper: OtherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

    private val otherInfoDescriptionPresenter = OtherInfoPresenterImpl(
        articleRepository, otherInfoDescriptionHelper
    )

    @Test
    fun `get artist by name should notify by observable`() {
        val article = Card.ArtistBiography(
            "name",
            "biography",
            "article",
            false,
        )
        every { articleRepository.getCard("name") } returns article

        otherInfoDescriptionPresenter.getCard("name")
        otherInfoDescriptionPresenter.otherInfoObservable.subscribe { state ->
            Assert.assertEquals("name", state.artistName)
            Assert.assertEquals("article", state.infoUrl)
        }
    }
}