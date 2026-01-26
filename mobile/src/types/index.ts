export enum GameStatus {
  MENU = 'menu',
  PLAYING = 'playing',
  GAME_OVER = 'game_over'
}

export interface GameEntity {
  renderer?: any;
  body?: Matter.Body;
}
