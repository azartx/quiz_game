package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.data.repositories.questions.QuestionRepository
import com.solo4.millionerquiz.data.repositories.questions.impl.MockedQuestionsRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factory { MockedQuestionsRepositoryImpl() } bind QuestionRepository::class
}
