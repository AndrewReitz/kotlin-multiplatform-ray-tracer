package cash.andrew.mntrailconditions.ui.example;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cash.andrew.mntrailconditions.R;
import cash.andrew.mntrailconditions.data.Injector;
import javax.inject.Inject;

public class ExampleView extends LinearLayout {

  @BindView(R.id.example_toolbar) Toolbar toolbarView;

  @Inject DrawerLayout drawerLayout;

  public ExampleView(Context context, AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) {
      Injector.obtain(context).inject(this);
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    toolbarView.setNavigationIcon(R.drawable.menu_icon);
    toolbarView.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
  }
}
