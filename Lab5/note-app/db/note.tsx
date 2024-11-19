import React, { createContext, useContext, useEffect, useReducer } from "react";
import { Storage } from "expo-sqlite/kv-store"; // Replace with your actual storage library if needed

// Define the Note type
export interface Note {
  id: string;
  title: string;
  subtitle: string;
  description: string;
  color: string;
}

// Define the shape of the context state
interface NotesState {
  notes: Note[];
}

// Define action types for the reducer
type NotesAction =
  | { type: "SET_NOTES"; payload: Note[] }
  | { type: "ADD_NOTE"; payload: Note }
  | { type: "UPDATE_NOTE"; payload: Note }
  | { type: "DELETE_NOTE"; payload: string };

// Initial state for the notes
const initialState: NotesState = {
  notes: [],
};

// Create the reducer
function notesReducer(state: NotesState, action: NotesAction): NotesState {
  switch (action.type) {
    case "SET_NOTES":
      return { notes: action.payload };
    case "ADD_NOTE":
      return { notes: [...state.notes, action.payload] };
    case "UPDATE_NOTE":
      return {
        notes: state.notes.map((note) =>
          note.id === action.payload.id ? action.payload : note
        ),
      };
    case "DELETE_NOTE":
      return {
        notes: state.notes.filter((note) => note.id !== action.payload),
      };
    default:
      return state;
  }
}

// Create the context
const NotesContext = createContext<{
  state: NotesState;
  dispatch: React.Dispatch<NotesAction>;
} | null>(null);

// Create the provider
export function NotesProvider({ children }: React.PropsWithChildren) {
  const [state, dispatch] = useReducer(notesReducer, initialState);

  // Load notes from storage on mount
  useEffect(() => {
    const loadNotes = async () => {
      try {
        const data = await Storage.getItemAsync("notes");
        if (data) {
          const notes = JSON.parse(data) as Note[];
          dispatch({ type: "SET_NOTES", payload: notes });
        }
      } catch (e) {
        console.error("Error loading notes", e);
      }
    };

    loadNotes();
  }, []);

  // Save notes to storage whenever they change
  useEffect(() => {
    Storage.setItemAsync("notes", JSON.stringify(state.notes)).catch((e) =>
      console.error("Error saving notes", e)
    );
  }, [state.notes]);

  return (
    <NotesContext.Provider value={{ state, dispatch }}>
      {children}
    </NotesContext.Provider>
  );
};

// Hook for consuming the notes context
export function useNotes() {
  const context = useContext(NotesContext);
  if (!context) {
    throw new Error("useNotes must be used within a NotesProvider");
  }
  return context;
};
