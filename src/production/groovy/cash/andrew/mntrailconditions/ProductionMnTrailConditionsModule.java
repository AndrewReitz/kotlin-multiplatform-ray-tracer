package cash.andrew.mntrailconditions;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(addsTo = MnTrailConditionsModule.class, overrides = true)
public final class ProductionMnTrailConditionsModule {
  @Provides @Singleton MnTrailConditionsInitializer provideMnTrailConditionsInitializer() {
    return new ProductionInitalizer();
  }
}
