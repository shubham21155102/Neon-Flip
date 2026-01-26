import Matter from 'matter-js';
import { Player } from '../components/Player';
import { Floor } from '../components/Floor';
import { Obstacle } from '../components/Obstacle';
import { setupCollisionHandling } from '../systems/Collision';

const PLAYER_SIZE = 40;
const FLOOR_HEIGHT = 20;

export const createWorld = (
  width: number,
  height: number,
  onGameOver: () => void
) => {
  const engine = Matter.Engine.create({
    gravity: { x: 0, y: 1 }
  });

  // Create player
  const player = Matter.Bodies.rectangle(
    width / 4,
    height / 2,
    PLAYER_SIZE,
    PLAYER_SIZE,
    {
      label: 'player',
      restitution: 0,
      friction: 0,
      frictionAir: 0.01,
      density: 0.001
    }
  );

  // Create floor
  const floor = Matter.Bodies.rectangle(
    width / 2,
    height - FLOOR_HEIGHT / 2,
    width,
    FLOOR_HEIGHT,
    {
      isStatic: true,
      label: 'floor',
      friction: 0
    }
  );

  // Create ceiling
  const ceiling = Matter.Bodies.rectangle(
    width / 2,
    FLOOR_HEIGHT / 2,
    width,
    FLOOR_HEIGHT,
    {
      isStatic: true,
      label: 'ceiling',
      friction: 0
    }
  );

  // Create walls
  const leftWall = Matter.Bodies.rectangle(
    -50,
    height / 2,
    100,
    height,
    { isStatic: true, label: 'wall' }
  );

  const rightWall = Matter.Bodies.rectangle(
    width + 50,
    height / 2,
    100,
    height,
    { isStatic: true, label: 'wall' }
  );

  Matter.World.add(engine.world, [player, floor, ceiling, leftWall, rightWall]);

  // Setup collision detection
  setupCollisionHandling(engine, onGameOver);

  return {
    physics: { engine },
    player: {
      body: player,
      size: [PLAYER_SIZE, PLAYER_SIZE],
      color: '#00ff00',
      renderer: Player
    },
    floor: {
      body: floor,
      size: [width, FLOOR_HEIGHT],
      renderer: Floor
    },
    ceiling: {
      body: ceiling,
      size: [width, FLOOR_HEIGHT],
      renderer: Floor
    }
  };
};

export const toggleGravity = (engine: Matter.Engine) => {
  const currentGravity = engine.world.gravity.y;
  const newGravity = currentGravity > 0 ? -1 : 1;
  engine.world.gravity.y = newGravity;
  return newGravity;
};
