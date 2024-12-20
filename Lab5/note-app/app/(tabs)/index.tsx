import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { ThemedButton } from "@/components/ThemedButton";

import { FlatList, StatusBar, StyleSheet, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

import { Storage } from "expo-sqlite/kv-store";
import { useEffect, useState } from "react";
import { Note, useNotes } from "@/db/note";
import { Card } from "@/components/Card";
import { ThemedInput } from "@/components/ThemedInput";

import { MaterialIcons } from "@expo/vector-icons";
import { useThemeColor } from "@/hooks/useThemeColor";
import { Link } from "expo-router";

export default function HomeScreen() {
  const textColor = useThemeColor({  }, 'text');
  const [text, onChangeText] = useState('Useless Text');
  
  const { state } = useNotes();
  const noteList = state.notes;

  return (
    <SafeAreaProvider>
      <SafeAreaView style={styles.container}>
        <View style={styles.layout}>
          <ThemedInput
            style={styles.searchInputContainer}
            inputProps={{
              onChangeText: onChangeText,
              value: text,
            }}
            icon={<MaterialIcons name="search" size={24} color={textColor} />}
          />

          {noteList.length > 0 ? (
            <View style={styles.notes}>
              <FlatList
                data={noteList}
                renderItem={({ item }) => <Card {...item} />}
                keyExtractor={(item) => item.id}
              />
            </View>
          ) : <ThemedText>No notes here</ThemedText>}
        </View>
      </SafeAreaView>

      <Link 
        href="./edit" 
        style={styles.newNoteButton} 
        onPress={() => console.log("New Note")} 
      >New Note</Link>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: StatusBar.currentHeight || 0,
  },
  layout: {
    flex: 1,
    gap: 8,
    padding: 16,
  },
  notes: {
    flex: 1,
    gap: 8,
  },
  emptyNote: {
    alignItems: "center",
    alignSelf: "center",
  },
  searchInputContainer: {
    flexDirection: "row",
    alignItems: "center",
    gap: 16,
    paddingBlock: 8,
    paddingInline: 16,
    width: "100%",
    borderRadius: 6,
    marginBottom: 16,
  },
  newNoteButton: {
    backgroundColor: "lightblue",
    position: "absolute",
    bottom: 16,
    right: 16,
    padding: 16,
    borderRadius: 50,
    elevation: 3,
    fontWeight: "semibold",
  }
});
