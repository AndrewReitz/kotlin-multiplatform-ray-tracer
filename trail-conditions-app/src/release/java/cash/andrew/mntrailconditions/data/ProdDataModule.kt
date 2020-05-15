package cash.andrew.mntrailconditions.data

import cash.andrew.mntrailconditions.data.api.ProdApiModule
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ProdApiModule::class])
object ProdDataModule
