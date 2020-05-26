package cash.andrew.mntrailconditions.data

import cash.andrew.mntrailconditions.data.api.ApiModule
import cash.andrew.mntrailconditions.data.api.ProdApiModule
import dagger.Module

@Module(includes = [ApiModule::class, ProdApiModule::class])
object ProdDataModule
