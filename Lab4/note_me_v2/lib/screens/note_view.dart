import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:note_me_v2/services/database_service.dart';

import '../models/note_model.dart';



class NoteView extends StatefulWidget {
  const NoteView({
    super.key,
    required this.note,
    required this.onNoteUpdated,
    required this.onNoteDeleted,
  });

  final Note note;
  final VoidCallback onNoteUpdated;
  final VoidCallback onNoteDeleted;

  @override
  State<NoteView> createState() => _NoteViewState();
}

class _NoteViewState extends State<NoteView> {
  late TextEditingController titleController;
  late TextEditingController contentController;
  late Color selectedColor;
  String? imagePath;

  @override
  void initState() {
    super.initState();
    titleController = TextEditingController(text: widget.note.title);
    contentController = TextEditingController(text: widget.note.content);
    selectedColor = Color(widget.note.color);
    imagePath = widget.note.image;
  }

  Future<void> pickImage() async {
    final ImagePicker picker = ImagePicker();
    final XFile? pickedFile =
    await picker.pickImage(source: ImageSource.gallery);
    if (pickedFile != null) {
      setState(() {
        imagePath = pickedFile.path;
      });
    }
  }

  Future<void> saveNote() async {
    await DatabaseService.instance.updateNote(
      widget.note.id!,
      titleController.text,
      contentController.text,
      selectedColor.value,
      imagePath,
    );
    widget.onNoteUpdated(); // Notify home screen
    Navigator.pop(context);
  }

  Future<void> deleteNote() async {
    await DatabaseService.instance.deleteNoteById(widget.note.id!);
    widget.onNoteDeleted(); // Notify home screen
    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.note.title),
        actions: [
          IconButton(
              onPressed: () {
                showDialog(
                    context: context,
                    builder: (context) {
                      return AlertDialog(
                        title: const Text("Are you sure?"),
                        content:
                        Text("Note '${widget.note.title}' will be deleted"),
                        actions: [
                          TextButton(
                              onPressed: () {
                                Navigator.of(context).pop();
                              },
                              child: const Text("Cancel")),
                          TextButton(
                              onPressed: () {
                                Navigator.of(context).pop();
                                widget.onNoteDeleted();
                                Navigator.of(context).pop();
                              },
                              child: const Text("Delete")),
                        ],
                      );
                    });
              },
              icon: const Icon(Icons.delete))
        ],
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
                  border: InputBorder.none, hintText: "Note"),
            ),
            const SizedBox(height: 20),
            Row(
              children: [
                const Text('Pick a color: '),
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
                  child: Container(
                    width: 24,
                    height: 24,
                    color: selectedColor,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),
            Row(
              children: [
                if (imagePath != null)
                  Image.file(
                    File(imagePath!),
                    width: 100,
                    height: 100,
                  ),
                IconButton(
                  icon: const Icon(Icons.add_a_photo),
                  onPressed: pickImage,
                ),
              ],
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
          onPressed: () {
            widget.onNoteUpdated();
            Navigator.of(context).pop();
          },
          child: Icon(Icons.save)),
    );
  }
}