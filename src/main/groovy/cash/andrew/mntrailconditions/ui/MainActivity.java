package cash.andrew.mntrailconditions.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kobakei.ratethisapp.RateThisApp;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import cash.andrew.mntrailconditions.R;
import cash.andrew.mntrailconditions.data.Injector;
import dagger.ObjectGraph;

public final class MainActivity extends Activity {
  @BindView(R.id.main_content) ViewGroup content;

  @BindColor(R.color.status_bar) int statusBarColor;

  @Inject ViewContainer viewContainer;

  private ObjectGraph activityGraph;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = getLayoutInflater();

    // Explicitly reference the application object since we don't want to match our own injector.
    ObjectGraph appGraph = Injector.obtain(getApplication());
    appGraph.inject(this);
    activityGraph = appGraph.plus(new MainActivityModule(this));

    ViewGroup container = viewContainer.forActivity(this);

    inflater.inflate(R.layout.main_activity, container);
    ButterKnife.bind(this, container);

    inflater.inflate(R.layout.trail_list_view, content);

    RateThisApp.onCreate(this);
    RateThisApp.showRateDialogIfNeeded(this);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return activityGraph;
    }
    return super.getSystemService(name);
  }

  @Override protected void onDestroy() {
    activityGraph = null;
    super.onDestroy();
  }
}
