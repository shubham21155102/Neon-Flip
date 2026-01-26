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
    return this.prisma.user.create({
      data,
    });
  }

  async findById(id: string): Promise<User | null> {
    return this.prisma.user.findUnique({
      where: { id },
    });
  }

  async findByEmail(email: string): Promise<User | null> {
    return this.prisma.user.findUnique({
      where: { email },
    });
  }

  async findByUsername(username: string): Promise<User | null> {
    return this.prisma.user.findUnique({
      where: { username },
    });
  }

  async updateHighScore(userId: string, score: number): Promise<User> {
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
    return this.prisma.user.findMany({
      take: limit,
      orderBy: {
        highScore: 'desc',
      },
      select: {
        username: true,
        highScore: true,
      },
    });
  }
}
