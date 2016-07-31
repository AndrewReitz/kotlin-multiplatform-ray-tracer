package cash.andrew.mntrailconditions.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.picasso.OkHttp3Downloader;
import cash.andrew.mntrailconditions.data.api.DebugApiModule;
import cash.andrew.mntrailconditions.data.prefs.InetSocketAddressPreferenceAdapter;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import timber.log.Timber;

@Module(
    includes = DebugApiModule.class,
    complete = false,
    library = true,
    overrides = true
)
public final class DebugDataModule {
  private static final int DEFAULT_ANIMATION_SPEED = 1; // 1x (normal) speed.
  private static final boolean DEFAULT_PICASSO_DEBUGGING = false; // Debug indicators displayed
  private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false; // No pixel grid overlay.
  private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false; // No pixel ratio overlay.
  private static final boolean DEFAULT_SCALPEL_ENABLED = false; // No crazy 3D view tree.
  private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by default.
  private static final boolean DEFAULT_SEEN_DEBUG_DRAWER = false; // Show debug drawer first time.
  private static final boolean DEFAULT_CAPTURE_INTENTS = true; // Capture external intents.

  @Provides @Singleton RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
    return RxSharedPreferences.create(prefs);
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app,
      Preference<InetSocketAddress> networkProxyAddress) {
    return DataModule.createOkHttpClient(app)
        .sslSocketFactory(createBadSslSocketFactory())
        .proxy(InetSocketAddressPreferenceAdapter.createProxy(networkProxyAddress.get()))
        .build();
  }

  @Provides @Singleton @ApiEndpoint
  Preference<String> provideEndpointPreference(RxSharedPreferences preferences) {
    return preferences.getString("debug_endpoint", ApiEndpoints.PRODUCTION.url);
  }

  @Provides @Singleton @NetworkDelay
  Preference<Long> provideNetworkDelay(RxSharedPreferences preferences) {
    return preferences.getLong("debug_network_delay", 2000L);
  }

  @Provides @Singleton @NetworkFailurePercent
  Preference<Integer> provideNetworkFailurePercent(RxSharedPreferences preferences) {
    return preferences.getInteger("debug_network_failure_percent", 3);
  }

  @Provides @Singleton @NetworkVariancePercent
  Preference<Integer> provideNetworkVariancePercent(RxSharedPreferences preferences) {
    return preferences.getInteger("debug_network_variance_percent", 40);
  }

  @Provides @Singleton
  Preference<InetSocketAddress> provideNetworkProxyAddress(RxSharedPreferences preferences) {
    return preferences.getObject("debug_network_proxy",
        InetSocketAddressPreferenceAdapter.INSTANCE);
  }

  @Provides @Singleton @CaptureIntents
  Preference<Boolean> provideCaptureIntentsPreference(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
  }

  @Provides @Singleton @AnimationSpeed
  Preference<Integer> provideAnimationSpeed(RxSharedPreferences preferences) {
    return preferences.getInteger("debug_animation_speed", DEFAULT_ANIMATION_SPEED);
  }

  @Provides @Singleton @PicassoDebugging
  Preference<Boolean> providePicassoDebugging(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
  }

  @Provides @Singleton @PixelGridEnabled
  Preference<Boolean> providePixelGridEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_pixel_grid_enabled", DEFAULT_PIXEL_GRID_ENABLED);
  }

  @Provides @Singleton @PixelRatioEnabled
  Preference<Boolean> providePixelRatioEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_pixel_ratio_enabled", DEFAULT_PIXEL_RATIO_ENABLED);
  }

  @Provides @Singleton @SeenDebugDrawer
  Preference<Boolean> provideSeenDebugDrawer(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER);
  }

  @Provides @Singleton @ScalpelEnabled
  Preference<Boolean> provideScalpelEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
  }

  @Provides @Singleton @ScalpelWireframeEnabled
  Preference<Boolean> provideScalpelWireframeEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_scalpel_wireframe_drawer",
        DEFAULT_SCALPEL_WIREFRAME_ENABLED);
  }

  @Provides @Singleton Picasso providePicasso(OkHttpClient client, Application app) {
    Picasso.Builder builder = new Picasso.Builder(app).downloader(new OkHttp3Downloader(client));
    builder.listener((picasso, uri, exception) -> {
      Timber.e(exception, "Error while loading image %s", uri);
    });
    return builder.build();
  }

  private static SSLSocketFactory createBadSslSocketFactory() {
    try {
      // Construct SSLSocketFactory that accepts any cert.
      SSLContext context = SSLContext.getInstance("TLS");
      TrustManager permissive = new X509TrustManager() {
        @Override public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[0];
        }
      };
      context.init(null, new TrustManager[] { permissive }, null);
      return context.getSocketFactory();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
