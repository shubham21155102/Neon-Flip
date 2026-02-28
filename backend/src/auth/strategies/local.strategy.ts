import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { Strategy } from 'passport-local';
import { AuthService } from '../auth.service';

@Injectable()
export class LocalStrategy extends PassportStrategy(Strategy) {
  constructor(private authService: AuthService) {
    super({
      usernameField: 'email',
    });
  }

  async validate(email: string, password: string): Promise<any> {
    console.log('[LocalStrategy] Validating local login', { email });
    const user = await this.authService.validateUser(email, password);
    if (!user) {
      console.warn('[LocalStrategy] Validation failed', { email });
      throw new UnauthorizedException();
    }
    console.log('[LocalStrategy] Validation succeeded', { userId: user.id, email });
    return user;
  }
}
