package com.elitesoldier.game.game.level

import com.elitesoldier.game.game.enemy.EnemySpawnData
import com.elitesoldier.game.game.enemy.EnemyType

/**
 * Manages level loading, progression, and difficulty scaling
 */
class LevelManager {

    companion object {
        const val TOTAL_LEVELS = 10
    }

    private val levels = mutableMapOf<Int, Level>()

    init {
        initializeLevels()
    }

    private fun initializeLevels() {
        for (i in 1..TOTAL_LEVELS) {
            levels[i] = createLevel(i)
        }
    }

    private fun createLevel(levelNumber: Int): Level {
        val difficulty = levelNumber / 3 + 1 // Increases every 3 levels
        val enemyCount = 5 + (levelNumber * 2)
        val enemies = mutableListOf<EnemySpawnData>()

        // Spawn different enemy types based on level
        for (i in 0 until enemyCount) {
            val enemyType = when {
                levelNumber <= 2 -> EnemyType.SCOUT
                levelNumber <= 5 -> if (i % 2 == 0) EnemyType.ROBOT else EnemyType.SCOUT
                levelNumber <= 8 -> if (i % 3 == 0) EnemyType.HEAVY_ARMOR else EnemyType.ROBOT
                else -> when (i % 3) {
                    0 -> EnemyType.HEAVY_ARMOR
                    1 -> EnemyType.ROBOT
                    else -> EnemyType.SCOUT
                }
            }

            val x = (i % 4) * 10f - 15f
            val y = 1f
            val z = (i / 4) * 10f - 10f

            enemies.add(EnemySpawnData(enemyType, x, y, z))
        }

        // Add boss on final level
        if (levelNumber == TOTAL_LEVELS) {
            enemies.add(EnemySpawnData(EnemyType.BOSS, 0f, 1f, 30f))
        }

        return Level(
            number = levelNumber,
            name = "Level $levelNumber",
            difficulty = difficulty,
            enemies = enemies
        )
    }

    fun loadLevel(levelNumber: Int): Level? {
        return levels[levelNumber]
    }

    fun getEnemyData(levelNumber: Int): List<EnemySpawnData> {
        return levels[levelNumber]?.enemies ?: emptyList()
    }

    fun cleanup() {
        levels.clear()
    }
}

data class Level(
    val number: Int,
    val name: String,
    val difficulty: Int,
    val enemies: List<EnemySpawnData>
)
