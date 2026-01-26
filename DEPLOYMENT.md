# Deployment Guide

This guide covers deploying Neon Flip to production.

## Backend Deployment

### Option 1: Deploy to Render

1. **Create account** at [render.com](https://render.com)

2. **Create PostgreSQL Database**
   - Web Service → PostgreSQL
   - Save the DATABASE_URL

3. **Create Web Service (NestJS API)**
   - Connect your GitHub repository
   - Select `backend` folder as root directory
   - Set environment variables:
     ```
     DATABASE_URL=your-database-url
     JWT_SECRET=your-strong-secret
     JWT_EXPIRES_IN=7d
     PORT=3000
     NODE_ENV=production
     ```
   - Build Command: `npm install && npx prisma generate && npx prisma migrate deploy && npm run build`
   - Start Command: `npm run start:prod`

4. **Access your API** at `https://your-api.onrender.com`

### Option 2: Deploy to Railway

```bash
npm install -g railway
railway login
railway init
railway up
railway add postgres
railway add nestjs
```

### Option 3: Deploy to AWS/DigitalOcean/VPS

```bash
# SSH into server
ssh user@your-server

# Install dependencies
sudo apt update
sudo apt install nodejs npm postgresql -y

# Clone repo
git clone https://github.com/shubham21155102/Neon-Flip.git
cd Neon-Flip/backend

# Install
npm install
cp .env.example .env
# Edit .env

# Setup database
sudo -u postgres createdb neon_flip
npx prisma migrate deploy

# Run with PM2
npm install -g pm2
pm2 start dist/main.js --name neon-flip-api
pm2 save
pm2 startup
```

### Option 4: Docker Deployment

```bash
# Build image
docker build -t neon-flip-api ./backend

# Run container
docker run -d \
  --name neon-flip-api \
  -p 3000:3000 \
  --env-file .env \
  neon-flip-api

# Or use Docker Compose
docker-compose up -d
```

## Mobile App Deployment

### 1. Configure EAS

```bash
cd mobile
npm install -g eas-cli
eas login
eas build:configure
```

### 2. Update app.json

Fill in your app details:

```json
{
  "expo": {
    "ios": {
      "bundleIdentifier": "com.yourcompany.neonflip"
    },
    "android": {
      "package": "com.yourcompany.neonflip"
    }
  }
}
```

### 3. Set Production API URL

Create `.env` file:

```
EXPO_PUBLIC_API_URL=https://your-api-url.com
```

### 4. Build

#### Development Build

```bash
eas build --profile development --platform all
```

#### Production Build

```bash
eas build --profile production --platform all
```

### 5. Submit to Stores

#### iOS App Store

```bash
eas submit --platform ios
```

#### Google Play Store

```bash
eas submit --platform android
```

## Database Setup

### Production Database

```bash
# Generate Prisma Client
npx prisma generate

# Run migrations
npx prisma migrate deploy

# (Optional) Seed database
npx prisma db seed
```

### Using Prisma Studio (Development)

```bash
npx prisma studio
```

## Monitoring & Logs

### Backend Logs

```bash
# PM2
pm2 logs neon-flip-api

# Docker
docker logs neon-flip-api

# Railway
railway logs

# Render
# View logs in Render dashboard
```

### Mobile Analytics

Consider integrating:
- [Expo Analytics](https://docs.expo.dev/guides/analytics/)
- [Firebase Analytics](https://firebase.google.com/docs/analytics)

## Security Checklist

- [ ] Change JWT_SECRET to strong random value
- [ ] Enable HTTPS
- [ ] Set secure CORS origin
- [ ] Use environment variables for secrets
- [ ] Enable rate limiting
- [ ] Set up database backups
- [ ] Monitor for suspicious activity

## Scaling Considerations

### Backend
- Add Redis caching for leaderboards
- Use connection pooling for database
- Add load balancer for multiple instances
- Implement CDN for static assets

### Mobile
- Use image optimization
- Lazy load resources
- Implement offline support
- Add crash reporting (Sentry)

## Backup Strategy

### Database Backups

```bash
# Manual backup
pg_dump neon_flip > backup_$(date +%Y%m%d).sql

# Automated backups (cron)
0 2 * * * pg_dump neon_flip | gzip > /backups/neon_flip_$(date +\%Y\%m\%d).sql.gz
```

### Database Restore

```bash
psql neon_flip < backup_20240126.sql
```

## Troubleshooting

### Common Issues

**API Connection Refused**
- Check if backend is running
- Verify EXPO_PUBLIC_API_URL is correct
- Check CORS settings

**Migration Failed**
- Check DATABASE_URL
- Ensure database exists
- Check database permissions

**Build Failed**
- Clear cache: `eas build --clear-cache`
- Check dependencies are compatible
- Review build logs

## Cost Optimization

- Use free tiers for development (Render free tier, etc.)
- Optimize database queries
- Add pagination to leaderboards
- Use CDN for static assets

---

For support, open an issue on GitHub.
