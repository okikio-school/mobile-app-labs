import { useNotes, type Note } from "@/db/note";

import { ThemedText } from "@/components/ThemedText";
import { ThemedButton } from "@/components/ThemedButton";

import { useEffect, useState } from "react";
import { StatusBar, StyleSheet, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

import { Storage } from "expo-sqlite/kv-store";
import { ThemedInput } from "@/components/ThemedInput";
import { useThemeColor } from "@/hooks/useThemeColor";
import { router, Stack } from "expo-router";
import Toast from "react-native-root-toast";

export default function NewNoteScreen() {
  const textColor = useThemeColor({  }, 'text');

  const [title, onChangeTitle] = useState('');
  const [subtitle, onChangeSubtitle] = useState('');
  const [description, onChangeDescription] = useState('');

  
  const { state, dispatch } = useNotes();
  const noteList = state.notes;

  const saveNote = async () => {
    if (title.trim().length === 0) {// Add a Toast on screen.
      Toast.show('Missing title', {
        duration: Toast.durations.SHORT,
      });
      return;
    }

    const newNote = {
      id: `${noteList.length}`,
      title: title,
      subtitle: subtitle,
      description: description,
      color: "lightblue",
    };

    dispatch({ type: "ADD_NOTE", payload: newNote });
    console.log("New Note")
    
    router.push("./");
  }

  return (
    <>
      <Stack.Screen 
        options={{ 
          title: '', 
          headerShown: true, 
          headerBackVisible: true,
          headerStyle: { backgroundColor: 'transparent' },
        }} 
      />
      <SafeAreaProvider>
        <SafeAreaView style={styles.container}>
          <View style={styles.layout}>
            <ThemedText type="title">New Note</ThemedText>

            
            <ThemedText type="default">Title</ThemedText>
            <ThemedInput
              style={styles.searchInputContainer}
              inputProps={{
                onChangeText: onChangeTitle,
                value: title,
              }}
            />
            
            <ThemedText type="default">Subtitle</ThemedText>
            <ThemedInput
              style={styles.searchInputContainer}
              inputProps={{
                onChangeText: onChangeSubtitle,
                value: subtitle,
              }}
            />
            
            <ThemedText type="default">Description</ThemedText>
            <ThemedInput
              style={styles.searchInputContainer}
              inputProps={{
                onChangeText: onChangeDescription,
                value: description,
              }}
            />

            <ThemedButton 
              title="Save Note"
              style={styles.saveNoteButton} 
              onPress={() => saveNote()} 
            />
          </View>
        </SafeAreaView>
      </SafeAreaProvider>
    </>
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
  saveNoteButton: {
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
