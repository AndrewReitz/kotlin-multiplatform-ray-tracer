package cash.andrew.mntrailconditions.data.api;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import cash.andrew.mntrailconditions.data.ApiEndpoint;
import cash.andrew.mntrailconditions.data.NetworkDelay;
import cash.andrew.mntrailconditions.data.NetworkFailurePercent;
import cash.andrew.mntrailconditions.data.NetworkVariancePercent;

import com.f2prateek.rx.preferences2.Preference;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

@Module(complete = false, library = true, overrides = true)
public final class DebugApiModule {
  @Provides
  @Singleton
  HttpUrl provideHttpUrl(@ApiEndpoint Preference<String> apiEndpoint) {
    return HttpUrl.parse(apiEndpoint.get());
  }

  @Provides
  @Singleton
  HttpLoggingInterceptor provideLoggingInterceptor() {
    HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return loggingInterceptor;
  }

  @Provides
  @Singleton
  @Named("Api")
  OkHttpClient provideApiClient(OkHttpClient client, HttpLoggingInterceptor loggingInterceptor) {
    return ApiModule.createApiClient(client)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
  }

  @Provides
  @Singleton
  NetworkBehavior provideBehavior(
      @NetworkDelay Preference<Long> networkDelay,
      @NetworkFailurePercent Preference<Integer> networkFailurePercent,
      @NetworkVariancePercent Preference<Integer> networkVariancePercent) {
    NetworkBehavior behavior = NetworkBehavior.create();
    behavior.setDelay(networkDelay.get(), MILLISECONDS);
    behavior.setFailurePercent(networkFailurePercent.get());
    behavior.setVariancePercent(networkVariancePercent.get());
    return behavior;
  }

  @Provides
  @Singleton
  MockRetrofit provideMockRetrofit(Retrofit retrofit, NetworkBehavior behavior) {
    return new MockRetrofit.Builder(retrofit).networkBehavior(behavior).build();
  }
}
