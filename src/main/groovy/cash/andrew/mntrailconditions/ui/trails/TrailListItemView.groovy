package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.model.TrailInfo
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import groovy.transform.CompileStatic

@CompileStatic
class TrailListItemView extends CardView {

  @InjectView(R.id.trail_list_item_title) TextView title
  @InjectView(R.id.trail_list_item_conditions_image) ImageView conditionsImage
  @InjectView(R.id.trail_list_item_condition_text) TextView conditionsText
  @InjectView(R.id.trail_list_item_details) TextView detailsText
  @InjectView(R.id.trail_list_last_updated_text) TextView lastUpdatedText

  TrailListItemView(Context context, AttributeSet attrs) {
    super(context, attrs)
  }

  void bind(TrailInfo trailInfo) {
    trailInfo.with {
      title.text = name
      conditionsText.text = status
      detailsText.text = description
      lastUpdatedText.text = lastUpdated.toString()
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate()
    SwissKnife.inject(this)
  }
}
