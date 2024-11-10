import 'package:flutter/material.dart';
import 'package:note_me_v2/screens/widgets/note_card.dart';
import '../models/note_model.dart';
import '../services/database_service.dart';
import 'new_notes_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final DatabaseService _databaseService = DatabaseService.instance;

  List<Note> notes = [];
  String searchQuery = "";

  @override
  void initState() {
    super.initState();
    fetchNotes();
  }

  Future<void> fetchNotes() async {
    final noteData = await _databaseService.fetchAllNotes();
    setState(() {
      notes = noteData.map((e) => Note.fromMap(e)).toList();
    });
  }

  Future<void> searchNotes(String query) async {
    final noteData = await _databaseService.searchNotes(query);
    setState(() {
      notes = noteData.map((e) => Note.fromMap(e)).toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Container(
          decoration: BoxDecoration(
              color: const Color.fromARGB(57, 0, 0, 0),
              borderRadius: BorderRadius.circular(50)),
          child: Padding(
            padding: EdgeInsets.fromLTRB(12, 2, 12, 2),
            child: TextField(
              decoration: InputDecoration(
                icon: Icon(Icons.search),
                hintText: "Search your notes",
                border: InputBorder.none,
              ),
              onChanged: (query) {
                setState(() {
                  searchQuery = query;
                });
                searchNotes(query);
              },
            ),
          ),
        ),
      ),
      body: Center(
        child: notes.isEmpty
            ? const Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Notes you add appear here',
            ),
          ],
        )
            : Padding(
          padding: const EdgeInsets.all(15.0),
          child: ListView.builder(
            itemCount: notes.length,
            itemBuilder: (context, index) {
              return NoteCard(
                note: notes[index],
                index: index,
                onNoteUpdated: (updatedNote) async {
                  await fetchNotes();
                },
                onNoteDeleted: (deletedNote) async {
                  await _databaseService.deleteNoteById(deletedNote.id);
                  await fetchNotes();
                },
              );
            },
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.of(context).push(MaterialPageRoute(
              builder: (context) => NewNotesScreen(
                onNewNoteCreated: (note) async {
                  await fetchNotes();
                },
              )));
        },
        child: const Icon(Icons.add),
      ),
    );
  }

  void onNoteDeleted(Note deletedNote) {
    setState(() {
      notes.removeWhere((note) => note.id == deletedNote.id);
    });
    _databaseService.deleteNoteById(deletedNote.id!);
  }
}
