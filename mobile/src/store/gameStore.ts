import { create } from 'zustand';

interface GameState {
  isAuthenticated: boolean;
  token: string | null;
  user: {
    id: string;
    username: string;
    email: string;
    highScore: number;
  } | null;
  currentScore: number;
  isGameOver: boolean;
  isPlaying: boolean;

  setAuth: (token: string, user: GameState['user']) => void;
  logout: () => void;
  startGame: () => void;
  endGame: () => void;
  updateScore: (score: number) => void;
  resetGame: () => void;
}

export const useGameStore = create<GameState>((set) => ({
  isAuthenticated: false,
  token: null,
  user: null,
  currentScore: 0,
  isGameOver: false,
  isPlaying: false,

  setAuth: (token, user) => set({ token, user, isAuthenticated: true }),

  logout: () => set({
    token: null,
    user: null,
    isAuthenticated: false,
    currentScore: 0,
    isGameOver: false,
    isPlaying: false
  }),

  startGame: () => set({ isPlaying: true, isGameOver: false, currentScore: 0 }),

  endGame: () => set({ isPlaying: false, isGameOver: true }),

  updateScore: (score) => set({ currentScore: score }),

  resetGame: () => set({
    currentScore: 0,
    isGameOver: false,
    isPlaying: false
  })
}));
