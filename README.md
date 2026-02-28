# Neon Flip

A production-grade 2D mobile game built with React Native (Expo) and NestJS backend.

## 🎮 Game Overview

**Neon Flip** is an endless runner game where players control a cube that must avoid obstacles by flipping gravity. The game features:

- **Gravity Flip Mechanic**: Tap to reverse gravity and move between floor and ceiling
- **Endless Obstacles**: Randomly spawning spikes that scroll from right to left
- **Global Leaderboards**: Compete with players worldwide
- **User Authentication**: Create accounts and track your progress
- **High Score System**: Save and submit your best scores

## 🏗️ Architecture

### Frontend (Mobile)
- **Framework**: React Native with Expo SDK 50
- **Game Engine**: react-native-game-engine + Matter.js (physics)
- **State Management**: Zustand
- **Navigation**: Expo Router
- **Audio**: expo-av
- **Haptics**: expo-haptics

### Backend (API)
- **Framework**: NestJS (Node.js)
- **Database**: PostgreSQL
- **ORM**: Prisma
- **Authentication**: JWT + Passport
- **API Style**: RESTful

## 📁 Project Structure

```
Neon-Flip/
├── mobile/                 # React Native Expo app
│   ├── app/               # Expo Router pages
│   ├── src/
│   │   ├── api/           # API client & services
│   │   ├── components/    # Game components (Player, Floor, Obstacle)
│   │   ├── entities/      # Game world entities
│   │   ├── screens/       # Screen components
│   │   ├── systems/       # Game systems (Physics, Obstacles, Collision)
│   │   ├── store/         # Zustand state management
│   │   └── types/         # TypeScript types
│   ├── assets/            # Images, sounds, fonts
│   ├── package.json
│   └── app.json
├── backend/               # NestJS API
│   ├── src/
│   │   ├── auth/          # Authentication module
│   │   ├── users/         # Users module
│   │   ├── scores/        # Scores & leaderboards
│   │   ├── prisma/        # Prisma service
│   │   ├── main.ts
│   │   └── app.module.ts
│   ├── prisma/
│   │   └── schema.prisma  # Database schema
│   ├── package.json
│   └── .env.example
└── docker-compose.yml     # Development environment
```

## 🚀 Getting Started

### Prerequisites

- **Node.js** 20+ and npm/yarn
- **PostgreSQL** 15+ (or use Docker)
- **Expo CLI** (`npm install -g expo-cli`)
- **Expo Go** app on your mobile device (for development)

### 1. Clone & Install

```bash
git clone https://github.com/shubham21155102/Neon-Flip.git
cd Neon-Flip
```

#### Backend Setup

```bash
cd backend
npm install

# Copy environment file
cp .env.example .env

# Edit .env and set your values
# DATABASE_URL="postgresql://postgres:password@localhost:5432/neon_flip?schema=public"
# JWT_SECRET="your-secret-key"

# Generate Prisma client
npx prisma generate

# Run database migrations
npx prisma migrate dev --name init

# Start backend
npm run start:dev
```

#### Mobile Setup

```bash
cd mobile
npm install

# Create environment file
echo "EXPO_PUBLIC_API_URL=http://localhost:3000" > .env

# Start Expo development server
npx expo start
```

### 2. Using Docker (Recommended)

```bash
# Start PostgreSQL and backend (development)
docker compose -f docker-compose.dev.yml up -d

# The API will be available at http://localhost:3000
# Database at localhost:5432
```

For production compose (using pushed image from GHCR):

```bash
docker compose -f docker-compose.prod.yml up -d
```

### 3. Running the Game

1. **Backend**: Ensure backend is running (port 3000)
2. **Mobile**: Run `npx expo start` in the `mobile/` directory
3. **Scan QR** code with Expo Go app on your phone
4. **Play**!

## 🎯 Game Mechanics

### Core Gameplay

1. **Player Movement**: Cube falls based on gravity direction
2. **Gravity Flip**: Tap screen to reverse gravity (up/down)
3. **Obstacles**: Spikes spawn from right and move left
4. **Collision**: Hitting a spike ends the game
5. **Scoring**: Score increases every frame you survive

### Technical Implementation

#### Physics World (Matter.js)

```typescript
// Gravity flip mechanic
const toggleGravity = (engine) => {
  const currentGravity = engine.world.gravity.y;
  engine.world.gravity.y = currentGravity > 0 ? -1 : 1;
};
```

