package com.solo4.millionerquiz.data.repositories.questions.impl

import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.model.game.Answer
import com.solo4.millionerquiz.model.game.Level
import com.solo4.millionerquiz.model.game.Question

class MockedQuestionsRepositoryImpl : LevelsRepository {
    override suspend fun getAllLevels(): List<Level> {
        return listOf(
            Level(
                questions = listOf(
                    Question(
                        1,
                        "Hello world 1?",
                        "",
                        listOf(
                            Answer(1, "Answer 1", true),
                            Answer(2, "Answer 2"),
                            Answer(3, "Answer 3"),
                            Answer(4, "Answer 4")
                        )
                    ),
                    Question(
                        2,
                        "Hello world 2?",
                        "",
                        listOf(
                            Answer(1, "Answer 1"),
                            Answer(2, "Answer 2", true),
                            Answer(3, "Answer 3"),
                            Answer(4, "Answer 4")
                        )
                    ),
                    Question(
                        3,
                        "Hello world 3?",
                        "",
                        listOf(
                            Answer(1, "Answer 1"),
                            Answer(2, "Answer 2"),
                            Answer(3, "Answer 3", true),
                            Answer(4, "Answer 4")
                        )
                    ),
                    Question(
                        4,
                        "Hello world 4?",
                        "",
                        listOf(
                            Answer(1, "Answer 1"),
                            Answer(2, "Answer 2"),
                            Answer(3, "Answer 3"),
                            Answer(4, "Answer 4", true)
                        )
                    )
                )
            )
        )
    }

    override suspend fun getSpecificLevel(levelNumber: Int): Level {
        return Level(
            questions = listOf(
                Question(
                    1,
                    "Hello world 1?",
                    "",
                    listOf(
                        Answer(1, "Answer 1", true),
                        Answer(2, "Answer 2"),
                        Answer(3, "Answer 3"),
                        Answer(4, "Answer 4")
                    )
                ),
                Question(
                    2,
                    "Hello world 2?",
                    "",
                    listOf(
                        Answer(1, "Answer 1"),
                        Answer(2, "Answer 2", true),
                        Answer(3, "Answer 3"),
                        Answer(4, "Answer 4")
                    )
                ),
                Question(
                    3,
                    "Hello world 3?",
                    "",
                    listOf(
                        Answer(1, "Answer 1"),
                        Answer(2, "Answer 2"),
                        Answer(3, "Answer 3", true),
                        Answer(4, "Answer 4")
                    )
                ),
                Question(
                    4,
                    "Hello world 4?",
                    "",
                    listOf(
                        Answer(1, "Answer 1"),
                        Answer(2, "Answer 2"),
                        Answer(3, "Answer 3"),
                        Answer(4, "Answer 4", true)
                    )
                )
            )
        )
    }

    override suspend fun getLevelsCount(): Int {
        return 1
    }
}