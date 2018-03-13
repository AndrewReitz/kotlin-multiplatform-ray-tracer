package cash.andrew.mntrailconditions.ui;

import cash.andrew.mntrailconditions.ui.debug.DebugViewContainer;
import cash.andrew.mntrailconditions.ui.debug.SocketActivityHierarchyServer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class DebugUiModule {
  @Provides
  @Singleton
  ViewContainer provideViewContainer(DebugViewContainer debugViewContainer) {
    return debugViewContainer;
  }

  @Provides
  @Singleton
  ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
}
