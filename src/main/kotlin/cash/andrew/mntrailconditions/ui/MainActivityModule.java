package cash.andrew.mntrailconditions.ui;

import dagger.Module;

@Module
public final class MainActivityModule {
  private final MainActivity mainActivity;

  MainActivityModule(MainActivity mainActivity) {
    this.mainActivity = mainActivity;
  }
}
