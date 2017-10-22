package cash.andrew.mntrailconditions;

import android.app.Application;
import cash.andrew.mntrailconditions.data.DataModule;
import cash.andrew.mntrailconditions.ui.UiModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
  includes = {UiModule.class, DataModule.class},
  injects = {MnTrailConditionsApp.class}
)
public final class MnTrailConditionsModule {
  private final MnTrailConditionsApp app;

  public MnTrailConditionsModule(MnTrailConditionsApp app) {
    this.app = app;
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return app;
  }

  @Provides
  @Singleton
  MnTrailConditionsInitializer provideMnTrailConditionsInitializer() {
    return context -> {
      /* Production initialization such as Crashlytcs */
    };
  }
}
