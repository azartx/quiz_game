package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.data.repositories.questions.impl.LevelsRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    //factory { MockedQuestionsRepositoryImpl() } bind LevelsRepository::class
    factory { LevelsRepositoryImpl(get()) } bind LevelsRepository::class
}
