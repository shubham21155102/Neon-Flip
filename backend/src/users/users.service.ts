import { Injectable } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { User } from '@prisma/client';

@Injectable()
export class UsersService {
  constructor(private prisma: PrismaService) {}

  async create(data: {
    email: string;
    username: string;
    passwordHash: string;
  }): Promise<User> {
    console.log('[UsersService] Creating user', {
      email: data.email,
      username: data.username,
    });
    return this.prisma.user.create({
      data,
    });
  }

  async findById(id: string): Promise<User | null> {
    console.log('[UsersService] Finding user by id', { id });
    return this.prisma.user.findUnique({
      where: { id },
    });
  }

  async findByEmail(email: string): Promise<User | null> {
    console.log('[UsersService] Finding user by email', { email });
    return this.prisma.user.findUnique({
      where: { email },
    });
  }

  async findByUsername(username: string): Promise<User | null> {
    console.log('[UsersService] Finding user by username', { username });
    return this.prisma.user.findUnique({
      where: { username },
    });
  }

  async updateHighScore(userId: string, score: number): Promise<User> {
    console.log('[UsersService] Updating high score', { userId, score });
    return this.prisma.user.update({
      where: { id: userId },
      data: {
        highScore: score,
        totalGamesPlayed: {
          increment: 1,
        },
      },
    });
  }

  async getLeaderboard(limit: number = 50) {
    console.log('[UsersService] Getting leaderboard', { limit });
    return this.prisma.user.findMany({
      take: limit,
      orderBy: {
        highScore: 'desc',
      },
      select: {
        id: true,
        username: true,
        highScore: true,
        createdAt: true,
      },
    });
  }
}