#### Game Loop

```typescript
// 60 FPS update cycle
const systems = [
  physicsSystem,    // Matter.js physics update
  obstacleSystem,   // Spawn and move obstacles
  collisionSystem,  // Detect collisions
  scoreSystem       // Update score
];
```

## 🔐 API Endpoints

### Authentication

- `POST /auth/register` - Create new account
- `POST /auth/login` - Login and get JWT token
- `GET /auth/profile` - Get current user profile (protected)

### Users

- `GET /users/me` - Get user profile and high score (protected)

### Scores

- `POST /scores/submit` - Submit a game score (protected)
- `GET /scores/leaderboard` - Get top 50 players

## 🗄️ Database Schema

```prisma
model User {
  id               String    @id @default(uuid())
  username         String    @unique
  email            String    @unique
  passwordHash     String
  highScore        Int       @default(0)
  totalGamesPlayed Int       @default(0)
  createdAt        DateTime  @default(now())
  updatedAt        DateTime  @updatedAt
}

model Score {
  id        String   @id @default(uuid())
  userId    String
  score     Int
  timestamp DateTime @default(now())
  user      User     @relation(fields: [userId], references: [id])
}
```

## 🛠️ Development

### Backend Commands

```bash
npm run start:dev    # Start with hot-reload
npm run build        # Build for production
npm run test         # Run unit tests
npm run test:e2e     # Run e2e tests
npm run prisma:studio # Open Prisma Studio
```

### Mobile Commands

```bash
npx expo start       # Start development server
npx expo start --ios # Run on iOS simulator
npx expo start --android # Run on Android emulator
eas build --platform all  # Build for production
```

## 📦 Building for Production

### Mobile App

1. **Configure EAS** (Expo Application Services)

```bash
npm install -g eas-cli
eas login
eas build:configure
```

2. **Update app.json** with your app credentials

3. **Build**

```bash
eas build --platform all
```

### Backend

```bash
cd backend
npm run build
npm run start:prod
```

Or use Docker:

```bash
docker build -t neon-flip-api ./backend
docker run -p 3000:3000 neon-flip-api
```

## 🐳 GitHub Packages (GHCR)

This repository includes a GitHub Actions pipeline at:

- `.github/workflows/publish-backend-image.yml`

It builds `backend/Dockerfile` and pushes images to:

- `ghcr.io/<owner>/neon-flip-api`

It runs on:

- Push to `main` or `master`
- Git tags matching `v*`
- Manual trigger (`workflow_dispatch`)

Generated tags include:

- `latest` (default branch only)
- branch name
- git tag name
- `sha-<commit>`

## 🌍 Environment Variables

### Backend (.env)

```
DATABASE_URL="postgresql://user:password@host:5432/dbname"
JWT_SECRET="your-secret-key"
JWT_EXPIRES_IN="7d"
PORT=3000
NODE_ENV="production"
```

### Mobile (.env)

```
EXPO_PUBLIC_API_URL="https://your-api-url.com"
```

## 🧪 Testing

### Backend Tests

```bash
# Unit tests
npm run test

# E2E tests
npm run test:e2e

# Coverage
npm run test:cov
```

### Mobile Testing

Use Jest + React Native Testing Library for component tests.

## 📈 Roadmap

- [x] Core game mechanics (gravity flip)
- [x] Physics system (Matter.js)
- [x] Obstacle generation
- [x] Collision detection
- [x] Score system
- [x] User authentication
- [x] High score tracking
- [x] Global leaderboards
- [ ] Sound effects integration
- [ ] Particle effects
- [ ] Screen shake on death
- [ ] Skin shop system
- [ ] Daily challenges
- [ ] Multiplayer mode
- [ ] Achievements system

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Commit Convention

We use [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes
- `refactor:` - Code refactoring
- `test:` - Test changes
- `chore:` - Build process or auxiliary tool changes

## 📝 License

This project is licensed under the MIT License.

## 👤 Author

**Shubham Sharma** ([shubham21155102](https://github.com/shubham21155102))

## 🙏 Acknowledgments

- React Native Game Engine community
- Matter.js physics engine
- Expo team for amazing developer tools
- NestJS framework

---

**Built with ❤️ by shubham21155102**
