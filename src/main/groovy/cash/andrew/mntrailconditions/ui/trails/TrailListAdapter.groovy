package cash.andrew.mntrailconditions.ui.trails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cash.andrew.mntrailconditions.data.model.TrailInfo
import com.andrewreitz.velcro.BindableRecyclerAdapter
import groovy.transform.CompileStatic

@CompileStatic
class TrailListAdapter extends BindableRecyclerAdapter<TrailInfo> {

  private List<TrailInfo> trails = []

  void setTrails(List<TrailInfo> trails) {
    this.trails = trails
  }

  @Override View newView(LayoutInflater layoutInflater, int position, ViewGroup viewGroup) {
    return null
  }

  @Override TrailInfo getItem(int position) {
    return trails[position]
  }

  @Override void bindView(TrailInfo trailInfo, View view, int position) {

  }

  @Override int getItemCount() {
    return trails.size()
  }
}
