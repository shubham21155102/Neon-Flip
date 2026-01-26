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
    // Save score record
    await this.prisma.score.create({
      data: {
        userId,
        score,
      },
    });

    // Get current user
    const user = await this.usersService.findById(userId);

    // Update high score if needed
    if (score > user.highScore) {
      await this.usersService.updateHighScore(userId, score);
      return {
        success: true,
        newHighScore: true,
        highScore: score,
      };
    }

    return {
      success: true,
      newHighScore: false,
      highScore: user.highScore,
    };
  }

  async getLeaderboard(limit: number = 50) {
    const entries = await this.usersService.getLeaderboard(limit);

    return entries.map((entry, index) => ({
      rank: index + 1,
      username: entry.username,
      highScore: entry.highScore,
    }));
  }
}
