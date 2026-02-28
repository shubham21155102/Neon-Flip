import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { ConfigService } from '@nestjs/config';
import { UsersService } from '../../users/users.service';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor(
    private configService: ConfigService,
    private usersService: UsersService,
  ) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: configService.get<string>('JWT_SECRET'),
    });
  }

  async validate(payload: any) {
    console.log('[JwtStrategy] Validating JWT payload', {
      userId: payload?.sub,
      email: payload?.email,
    });
    const user = await this.usersService.findById(payload.sub);
    if (!user) {
      console.warn('[JwtStrategy] User not found for token payload', {
        userId: payload?.sub,
      });
      return null;
    }
    console.log('[JwtStrategy] JWT validation succeeded', { userId: user.id });
    return {
      id: user.id,
      email: user.email,
      username: user.username,
      highScore: user.highScore,
    };
  }
}
