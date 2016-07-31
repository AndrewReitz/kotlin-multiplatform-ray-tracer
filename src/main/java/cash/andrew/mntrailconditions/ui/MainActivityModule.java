package cash.andrew.mntrailconditions.ui;

import android.support.v4.widget.DrawerLayout;

import cash.andrew.mntrailconditions.MnTrailConditionsModule;

import cash.andrew.mntrailconditions.ui.example.ExampleView;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    addsTo = MnTrailConditionsModule.class,
    injects = ExampleView.class
)
public final class MainActivityModule {
  private final MainActivity mainActivity;

  MainActivityModule(MainActivity mainActivity) {
    this.mainActivity = mainActivity;
  }

  @Provides @Singleton DrawerLayout provideDrawerLayout() {
    return mainActivity.drawerLayout;
  }
}
