import React, { useState, useEffect } from 'react';
import { StyleSheet, View, Text, FlatList, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useRouter } from 'expo-router';
import { scoresService, LeaderboardEntry } from '../api/scores';
import { useGameStore } from '../store/gameStore';

export default function LeaderboardScreen() {
  const router = useRouter();
  const user = useGameStore((state) => state.user);

  const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadLeaderboard();
  }, []);

  const loadLeaderboard = async () => {
    try {
      const data = await scoresService.getLeaderboard();
      setLeaderboard(data);
    } catch (error) {
      console.log('Error loading leaderboard:', error);
    } finally {
      setLoading(false);
    }
  };

  const renderItem = ({ item }: { item: LeaderboardEntry }) => {
    const isCurrentUser = item.username === user?.username;

    return (
      <View
        style={[
          styles.item,
          isCurrentUser && styles.currentUserItem
        ]}
      >
        <Text style={[styles.rank, isCurrentUser && styles.highlightText]}>
          #{item.rank}
        </Text>
        <Text style={[styles.username, isCurrentUser && styles.highlightText]}>
          {item.username}
        </Text>
        <Text style={[styles.score, isCurrentUser && styles.highlightText]}>
          {item.highScore}
        </Text>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => router.back()}>
          <Text style={styles.backButton}>← Back</Text>
        </TouchableOpacity>
        <Text style={styles.title}>LEADERBOARD</Text>
        <View style={{ width: 50 }} />
      </View>

      <View style={styles.headerRow}>
        <Text style={styles.headerText}>RANK</Text>
        <Text style={styles.headerText}>PLAYER</Text>
        <Text style={styles.headerText}>SCORE</Text>
      </View>

      {loading ? (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#00ffff" />
        </View>
      ) : (
        <FlatList
          data={leaderboard}
          renderItem={renderItem}
          keyExtractor={(item) => item.rank.toString()}
          contentContainerStyle={styles.list}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0a0a0a',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: 60,
    paddingBottom: 20,
  },
  backButton: {
    color: '#00ffff',
    fontSize: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#00ffff',
    textShadowColor: '#00ffff',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 10,
  },
  headerRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#333',
  },
  headerText: {
    color: '#666',
    fontSize: 12,
    fontWeight: 'bold',
  },
  list: {
    paddingVertical: 10,
  },
  item: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#1a1a1a',
  },
  currentUserItem: {
    backgroundColor: 'rgba(0, 255, 255, 0.1)',
    borderWidth: 1,
    borderColor: '#00ffff',
  },
  rank: {
    width: 50,
    color: '#ffffff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  username: {
    flex: 1,
    color: '#ffffff',
    fontSize: 16,
  },
  score: {
    width: 80,
    color: '#00ff00',
    fontSize: 16,
    fontWeight: 'bold',
    textAlign: 'right',
  },
  highlightText: {
    color: '#00ffff',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
