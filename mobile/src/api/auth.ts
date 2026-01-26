import apiClient from './client';
import * as SecureStore from 'expo-secure-store';

export interface LoginResponse {
  access_token: string;
  user: {
    id: string;
    username: string;
    email: string;
    highScore: number;
  };
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials extends LoginCredentials {
  username: string;
}

export const authService = {
  async login(credentials: LoginCredentials): Promise<LoginResponse> {
    const response = await apiClient.post<LoginResponse>('/auth/login', credentials);
    await SecureStore.setItemAsync('userToken', response.data.access_token);
    return response.data;
  },

  async register(credentials: RegisterCredentials): Promise<LoginResponse> {
    const response = await apiClient.post<LoginResponse>('/auth/register', credentials);
    await SecureStore.setItemAsync('userToken', response.data.access_token);
    return response.data;
  },

  async logout() {
    await SecureStore.deleteItemAsync('userToken');
  },

  async getProfile() {
    const response = await apiClient.get('/users/me');
    return response.data;
  }
};
