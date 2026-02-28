import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { PrismaClient } from '@prisma/client';

@Injectable()
export class PrismaService extends PrismaClient implements OnModuleInit, OnModuleDestroy {
  async onModuleInit() {
    console.log('[PrismaService] Connecting to database');
    await this.$connect();
    console.log('[PrismaService] Database connection established');
  }

  async onModuleDestroy() {
    console.log('[PrismaService] Disconnecting from database');
    await this.$disconnect();
    console.log('[PrismaService] Database connection closed');
  }
}
