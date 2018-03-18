package cash.andrew.mntrailconditions.data

import cash.andrew.mntrailconditions.data.api.InternalReleaseApiModule
import dagger.Module

@Module(includes = [InternalReleaseApiModule::class])
object InternalReleaseDataModule
