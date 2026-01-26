import { GameStatus } from '../types';

export const scoreSystem = (entities: any, { time }: any) => {
  const gameStatus = entities.gameStatus?.status || GameStatus.MENU;

  if (gameStatus === GameStatus.PLAYING) {
    // Initialize score if it doesn't exist
    if (!entities.score) {
      entities.score = { value: 0 };
    }

    // Increment score every frame
    entities.score.value += 1;
  }

  return entities;
};
