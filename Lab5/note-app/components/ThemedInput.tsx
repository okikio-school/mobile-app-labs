import { TextInput, ViewProps, type TextInputProps } from 'react-native';

import { useThemeColor } from '@/hooks/useThemeColor';
import { ThemedView } from './ThemedView';

export type ThemedInputProps = ViewProps & {
  lightColor?: string;
  darkColor?: string;
  lightBackgroundColor?: string;
  darkBackgroundColor?: string;
  icon?: React.ReactNode;

  inputProps?: TextInputProps;
};

export function ThemedInput({ style, icon, lightColor, darkColor, lightBackgroundColor, darkBackgroundColor, inputProps, ...otherProps }: ThemedInputProps) {
  const backgroundColor = useThemeColor({ light: lightColor, dark: darkColor }, 'background');
  const color = useThemeColor({ light: lightBackgroundColor, dark: darkBackgroundColor }, 'text');

  return (
    <ThemedView style={[{ backgroundColor }, style]} {...otherProps} >
      {icon}
      <TextInput style={[{ color }, inputProps?.style]} {...inputProps}  />
    </ThemedView>
  );
}
