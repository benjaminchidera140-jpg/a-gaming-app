package com.elitesoldier.game.game.world

import com.elitesoldier.game.game.input.InputManager
import com.elitesoldier.game.game.player.Player
import com.elitesoldier.game.game.enemy.EnemyManager
import com.elitesoldier.game.game.level.LevelManager
import com.elitesoldier.game.game.physics.PhysicsEngine

/**
 * Represents the game world and manages all game objects
 */
class GameWorld {

    private val player: Player = Player()
    private val enemyManager: EnemyManager = EnemyManager()
    private val levelManager: LevelManager = LevelManager()
    private val physicsEngine: PhysicsEngine = PhysicsEngine()

    // Game state
    private var currentLevel = 1
    private var gameScore = 0
    private var isGameActive = true
    private var isGameOver = false
    private var isGameWon = false

    init {
        loadLevel(currentLevel)
    }

    fun handleInput(inputManager: InputManager) {
        // Update player input
        player.handleInput(inputManager)
    }

    fun update(deltaTime: Float) {
        if (!isGameActive) return

        // Update player
        player.update(deltaTime)

        // Update enemies
        enemyManager.update(deltaTime)

        // Update physics
        physicsEngine.update(deltaTime)

        // Check collisions
        checkCollisions()

        // Check level completion
        checkLevelCompletion()
    }

    private fun checkCollisions() {
        // Implement collision detection between:
        // - Player shots and enemies
        // - Enemies and player
        // - Player and level geometry
        // - Player and power-ups
    }

    private fun checkLevelCompletion() {
        if (enemyManager.areAllEnemiesDefeated() && !isGameWon) {
            completeLevel()
        }
    }

    private fun completeLevel() {
        gameScore += 5000 // Level completion bonus
        currentLevel++

        if (currentLevel > LevelManager.TOTAL_LEVELS) {
            isGameWon = true
            isGameActive = false
        } else {
            loadLevel(currentLevel)
        }
    }

    private fun loadLevel(level: Int) {
        levelManager.loadLevel(level)
        enemyManager.clear()
        enemyManager.spawnEnemies(levelManager.getEnemyData(level))
    }

    fun addScore(points: Int) {
        gameScore += points
    }

    fun getScore(): Int = gameScore

    fun getPlayer(): Player = player

    fun getEnemyManager(): EnemyManager = enemyManager

    fun getCurrentLevel(): Int = currentLevel

    fun isGameWon(): Boolean = isGameWon

    fun isGameOver(): Boolean = isGameOver

    fun shutdown() {
        player.cleanup()
        enemyManager.clear()
        levelManager.cleanup()
    }
}
