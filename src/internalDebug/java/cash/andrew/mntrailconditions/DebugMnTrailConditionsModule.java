package cash.andrew.mntrailconditions;

import cash.andrew.mntrailconditions.data.DebugDataModule;
import cash.andrew.mntrailconditions.ui.DebugUiModule;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.timber.StethoTree;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import timber.log.Timber;

@Module(
    addsTo = MnTrailConditionsModule.class,
    includes = {
        DebugUiModule.class,
        DebugDataModule.class
    },
    overrides = true
)
public final class DebugMnTrailConditionsModule {
  // Low-tech flag to force certain debug build behaviors when running in an instrumentation test.
  // This value is used in the creation of singletons so it must be set before the graph is created.
  static boolean instrumentationTest = false;

  @Provides @Singleton @IsInstrumentationTest boolean provideIsInstrumentationTest() {
    return instrumentationTest;
  }

  @Provides @Singleton MnTrailConditionsInitializer provideMnTrailConditionsInitializer() {
    return context -> {
      Stetho.initializeWithDefaults(context);

      Timber.plant(new Timber.DebugTree());
      Timber.plant(new StethoTree());
    };
  }
}
