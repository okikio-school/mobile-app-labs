import { StyleSheet, Text, type ViewProps, type ColorValue } from "react-native";
import { ThemedView } from "./ThemedView";
import { ThemedText } from "./ThemedText";

export interface CardProps extends ViewProps {
  title: string;
  subtitle: string;
  description: string;
  color: ColorValue;
}

export function Card(props: CardProps) {
  return (
    <ThemedView 
      style={[
        cardstyles.card,
        { backgroundColor: props.color ?? "white" },
      ]}
    >
      <Text style={cardstyles.cardTitle}>{props.title}</Text>
      <Text style={cardstyles.cardContent}>{props.subtitle}</Text>
      <Text style={cardstyles.cardContent}>{props.description}</Text>
    </ThemedView>
  );
}

const cardstyles = StyleSheet.create({
    card: {
        backgroundColor: 'white',
        borderRadius: 10,
        padding: 16,
        shadowColor: 'black',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 4,
    },
    cardTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    cardContent: {
        fontSize: 16,
    },
    cardActions: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        marginTop: 16,
    },
    cardAction: {
        marginLeft: 16,
    },
})