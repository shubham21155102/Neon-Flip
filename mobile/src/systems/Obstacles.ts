import Matter from 'matter-js';
import { GameStatus } from '../types';

const OBSTACLE_SPAWN_INTERVAL = 90; // frames
const OBSTACLE_SPEED = 5;
const OBSTACLE_WIDTH = 40;
const OBSTACLE_HEIGHT = 60;

export const obstacleSystem = (entities: any, { time }: any) => {
  const engine = entities.physics.engine;
  const gameStatus = entities.gameStatus?.status || GameStatus.MENU;
  const screenBounds = entities.screenBounds;

  if (!screenBounds || gameStatus !== GameStatus.PLAYING) {
    return entities;
  }

  // Initialize frame counter if it doesn't exist
  if (!entities.obstacleManager) {
    entities.obstacleManager = {
      frameCount: 0,
      obstacles: []
    };
  }

  const manager = entities.obstacleManager;
  manager.frameCount++;

  // Spawn new obstacle
  if (manager.frameCount % OBSTACLE_SPAWN_INTERVAL === 0) {
    const spawnY = Math.random() > 0.5
      ? screenBounds.height * 0.75 // Floor spike
      : screenBounds.height * 0.25; // Ceiling spike

    const obstacle = Matter.Bodies.rectangle(
      screenBounds.width + 50,
      spawnY,
      OBSTACLE_WIDTH,
      OBSTACLE_HEIGHT,
      {
        isStatic: true,
        label: 'obstacle',
        render: {
          fillStyle: '#ff0066'
        }
      }
    );

    Matter.World.add(engine.world, obstacle);
    manager.obstacles.push(obstacle);
  }

  // Move obstacles and remove off-screen ones
  manager.obstacles = manager.obstacles.filter((obstacle: Matter.Body) => {
    Matter.Body.translate(obstacle, { x: -OBSTACLE_SPEED, y: 0 });

    // Remove if off-screen
    if (obstacle.position.x < -50) {
      Matter.World.remove(engine.world, obstacle);
      return false;
    }
    return true;
  });

  return entities;
};
