import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { ThemedButton } from "@/components/Button";

import { FlatList, StatusBar, StyleSheet, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

import { Storage } from "expo-sqlite/kv-store";
import { useEffect, useState } from "react";
import { Note } from "@/db/note";
import { Card } from "@/components/Card";
import { ThemedInput } from "@/components/ThemedInput";

import { MaterialIcons } from "@expo/vector-icons";
import { useThemeColor } from "@/hooks/useThemeColor";

export default function HomeScreen() {
  const textColor = useThemeColor({  }, 'text');
  const [noteList, setNoteList] = useState<Note[]>([
    {
      id: "1",
      title: "Hello World",
      subtitle: "This is a subtitle",
      description: "This is a description",
      color: "lightblue",
    }
  ]);
  
  const [text, onChangeText] = useState('Useless Text');

  useEffect(() => {
    // Storage.getItemAsync("notes").then((data) => {
    //   if (data === null) return;

    //   try {
    //     const notes = JSON.parse(data) as Note[];
    //     setNoteList(notes);
    //   } catch (e) {
    //     console.log("Error reading notes", e);
    //   }
    // });
  }, []);

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

      <Button title="New Note" style={styles.newNoteButton} onPress={() => console.log("New Note")} />
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
    position: "absolute",
    bottom: 16,
    right: 16,
  }
});
