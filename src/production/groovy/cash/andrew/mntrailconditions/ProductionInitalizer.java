package cash.andrew.mntrailconditions;

import android.content.Context;
import cash.andrew.mntrailconditions.util.CrashlyticsTree;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

class ProductionInitalizer implements MnTrailConditionsInitializer {
  @Override public void init(Context context) {
    Fabric.with(context, new Crashlytics());
    Timber.plant(new CrashlyticsTree());
  }
}
