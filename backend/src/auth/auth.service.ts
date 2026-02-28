import { Injectable, UnauthorizedException, ConflictException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import * as bcrypt from 'bcrypt';
import { UsersService } from '../users/users.service';
import { RegisterDto } from './dto/register.dto';
import { LoginDto } from './dto/login.dto';

@Injectable()
export class AuthService {
  constructor(
    private usersService: UsersService,
    private jwtService: JwtService,
  ) {}

  async register(registerDto: RegisterDto) {
    const { email, username, password } = registerDto;
    console.log('[AuthService] Register flow started', { email, username });

    // Check if user exists
    const existingUser = await this.usersService.findByEmail(email);
    if (existingUser) {
      console.warn('[AuthService] Register failed: email already exists', { email });
      throw new ConflictException('User with this email already exists');
    }

    const existingUsername = await this.usersService.findByUsername(username);
    if (existingUsername) {
      console.warn('[AuthService] Register failed: username already taken', { username });
      throw new ConflictException('Username already taken');
    }

    // Hash password
    console.log('[AuthService] Hashing password', { email });
    const passwordHash = await bcrypt.hash(password, 10);

    // Create user
    const user = await this.usersService.create({
      email,
      username,
      passwordHash,
    });
    console.log('[AuthService] User created successfully', {
      userId: user.id,
      email: user.email,
      username: user.username,
    });

    // Generate token
    const token = this.generateToken(user.id, user.email, user.username);
    console.log('[AuthService] Register flow completed', { userId: user.id });

    return {
      token,
      user: {
        id: user.id,
        username: user.username,
        email: user.email,
        createdAt: user.createdAt,
      },
    };
  }

  async validateUser(username: string, password: string): Promise<any> {
    console.log('[AuthService] Validating user credentials', { username });
    const user = await this.usersService.findByUsername(username);
    if (!user) {
      console.warn('[AuthService] Validation failed: user not found', { username });
      return null;
    }

    const isPasswordValid = await bcrypt.compare(password, user.passwordHash);
    if (!isPasswordValid) {
      console.warn('[AuthService] Validation failed: invalid password', { username });
      return null;
    }

    console.log('[AuthService] Validation successful', { userId: user.id, username });
    return user;
  }

  async login(loginDto: LoginDto) {
    const { username, password } = loginDto;
    console.log('[AuthService] Login flow started', { username });

    const user = await this.validateUser(username, password);
    if (!user) {
      console.warn('[AuthService] Login failed: invalid credentials', { username });
      throw new UnauthorizedException('Invalid credentials');
    }

    const token = this.generateToken(user.id, user.email, user.username);
    console.log('[AuthService] Login successful', { userId: user.id, username });

    return {
      token,
      user: {
        id: user.id,
        username: user.username,
        email: user.email,
        createdAt: user.createdAt,
      },
    };
  }

  private generateToken(userId: string, email: string, username: string): string {
    const payload = { sub: userId, email, username };
    console.log('[AuthService] Generating JWT token', { userId, email });
    return this.jwtService.sign(payload);
  }
}
