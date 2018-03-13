package cash.andrew.mntrailconditions.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import cash.andrew.mntrailconditions.R;
import com.kobakei.ratethisapp.RateThisApp;
import javax.inject.Inject;

public final class MainActivity extends Activity {
  @BindView(R.id.main_content)
  ViewGroup content;

  @BindColor(R.color.status_bar)
  int statusBarColor;

  @Inject ViewContainer viewContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = getLayoutInflater();

    // todo inject stuff

    ViewGroup container = viewContainer.forActivity(this);

    inflater.inflate(R.layout.main_activity, container);
    ButterKnife.bind(this, container);

    inflater.inflate(R.layout.trail_list_view, content);

    RateThisApp.onCreate(this);
    RateThisApp.showRateDialogIfNeeded(this);
  }
}
