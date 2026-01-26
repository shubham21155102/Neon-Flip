import React from 'react';
import { View } from 'react-native';

interface FloorProps {
  body: Matter.Body;
  size: object[];
}

export const Floor: React.FC<FloorProps> = ({ body, size }) => {
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
          backgroundColor: '#00ffff',
          opacity: 0.8,
          borderRadius: 2,
        },
      ]}
    />
  );
};
