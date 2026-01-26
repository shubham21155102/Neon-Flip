import { useEffect } from 'react';
import { Stack, useRouter } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { useGameStore } from '../src/store/gameStore';
import * as SecureStore from 'expo-secure-store';
import apiClient from '../src/api/client';

export default function RootLayout() {
  const router = useRouter();
  const isAuthenticated = useGameStore((state) => state.isAuthenticated);
  const setAuth = useGameStore((state) => state.setAuth);

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const token = await SecureStore.getItemAsync('userToken');

      if (token) {
        // Set the token in api client
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        // Fetch user profile
        const response = await apiClient.get('/users/me');
        setAuth(token, response.data);
        router.replace('/');
      } else {
        router.replace('/login');
      }
    } catch (error) {
      console.log('Auth check failed:', error);
      await SecureStore.deleteItemAsync('userToken');
      router.replace('/login');
    }
  };

  return (
    <>
      <StatusBar style="light" />
      <Stack screenOptions={{ headerShown: false }}>
        <Stack.Screen name="index" />
        <Stack.Screen name="login" />
        <Stack.Screen name="register" />
        <Stack.Screen name="leaderboard" />
      </Stack>
    </>
  );
}
