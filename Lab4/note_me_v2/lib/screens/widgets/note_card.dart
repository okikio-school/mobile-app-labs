
import 'dart:io';

import 'package:flutter/material.dart';

import '../../models/note_model.dart';
import '../note_view.dart';

class NoteCard extends StatelessWidget {
  const NoteCard(
      {super.key,
        required this.note,
        required this.index,
        required this.onNoteUpdated,
        required this.onNoteDeleted});

  final Note note;
  final int index;
  final Function(Note) onNoteUpdated;
  final Function(Note) onNoteDeleted;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => NoteView(
            note: note,
            onNoteUpdated: (updatedNote) {
              onNoteUpdated(updatedNote); // Pass the updated note back
              Navigator.of(context).pop();
            },
            onNoteDeleted: (deletedNote) {
              onNoteDeleted(deletedNote); // Pass the deleted note back
              Navigator.of(context).pop();
            },
          ),
        ));
      },
      child: Card(
        color: Color(
            note.color), // Set the background color from the saved color value
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                note.title,
                style: const TextStyle(
                  fontSize: 20,
                ),
              ),
              const SizedBox(
                height: 10,
              ),
              Text(
                note.content,
                style: const TextStyle(
                  fontSize: 16,
                ),
                maxLines: 3,
                overflow: TextOverflow.ellipsis,
              ),
              if (note.image != null)
                Image.file(
                  File(note.image!),
                  width: 100,
                  height: 100,
                  fit: BoxFit.cover,
                ),
            ],
          ),
        ),
      ),
    );
  }
}
