package cash.andrew.mntrailconditions.ui;

import cash.andrew.mntrailconditions.IsInstrumentationTest;
import cash.andrew.mntrailconditions.ui.debug.DebugView;
import cash.andrew.mntrailconditions.ui.debug.DebugViewContainer;
import cash.andrew.mntrailconditions.ui.debug.SocketActivityHierarchyServer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
  injects = {
    DebugViewContainer.class,
    DebugView.class,
  },
  complete = false,
  library = true,
  overrides = true
)
public class DebugUiModule {
  @Provides
  @Singleton
  ViewContainer provideViewContainer(
      DebugViewContainer debugViewContainer, @IsInstrumentationTest boolean isInstrumentationTest) {
    // Do not add the debug controls for when we are running inside of an instrumentation test.
    return isInstrumentationTest ? ViewContainer.DEFAULT : debugViewContainer;
  }

  @Provides
  @Singleton
  ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
}
