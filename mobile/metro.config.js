const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Increase file watcher limits to prevent EMFILE errors
config.watchFolders = [__dirname];
config.maxWorkers = 2;
config.resetCache = true;

module.exports = config;
