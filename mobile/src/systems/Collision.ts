import Matter from 'matter-js';
import * as Haptics from 'expo-haptics';
import { GameStatus } from '../types';

export const setupCollisionHandling = (engine: Matter.Engine, onGameOver: () => void) => {
  Matter.Events.on(engine, 'collisionStart', (event) => {
    event.pairs.forEach((pair) => {
      const bodyA = pair.bodyA;
      const bodyB = pair.bodyB;

      // Check if player hit an obstacle
      const playerBody = [bodyA, bodyB].find(
        (body) => body.label === 'player'
      );
      const obstacleBody = [bodyA, bodyB].find(
        (body) => body.label === 'obstacle'
      );

      if (playerBody && obstacleBody) {
        // Trigger haptic feedback
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Heavy);
        onGameOver();
      }
    });
  });
};

export const collisionSystem = (entities: any) => {
  return entities;
};
