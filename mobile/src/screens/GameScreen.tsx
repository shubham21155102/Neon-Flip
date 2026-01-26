import React, { useState, useEffect, useRef } from 'react';
import { StyleSheet, Dimensions, View, Text, TouchableOpacity, StatusBar } from 'react-native';
import { GameEngine } from 'react-native-game-engine';
import Matter from 'matter-js';
import { createWorld, toggleGravity } from '../entities';
import { physicsSystem, obstacleSystem, collisionSystem, scoreSystem } from '../systems';
import { GameStatus } from '../types';
import * as Haptics from 'expo-haptics';
import { Audio } from 'expo-av';
import { useGameStore } from '../store/gameStore';
import { scoresService } from '../api/scores';

const { width, height } = Dimensions.get('window');

export default function GameScreen() {
  const [gameEngine, setGameEngine] = useState<any>(null);
  const [entities, setEntities] = useState<any>(null);
  const [gameStatus, setGameStatus] = useState<GameStatus>(GameStatus.MENU);
  const [score, setScore] = useState(0);
  const [gravityDirection, setGravityDirection] = useState(1);

  const { user, startGame, endGame, resetGame } = useGameStore();
  const soundRef = useRef<Audio.Sound | null>(null);

  useEffect(() => {
    loadSounds();
    return () => {
      if (soundRef.current) {
        soundRef.current.unloadAsync();
      }
    };
  }, []);

  const loadSounds = async () => {
    try {
      const { sound } = await Audio.Sound.createAsync(
        require('../../assets/jump.mp3')
      );
      soundRef.current = sound;
    } catch (error) {
      console.log('Error loading sound:', error);
    }
  };

  const playJumpSound = async () => {
    if (soundRef.current) {
      try {
        await soundRef.current.replayAsync();
      } catch (error) {
        console.log('Error playing sound:', error);
      }
    }
  };

  const onGameOver = async () => {
    setGameStatus(GameStatus.GAME_OVER);
    endGame();

    // Submit score to server if authenticated
    if (user) {
      try {
        await scoresService.submitScore(score);
      } catch (error) {
        console.log('Error submitting score:', error);
      }
    }
  };

  const startGameHandler = () => {
    const worldEntities = createWorld(width, height, onGameOver);
    worldEntities.screenBounds = { width, height };
    worldEntities.gameStatus = { status: GameStatus.PLAYING };

    setEntities(worldEntities);
    setGameStatus(GameStatus.PLAYING);
    setScore(0);
    startGame();
  };

  const resetGameHandler = () => {
    if (gameEngine) {
      gameEngine.stop();
    }
    resetGame();
    startGameHandler();
  };

  const handleTouch = async () => {
    if (gameStatus === GameStatus.PLAYING && entities?.physics?.engine) {
      const newDirection = toggleGravity(entities.physics.engine);
      setGravityDirection(newDirection);

      // Haptic feedback
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);

      // Play sound
      playJumpSound();
    }
  };

  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content" />

      {gameStatus === GameStatus.MENU && (
        <View style={styles.menuContainer}>
          <Text style={styles.title}>NEON FLIP</Text>
          <Text style={styles.subtitle}>Tap to flip gravity</Text>

          {user && (
            <View style={styles.userInfo}>
              <Text style={styles.userInfoText}>High Score: {user.highScore}</Text>
            </View>
          )}

          <TouchableOpacity style={styles.startButton} onPress={startGameHandler}>
            <Text style={styles.startButtonText}>START GAME</Text>
          </TouchableOpacity>
        </View>
      )}

      {gameStatus === GameStatus.PLAYING && (
        <>
          <View style={styles.hud}>
            <Text style={styles.scoreText}>{score}</Text>
            <Text style={styles.gravityIndicator}>
              Gravity: {gravityDirection > 0 ? '↓' : '↑'}
            </Text>
          </View>

          <GameEngine
            ref={(ref) => setGameEngine(ref)}
            style={styles.gameContainer}
            systems={[physicsSystem, obstacleSystem, collisionSystem, scoreSystem]}
            entities={entities}
            running={gameStatus === GameStatus.PLAYING}
            onEvent={(e: any) => {
              if (e.type === 'score') {
                setScore(e.payload);
              }
            }}
            onTouchEnd={handleTouch}
          />
        </>
      )}

      {gameStatus === GameStatus.GAME_OVER && (
        <View style={styles.gameOverContainer}>
          <Text style={styles.gameOverTitle}>GAME OVER</Text>
          <Text style={styles.finalScore}>Score: {score}</Text>

          {user && score > user.highScore && (
            <Text style={styles.newHighScore}>NEW HIGH SCORE!</Text>
          )}

          <TouchableOpacity style={styles.restartButton} onPress={resetGameHandler}>
            <Text style={styles.restartButtonText}>PLAY AGAIN</Text>
          </TouchableOpacity>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0a0a0a',
  },
  menuContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0a0a0a',
  },
  title: {
    fontSize: 48,
    fontWeight: 'bold',
    color: '#00ffff',
    marginBottom: 10,
    textShadowColor: '#00ffff',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 20,
  },
  subtitle: {
    fontSize: 18,
    color: '#ffffff',
    marginBottom: 40,
    opacity: 0.8,
  },
  userInfo: {
    marginBottom: 30,
    padding: 15,
    backgroundColor: 'rgba(0, 255, 255, 0.1)',
    borderRadius: 10,
  },
  userInfoText: {
    fontSize: 16,
    color: '#00ffff',
  },
  startButton: {
    backgroundColor: '#00ff00',
    paddingHorizontal: 60,
    paddingVertical: 20,
    borderRadius: 30,
    shadowColor: '#00ff00',
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.8,
    shadowRadius: 15,
    elevation: 5,
  },
  startButtonText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#0a0a0a',
  },
  gameContainer: {
    flex: 1,
    backgroundColor: '#0a0a0a',
  },
  hud: {
    position: 'absolute',
    top: 50,
    left: 0,
    right: 0,
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 30,
    zIndex: 10,
  },
  scoreText: {
    fontSize: 36,
    fontWeight: 'bold',
    color: '#00ffff',
    textShadowColor: '#00ffff',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 10,
  },
  gravityIndicator: {
    fontSize: 24,
    color: '#ff0066',
  },
  gameOverContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(10, 10, 10, 0.95)',
  },
  gameOverTitle: {
    fontSize: 48,
    fontWeight: 'bold',
    color: '#ff0066',
    marginBottom: 20,
    textShadowColor: '#ff0066',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 20,
  },
  finalScore: {
    fontSize: 32,
    color: '#ffffff',
    marginBottom: 10,
  },
  newHighScore: {
    fontSize: 24,
    color: '#00ff00',
    marginBottom: 40,
    fontWeight: 'bold',
  },
  restartButton: {
    backgroundColor: '#00ff00',
    paddingHorizontal: 50,
    paddingVertical: 18,
    borderRadius: 30,
    shadowColor: '#00ff00',
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.8,
    shadowRadius: 15,
    elevation: 5,
  },
  restartButtonText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#0a0a0a',
  },
});
