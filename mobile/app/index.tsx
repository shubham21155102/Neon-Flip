import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Dimensions } from 'react-native';
import { useRouter } from 'expo-router';
import { useGameStore } from '../src/store/gameStore';
import { authService } from '../src/api/auth';
import * as SecureStore from 'expo-secure-store';
import GameScreen from '../src/screens/GameScreen';

const { width } = Dimensions.get('window');

export default function HomeScreen() {
  const router = useRouter();
  const user = useGameStore((state) => state.user);
  const logout = useGameStore((state) => state.logout);

  const handleLogout = async () => {
    try {
      await authService.logout();
      logout();
      router.replace('/login');
    } catch (error) {
      console.log('Logout error:', error);
    }
  };

  const MenuOverlay = () => (
    <View style={styles.menuOverlay}>
      <View style={styles.userInfo}>
        <Text style={styles.username}>{user?.username}</Text>
        <Text style={styles.highScore}>High Score: {user?.highScore || 0}</Text>
      </View>

      <View style={styles.menuButtons}>
        <TouchableOpacity
          style={styles.menuButton}
          onPress={() => router.push('/leaderboard')}
        >
          <Text style={styles.menuButtonText}>LEADERBOARD</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.menuButton, styles.logoutButton]}
          onPress={handleLogout}
        >
          <Text style={styles.menuButtonText}>LOGOUT</Text>
        </TouchableOpacity>
      </View>
    </View>
  );

  return (
    <View style={styles.container}>
      <GameScreen />

      {/* Add menu button in top-right corner when playing */}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0a0a0a',
  },
  menuOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    paddingTop: 60,
    paddingHorizontal: 20,
    zIndex: 100,
  },
  userInfo: {
    backgroundColor: 'rgba(0, 255, 255, 0.1)',
    borderRadius: 10,
    padding: 15,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#00ffff',
  },
  username: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#00ffff',
    marginBottom: 5,
  },
  highScore: {
    fontSize: 14,
    color: '#ffffff',
    opacity: 0.8,
  },
  menuButtons: {
    gap: 10,
  },
  menuButton: {
    backgroundColor: 'rgba(255, 0, 102, 0.2)',
    borderRadius: 10,
    padding: 15,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#ff0066',
  },
  logoutButton: {
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    borderColor: '#ffffff',
  },
  menuButtonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});
