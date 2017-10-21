package cash.andrew.mntrailconditions;

import cash.andrew.mntrailconditions.ui.InternalReleaseUiModule;
import dagger.Module;

@Module(
  addsTo = MnTrailConditionsModule.class,
  includes = InternalReleaseUiModule.class,
  overrides = true
)
public final class InternalReleaseMnTrailConditionsModule {}
