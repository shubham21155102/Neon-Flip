import React from 'react';
import { View } from 'react-native';

interface PlayerProps {
  body: Matter.Body;
  size: object[];
  color: string;
}

export const Player: React.FC<PlayerProps> = ({ body, size, color }) => {
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
          backgroundColor: color,
          borderRadius: 4,
          shadowColor: color,
          shadowOffset: { width: 0, height: 0 },
          shadowOpacity: 0.8,
          shadowRadius: 10,
          elevation: 5,
        },
      ]}
    />
  );
};
