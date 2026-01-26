import React from 'react';
import { View } from 'react-native';

interface ObstacleProps {
  body: Matter.Body;
  size: object[];
}

export const Obstacle: React.FC<ObstacleProps> = ({ body, size }) => {
  const { position } = body;
  const width = size[0];
  const height = size[1];

  return (
    <View
      style={[
        {
          position: 'absolute',
          left: position.x - width / 2,
          top: position.y - height / 2,
          width,
          height,
          backgroundColor: '#ff0066',
          borderRadius: 4,
          shadowColor: '#ff0066',
          shadowOffset: { width: 0, height: 0 },
          shadowOpacity: 0.6,
          shadowRadius: 8,
          elevation: 3,
        },
      ]}
    />
  );
};
