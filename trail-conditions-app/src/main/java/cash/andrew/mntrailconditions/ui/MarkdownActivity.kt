package cash.andrew.mntrailconditions.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.StringRes
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.databinding.ActivityMarkdownBinding
import cash.andrew.mntrailconditions.util.ComponentContainer
import cash.andrew.mntrailconditions.util.makeComponent
import io.noties.markwon.Markwon
import javax.inject.Inject

private const val TITLE_RES_KEY = "TitleRes"
private const val MARKDOWN_FILE_KEY = "MarkdownFile"

class MarkdownActivity : AppCompatActivity(), ComponentContainer<ActivityComponent> {

    @Inject lateinit var markwon: Markwon

    override val component by lazy { makeComponent() }

    private lateinit var binding: ActivityMarkdownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)

        val markDownFile = requireNotNull(intent.getStringExtra(MARKDOWN_FILE_KEY))
        val title = getString(intent.getIntExtra(TITLE_RES_KEY, 0))

        binding = ActivityMarkdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
        val arrow = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp, theme)
        arrow.setTint(typedValue.data)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(arrow)
        supportActionBar?.title = title

        resources.assets.open(markDownFile).use { inputStream ->
            inputStream.reader().use {
                markwon.setMarkdown(binding.markdownText, it.readText())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

enum class MarkDownFile(val fileName: String) {
    ABOUT("about.md"),
    WHY_ARE_TRAILS_CLOSED("why-are-trails-closed.md")
}

fun Activity.navigateToMarkDownActivity(@StringRes titleRes: Int, markDownFile: MarkDownFile) {
    val intent = Intent(this, MarkdownActivity::class.java)
    intent.putExtra(TITLE_RES_KEY, titleRes)
    intent.putExtra(MARKDOWN_FILE_KEY, markDownFile.fileName)
    startActivity(intent)
}
