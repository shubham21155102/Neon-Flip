import { Controller, Post, Get, UseGuards, Request, Body } from '@nestjs/common';
import { ScoresService } from './scores.service';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { SubmitScoreDto } from './dto/submit-score.dto';

@Controller('scores')
@UseGuards(JwtAuthGuard)
export class ScoresController {
  constructor(private readonly scoresService: ScoresService) {}

  @Post('submit')
  async submitScore(@Request() req, @Body() submitScoreDto: SubmitScoreDto) {
    return this.scoresService.submitScore(req.user.id, submitScoreDto.score);
  }

  @Get('leaderboard')
  async getLeaderboard() {
    return this.scoresService.getLeaderboard();
  }
}
