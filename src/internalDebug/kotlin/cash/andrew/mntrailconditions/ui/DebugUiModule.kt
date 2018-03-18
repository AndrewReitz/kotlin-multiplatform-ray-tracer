package cash.andrew.mntrailconditions.ui

import cash.andrew.mntrailconditions.ui.debug.DebugViewContainer
import cash.andrew.mntrailconditions.ui.debug.SocketActivityHierarchyServer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DebugUiModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideViewContainer(debugViewContainer: DebugViewContainer): ViewContainer = debugViewContainer

    @JvmStatic
    @Provides
    @Singleton
    fun provideActivityHierarchyServer(): ActivityHierarchyServer = SocketActivityHierarchyServer()
}
