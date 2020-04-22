package cash.andrew.mntrailconditions.ui

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UiModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideViewContainer(): ViewContainer {
        return ViewContainer.DEFAULT
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideActivityHierarchyServer(): ActivityHierarchyServer {
        return ActivityHierarchyServer.NONE
    }
}
