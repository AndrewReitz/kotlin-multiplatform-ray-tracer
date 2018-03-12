package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import kotlinx.android.synthetic.main.trail_list_item_view.view.*

class TrailListItemView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val title by lazy { trail_list_item_title }
    private val conditionsImage by lazy { trail_list_item_conditions_image }
    private val conditionsText by lazy { trail_list_item_condition_text }
    private val detailsText by lazy { trail_list_item_details }
    private val lastUpdatedText by lazy { trail_list_last_updated_time }

    fun bind(trail: TrailViewModel) {
        with(trail) {
            title.text = name
            conditionsText.text = status
            detailsText.text = description
            lastUpdatedText.text = lastUpdated
            conditionsImage.setImageDrawable(context.getDrawable(statusDrawableId))
        }
    }
}
