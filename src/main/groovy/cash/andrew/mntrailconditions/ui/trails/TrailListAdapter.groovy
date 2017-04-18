package cash.andrew.mntrailconditions.ui.trails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.model.TrailInfo
import com.andrewreitz.velcro.BindableRecyclerAdapter
import groovy.transform.CompileStatic

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@CompileStatic
class TrailListAdapter extends BindableRecyclerAdapter<TrailViewModel> {

  private List<TrailViewModel> trails = []

  @Inject TrailListAdapter() { }

  void setTrails(List<TrailInfo> trails) {
    this.trails = trails.collect { new TrailViewModel(it) }
  }

  @Override View newView(LayoutInflater layoutInflater, int position, ViewGroup viewGroup) {
    return layoutInflater.inflate(R.layout.trail_list_item_view, viewGroup, false)
  }

  @Override TrailViewModel getItem(int position) {
    return trails[position]
  }

  @Override void bindView(TrailViewModel trail, View view, int position) {
    (view as TrailListItemView).bind(trail)
  }

  @Override int getItemCount() {
    return trails.size()
  }
}
