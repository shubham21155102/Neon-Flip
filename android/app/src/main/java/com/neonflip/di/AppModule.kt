package com.neonflip.di

import android.content.Context
import com.neonflip.data.local.TokenStorage
import com.neonflip.data.remote.AuthInterceptor
import com.neonflip.data.repository.AuthRepositoryImpl
import com.neonflip.data.repository.ScoreRepositoryImpl
import com.neonflip.domain.repository.AuthRepository
import com.neonflip.domain.repository.ScoreRepository
import com.neonflip.domain.usecase.GetCurrentUserUseCase
import com.neonflip.domain.usecase.GetLeaderboardUseCase
import com.neonflip.domain.usecase.LoginUseCase
import com.neonflip.domain.usecase.RegisterUseCase
import com.neonflip.domain.usecase.SubmitScoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenStorage(
        @ApplicationContext context: Context
    ): TokenStorage {
        return TokenStorage(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository {
        return authRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideScoreRepository(
        scoreRepositoryImpl: ScoreRepositoryImpl
    ): ScoreRepository {
        return scoreRepositoryImpl
    }

    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository
    ): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun provideRegisterUseCase(
        authRepository: AuthRepository
    ): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    fun provideGetCurrentUserUseCase(
        authRepository: AuthRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    fun provideSubmitScoreUseCase(
        scoreRepository: ScoreRepository
    ): SubmitScoreUseCase {
        return SubmitScoreUseCase(scoreRepository)
    }

    @Provides
    fun provideGetLeaderboardUseCase(
        scoreRepository: ScoreRepository
    ): GetLeaderboardUseCase {
        return GetLeaderboardUseCase(scoreRepository)
    }
}
