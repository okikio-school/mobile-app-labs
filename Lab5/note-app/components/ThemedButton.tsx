import React from 'react';
import { Text, StyleSheet, Pressable, type ButtonProps, type StyleProp, type ViewStyle } from 'react-native';
import { ThemedText } from './ThemedText';

export type ThemedButtonProps = ButtonProps & {
  title?: string;
  style?: StyleProp<ViewStyle>;
}

export function ThemedButton(props: ThemedButtonProps) {
  const { onPress, title = 'Save' } = props;
  return (
    <Pressable style={[styles.button, props.style]} onPress={onPress}>
      <ThemedText style={styles.text}>{title}</ThemedText>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: 'black',
  },
  text: {
    fontSize: 16,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'black',
  },
});

export default ThemedButton;
