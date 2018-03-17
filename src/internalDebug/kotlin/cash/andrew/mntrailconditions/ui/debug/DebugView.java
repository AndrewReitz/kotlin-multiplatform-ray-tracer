package cash.andrew.mntrailconditions.ui.debug;

import static butterknife.ButterKnife.findById;

import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cash.andrew.mntrailconditions.BuildConfig;
import cash.andrew.mntrailconditions.MnTrailConditionsApp;
import cash.andrew.mntrailconditions.R;
import cash.andrew.mntrailconditions.data.AnimationSpeed;
import cash.andrew.mntrailconditions.data.ApiEndpoint;
import cash.andrew.mntrailconditions.data.ApiEndpoints;
import cash.andrew.mntrailconditions.data.CaptureIntents;
import cash.andrew.mntrailconditions.data.LumberYard;
import cash.andrew.mntrailconditions.data.NetworkDelay;
import cash.andrew.mntrailconditions.data.NetworkFailurePercent;
import cash.andrew.mntrailconditions.data.NetworkVariancePercent;
import cash.andrew.mntrailconditions.ui.logs.LogsDialog;
import cash.andrew.mntrailconditions.ui.misc.EnumAdapter;
import cash.andrew.mntrailconditions.util.IntentManager;
import cash.andrew.mntrailconditions.util.Strings;
import com.f2prateek.rx.preferences2.Preference;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.squareup.leakcanary.internal.DisplayLeakActivity;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAccessor;
import timber.log.Timber;

