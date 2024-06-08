package ayds.songinfo.moredetails.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

const val ARTIST_NAME_INTENT_EXTRA = "artistName"

class OtherInfoViewActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: OtherInfoPagerAdapter

    private lateinit var presenter: OtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initPresenter()
        initObservers()
        getCardAsync()
    }

    private fun initPresenter() {
        OtherInfoInjector.initGraph(this)
        presenter = OtherInfoInjector.presenter
    }

    private fun initViewProperties() {
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = OtherInfoPagerAdapter(supportFragmentManager, lifecycle)
    }

    private fun initObservers() {
        presenter.otherInfoObservable
            .subscribe { value -> updateUiCards(value) }
    }

    private fun getCardAsync() {
        Thread {
            presenter.getCard(getArtistName())
        }.start()
    }

    private fun getArtistName(): String =
        intent.getStringExtra(ARTIST_NAME_INTENT_EXTRA) ?: throw Exception("Missing artist name")


    private fun updateUiCards(otherInfoUiState: OtherInfoUiState) {
        runOnUiThread {
            updateCards(otherInfoUiState)
            updateAdapter()
        }
    }

    private fun updateCards(otherInfoUiState: OtherInfoUiState) {
        otherInfoUiState.cards
            .filter { !it.isEmpty }
            .forEach {
                loadTabCard(it)
            }
    }

    private fun updateAdapter() {
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pagerAdapter.getTitle(position)
        }.attach()
    }

    private fun loadTabCard(cardState: OtherInfoCardUiState) {
        val fragment = OtherInfoCardFragment(cardState)
        pagerAdapter.addFragment(fragment, cardState.source.toString())

    }
}
