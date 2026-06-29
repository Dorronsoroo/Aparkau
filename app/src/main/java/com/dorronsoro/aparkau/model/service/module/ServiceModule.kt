package com.dorronsoro.aparkau.model.service.module

import com.dorronsoro.aparkau.model.service.AccountService
import com.dorronsoro.aparkau.model.service.LogService
import com.dorronsoro.aparkau.model.service.UsuarioService
import com.dorronsoro.aparkau.model.service.impl.AccountServiceImp
import com.dorronsoro.aparkau.model.service.impl.LogServiceImpl
import com.dorronsoro.aparkau.model.service.impl.UsuarioServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun provideAccountService(impl: AccountServiceImp): AccountService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds
    abstract fun provideUsuarioService(impl: UsuarioServiceImpl): UsuarioService
}

