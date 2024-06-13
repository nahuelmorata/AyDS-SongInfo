package ayds.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import ayds.songinfo.R
import com.squareup.picasso.Picasso

class OtherInfoCardFragment(
    private val otherInfoCardUiState: OtherInfoCardUiState
) : Fragment() {
    private lateinit var descriptionTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.other_info_source, container, false)
        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewProperties(view)
        updateUiCard()
    }

    private fun initViewProperties(view: View) {
        descriptionTextView = view.findViewById(R.id.textPane1)
        openUrlButton = view.findViewById(R.id.openUrlButton1)
        lastFMImageView = view.findViewById(R.id.imageView1)
    }

    private fun updateUiCard() {
        updateUiCardLogo(otherInfoCardUiState.logoUrl)
        updateUiCardDescription(otherInfoCardUiState.descriptionHtml)
        updateUiCardInfoUrlButton(otherInfoCardUiState.infoUrl)
    }

    private fun updateUiCardLogo(logoUrl: String) {
        Picasso.get().load(logoUrl).into(lastFMImageView)
    }

    private fun updateUiCardDescription(description: String) {
        descriptionTextView.text = Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun updateUiCardInfoUrlButton(infoUrl: String) {
        openUrlButton.setOnClickListener {
            navigateToUrl(infoUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }
}