
import 'package:flutter/material.dart';

import '../models/note_model.dart';
import '../services/database_service.dart';

class NewNotesScreen extends StatefulWidget {
  const NewNotesScreen({super.key, required this.onNewNoteCreated});

  final Function(Note) onNewNoteCreated;

  @override
  State<NewNotesScreen> createState() => _NewNotesScreenState();
}

class _NewNotesScreenState extends State<NewNotesScreen> {
  final titleController = TextEditingController();
  final contentController = TextEditingController();
  Color selectedColor = Colors.transparent;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('New Note'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(15.0),
        child: Column(
          children: [
            TextFormField(
              controller: titleController,
              style: const TextStyle(fontSize: 28),
              decoration: const InputDecoration(
                  border: InputBorder.none, hintText: "Title"),
            ),
            TextFormField(
                controller: contentController,
                style: const TextStyle(fontSize: 18),
                decoration: const InputDecoration(
                    border: InputBorder.none, hintText: "Note")),
            const SizedBox(height: 50),
            Row(
              children: [
                GestureDetector(
                  onTap: () async {
                    Color? color = await showDialog(
                      context: context,
                      builder: (context) => AlertDialog(
                        title: const Text('Select a Color'),
                        content: SingleChildScrollView(
                          child: Wrap(
                            spacing: 10,
                            children: [
                              Colors.red,
                              Colors.green,
                              Colors.blue,
                              Colors.yellow,
                              Colors.orange,
                              Colors.purple,
                              Colors.pink,
                              Colors.brown,
                              Colors.amber,
                              Colors.lightBlue,
                              Colors.lightGreen,
                              Colors.blueGrey
                            ]
                                .map((color) => GestureDetector(
                              onTap: () =>
                                  Navigator.pop(context, color),
                              child: Container(
                                width: 24,
                                height: 24,
                                decoration: BoxDecoration(
                                  color: color,
                                  borderRadius:
                                  BorderRadius.circular(50),
                                ),
                              ),
                            ))
                                .toList(),
                          ),
                        ),
                      ),
                    );
                    if (color != null) {
                      setState(() => selectedColor = color);
                    }
                  },
                  child: Row(
                    children: [
                      Text('Pick a color: '),
                      Container(
                        width: 24,
                        height: 24,
                        decoration: BoxDecoration(
                          color: selectedColor,
                          borderRadius: BorderRadius.circular(50),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Check if the title or content is empty
          if (titleController.text.isEmpty && contentController.text.isEmpty) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                  content: Text("Title or content must be provided.")),
            );
            return;
          }

          // Create the note object
          final note = Note(
            title: titleController.text,
            content: contentController.text,
            color: selectedColor.value,
          );

          // Save the note to the database and await the operation
          DatabaseService.instance.addNote(
            note.title,
            note.content,
            note.color,
            null,
          );

          // Notify HomeScreen and close the NewNotesScreen
          widget.onNewNoteCreated(note);
          Navigator.of(context).pop();
        },
        child: const Icon(Icons.save),
      ),
    );
  }
}
