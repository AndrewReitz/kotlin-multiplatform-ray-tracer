package cash.andrew.mntrailconditions.ui;

import cash.andrew.mntrailconditions.MnTrailConditionsModule;
import cash.andrew.mntrailconditions.ui.trails.TrailListView;

import dagger.Module;

@Module(
    addsTo = MnTrailConditionsModule.class,
    injects = TrailListView.class
)
public final class MainActivityModule {
  private final MainActivity mainActivity;

  MainActivityModule(MainActivity mainActivity) {
    this.mainActivity = mainActivity;
  }
}
