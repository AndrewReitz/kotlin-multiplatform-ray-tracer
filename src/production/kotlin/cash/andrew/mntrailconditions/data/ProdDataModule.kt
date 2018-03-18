package cash.andrew.mntrailconditions.data

import cash.andrew.mntrailconditions.data.api.ProdApiModule
import dagger.Module

@Module(includes = [ProdApiModule::class])
object ProdDataModule
