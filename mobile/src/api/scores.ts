import apiClient from './client';

export interface ScoreSubmission {
  score: number;
}

export interface LeaderboardEntry {
  username: string;
  highScore: number;
  rank: number;
}

export const scoresService = {
  async submitScore(score: number) {
    const response = await apiClient.post('/scores/submit', { score });
    return response.data;
  },

  async getLeaderboard(): Promise<LeaderboardEntry[]> {
    const response = await apiClient.get('/scores/leaderboard');
    return response.data;
  }
};