public final class DebugView extends FrameLayout {
  private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.US).withZone(ZoneId.systemDefault());

  @BindView(R.id.debug_network_endpoint)
  Spinner endpointView;

  @BindView(R.id.debug_network_endpoint_edit)
  View endpointEditView;

  @BindView(R.id.debug_ui_animation_speed)
  Spinner uiAnimationSpeedView;

  @BindView(R.id.debug_build_name)
  TextView buildNameView;

  @BindView(R.id.debug_build_code)
  TextView buildCodeView;

  @BindView(R.id.debug_build_sha)
  TextView buildShaView;

  @BindView(R.id.debug_build_date)
  TextView buildDateView;

  @BindView(R.id.debug_device_make)
  TextView deviceMakeView;

  @BindView(R.id.debug_device_model)
  TextView deviceModelView;

  @BindView(R.id.debug_device_resolution)
  TextView deviceResolutionView;

  @BindView(R.id.debug_device_density)
  TextView deviceDensityView;

  @BindView(R.id.debug_device_release)
  TextView deviceReleaseView;

  @BindView(R.id.debug_device_api)
  TextView deviceApiView;

  @BindView(R.id.debug_picasso_indicators)
  Switch picassoIndicatorView;

  @BindView(R.id.debug_picasso_cache_size)
  TextView picassoCacheSizeView;

  @BindView(R.id.debug_picasso_cache_hit)
  TextView picassoCacheHitView;

  @BindView(R.id.debug_picasso_cache_miss)
  TextView picassoCacheMissView;

  @BindView(R.id.debug_picasso_decoded)
  TextView picassoDecodedView;

  @BindView(R.id.debug_picasso_decoded_total)
  TextView picassoDecodedTotalView;

  @BindView(R.id.debug_picasso_decoded_avg)
  TextView picassoDecodedAvgView;

  @BindView(R.id.debug_picasso_transformed)
  TextView picassoTransformedView;

  @BindView(R.id.debug_picasso_transformed_total)
  TextView picassoTransformedTotalView;

  @BindView(R.id.debug_picasso_transformed_avg)
  TextView picassoTransformedAvgView;

  @BindView(R.id.debug_okhttp_cache_max_size)
  TextView okHttpCacheMaxSizeView;

  @BindView(R.id.debug_okhttp_cache_write_error)
  TextView okHttpCacheWriteErrorView;

  @BindView(R.id.debug_okhttp_cache_request_count)
  TextView okHttpCacheRequestCountView;

  @BindView(R.id.debug_okhttp_cache_network_count)
  TextView okHttpCacheNetworkCountView;

  @BindView(R.id.debug_okhttp_cache_hit_count)
  TextView okHttpCacheHitCountView;

  @Inject OkHttpClient client;

  @Inject
  @Named("Api")
  OkHttpClient apiClient;

  @Inject LumberYard lumberYard;
  @Inject @ApiEndpoint Preference<String> networkEndpoint;
  @Inject @CaptureIntents Preference<Boolean> captureIntents;
  @Inject @AnimationSpeed Preference<Integer> animationSpeed;
  @Inject @NetworkDelay Preference<Long> networkDelay;
  @Inject @NetworkFailurePercent Preference<Integer> networkFailurePercent;
  @Inject @NetworkVariancePercent Preference<Integer> networkVariancePercent;
  @Inject Application app;
  @Inject IntentManager intentManager;

  public DebugView(Context context) {
    this(context, null);
  }

  public DebugView(Context context, AttributeSet attrs) {
    super(context, attrs);

    ((MnTrailConditionsApp)context.getApplicationContext()).getComponent().inject(this);

    // Inflate all of the controls and inject them.
    LayoutInflater.from(context).inflate(R.layout.debug_view_content, this);
    ButterKnife.bind(this);

    setupNetworkSection();
    setupBuildSection();
    setupDeviceSection();
    setupOkHttpCacheSection();
  }

  public void onDrawerOpened() {
    refreshOkHttpCacheStats();
  }

  private void setupNetworkSection() {
    final ApiEndpoints currentEndpoint = ApiEndpoints.Companion.from(networkEndpoint.get());
    final EnumAdapter<ApiEndpoints> endpointAdapter =
        new EnumAdapter<>(getContext(), ApiEndpoints.class);
    endpointView.setAdapter(endpointAdapter);
    endpointView.setSelection(currentEndpoint.ordinal());

    RxAdapterView.itemSelections(endpointView)
        .map(endpointAdapter::getItem)
        .filter(item -> item != currentEndpoint)
        .subscribe(
            selected -> {
              if (selected == ApiEndpoints.CUSTOM) {
                Timber.d("Custom network endpoint selected. Prompting for URL.");
                showCustomEndpointDialog(currentEndpoint.ordinal(), "http://");
              } else {
                setEndpointAndRelaunch(selected.getUrl());
              }
            });

    // Only show the endpoint editor when a custom endpoint is in use.
    endpointEditView.setVisibility(currentEndpoint == ApiEndpoints.CUSTOM ? VISIBLE : GONE);
  }

  @OnClick(R.id.debug_network_endpoint_edit)
  void onEditEndpointClicked() {
    Timber.d("Prompting to edit custom endpoint URL.");
    // Pass in the currently selected position since we are merely editing.
    showCustomEndpointDialog(endpointView.getSelectedItemPosition(), networkEndpoint.get());
  }

  @OnClick(R.id.debug_logs_show)
  void showLogs() {
    new LogsDialog(
            new ContextThemeWrapper(getContext(), R.style.Theme_MnTrailConditions),
            lumberYard,
            intentManager)
        .show();
  }

  @OnClick(R.id.debug_leaks_show)
  void showLeaks() {
    Intent intent = new Intent(getContext(), DisplayLeakActivity.class);
    getContext().startActivity(intent);
  }

  private void setupBuildSection() {
    buildNameView.setText(BuildConfig.VERSION_NAME);
    buildCodeView.setText(String.valueOf(BuildConfig.VERSION_CODE));
    buildShaView.setText(BuildConfig.GIT_SHA);

    TemporalAccessor buildTime = Instant.ofEpochSecond(BuildConfig.GIT_TIMESTAMP);
    buildDateView.setText(DATE_DISPLAY_FORMAT.format(buildTime));
  }

  private void setupDeviceSection() {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    String densityBucket = getDensityString(displayMetrics);
    deviceMakeView.setText(Strings.truncateAt(Build.MANUFACTURER, 20));
    deviceModelView.setText(Strings.truncateAt(Build.MODEL, 20));
    deviceResolutionView.setText(displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
    deviceDensityView.setText(displayMetrics.densityDpi + "dpi (" + densityBucket + ")");
    deviceReleaseView.setText(Build.VERSION.RELEASE);
    deviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
  }

  private void setupOkHttpCacheSection() {
    Cache cache = client.cache(); // Shares the cache with apiClient, so no need to check both.
    okHttpCacheMaxSizeView.setText(getSizeString(cache.maxSize()));

    refreshOkHttpCacheStats();
  }

  private void refreshOkHttpCacheStats() {
    Cache cache = client.cache(); // Shares the cache with apiClient, so no need to check both.
    int writeTotal = cache.writeSuccessCount() + cache.writeAbortCount();
    int percentage = (int) ((1f * cache.writeAbortCount() / writeTotal) * 100);
    okHttpCacheWriteErrorView.setText(
        cache.writeAbortCount() + " / " + writeTotal + " (" + percentage + "%)");
    okHttpCacheRequestCountView.setText(String.valueOf(cache.requestCount()));
    okHttpCacheNetworkCountView.setText(String.valueOf(cache.networkCount()));
    okHttpCacheHitCountView.setText(String.valueOf(cache.hitCount()));
  }

  private void applyAnimationSpeed(int multiplier) {
    try {
      Method method = ValueAnimator.class.getDeclaredMethod("setDurationScale", float.class);
      method.invoke(null, (float) multiplier);
    } catch (Exception e) {
      throw new RuntimeException("Unable to apply animation speed.", e);
    }
  }

  private static String getDensityString(DisplayMetrics displayMetrics) {
    switch (displayMetrics.densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return "ldpi";
      case DisplayMetrics.DENSITY_MEDIUM:
        return "mdpi";
      case DisplayMetrics.DENSITY_HIGH:
        return "hdpi";
      case DisplayMetrics.DENSITY_XHIGH:
        return "xhdpi";
      case DisplayMetrics.DENSITY_XXHIGH:
        return "xxhdpi";
      case DisplayMetrics.DENSITY_XXXHIGH:
        return "xxxhdpi";
      case DisplayMetrics.DENSITY_TV:
        return "tvdpi";
      default:
        return String.valueOf(displayMetrics.densityDpi);
    }
  }

  private static String getSizeString(long bytes) {
    String[] units = new String[] {"B", "KB", "MB", "GB"};
    int unit = 0;
    while (bytes >= 1024) {
      bytes /= 1024;
      unit += 1;
    }
    return bytes + units[unit];
  }

  private void showCustomEndpointDialog(final int originalSelection, String defaultUrl) {
    View view = LayoutInflater.from(app).inflate(R.layout.debug_drawer_network_endpoint, null);
    final EditText url = findById(view, R.id.debug_drawer_network_endpoint_url);
    url.setText(defaultUrl);
    url.setSelection(url.length());

    new AlertDialog.Builder(getContext()) //
        .setTitle("Set Network Endpoint")
        .setView(view)
        .setNegativeButton(
            "Cancel",
            (dialog, i) -> {
              endpointView.setSelection(originalSelection);
              dialog.cancel();
            })
        .setPositiveButton(
            "Use",
            (dialog, i) -> {
              String theUrl = url.getText().toString();
              if (!Strings.isBlank(theUrl)) {
                setEndpointAndRelaunch(theUrl);
              } else {
                endpointView.setSelection(originalSelection);
              }
            })
        .setOnCancelListener(
            (dialogInterface) -> {
              endpointView.setSelection(originalSelection);
            })
        .show();
  }

  private void setEndpointAndRelaunch(String endpoint) {
    Timber.d("Setting network endpoint to %s", endpoint);
    networkEndpoint.set(endpoint);

    Completable.timer(1L, TimeUnit.SECONDS)
            .subscribe(() -> ProcessPhoenix.triggerRebirth(getContext()));
  }
}
