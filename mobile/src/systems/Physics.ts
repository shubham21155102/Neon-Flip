import Matter from 'matter-js';
import { GameStatus } from '../types';

export const physicsSystem = (entities: any, { touches, time }: any) => {
  const engine = entities.physics.engine;
  const gameStatus = entities.gameStatus?.status || GameStatus.MENU;

  // Only update physics if game is running
  if (gameStatus === GameStatus.PLAYING) {
    Matter.Engine.update(engine, time.delta);
  }

  return entities;
};
