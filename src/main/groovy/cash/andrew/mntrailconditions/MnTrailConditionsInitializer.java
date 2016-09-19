package cash.andrew.mntrailconditions;

import android.content.Context;

/**
 * Interface that should be implemented by different build types to enable different
 * behaviors, such as logging.
 */
interface MnTrailConditionsInitializer {
  void init(Context context);
}
