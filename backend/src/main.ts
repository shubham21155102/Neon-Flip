import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';

async function bootstrap() {
  console.log('[Main] Starting Neon Flip API bootstrap');
  const app = await NestFactory.create(AppModule);

  // Enable CORS for React Native app
  app.enableCors({
    origin: '*',
    credentials: true,
  });
  console.log('[Main] CORS enabled');

  // Global validation pipe
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      transform: true,
    }),
  );
  console.log('[Main] Global validation pipe configured');

  const port = process.env.PORT || 3000;
  await app.listen(port);

  console.log(`[Main] 🚀 Neon Flip API running on port ${port}`);
}
bootstrap();
