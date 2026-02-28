import { Injectable } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { UsersService } from '../users/users.service';

@Injectable()
export class ScoresService {
  constructor(
    private prisma: PrismaService,
    private usersService: UsersService,
  ) {}

  async submitScore(userId: string, score: number) {
    console.log('[ScoresService] Submit score started', { userId, score });

    // Get current user first
    const user = await this.usersService.findById(userId);
    if (!user) {
      console.warn('[ScoresService] User not found while submitting score', { userId });
      throw new Error('User not found');
    }

    // Save score record
    const createdScore = await this.prisma.score.create({
      data: {
        userId,
        score,
      },
    });
    console.log('[ScoresService] Score record saved', { userId, score, scoreId: createdScore.id });

    // Update high score if needed
    if (score > user.highScore) {
      console.log('[ScoresService] New high score detected', {
        userId,
        previousHighScore: user.highScore,
        newHighScore: score,
      });
      await this.usersService.updateHighScore(userId, score);
    }

    console.log('[ScoresService] Score submitted successfully', {
      userId,
      highScore: user.highScore,
    });

    // Return the score in the format expected by the Android app
    return {
      id: createdScore.id,
      userId: createdScore.userId,
      username: user.username,
      score: createdScore.score,
      createdAt: createdScore.timestamp.toISOString(),
    };
  }

  async getLeaderboard(limit: number = 50) {
    console.log('[ScoresService] Fetching leaderboard', { limit });
    const entries = await this.usersService.getLeaderboard(limit);
    console.log('[ScoresService] Leaderboard fetched', { count: entries.length });

    // Return in the format expected by the Android app
    return entries.map((entry) => ({
      id: entry.id,
      userId: entry.id,
      username: entry.username,
      score: entry.highScore,
      createdAt: entry.createdAt.toISOString(),
    }));
  }
}
