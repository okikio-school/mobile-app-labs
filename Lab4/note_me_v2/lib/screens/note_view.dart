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
  final Function(Note) onNoteUpdated;
  final Function(Note) onNoteDeleted;

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
                                DatabaseService.instance
                                    .deleteNoteById(widget.note.id!);
                                widget.onNoteDeleted(widget.note);
                                if (Navigator.of(context).canPop()) {
                                  Navigator.of(context).pop();
                                }
                              },
                              child: const Text("Delete")),
                        ],
                      );
                    });
              },
              icon: const Icon(Icons.delete))
        ],
      ),
      body: Container(
        color: selectedColor,
        child: Padding(
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
                minLines: 5,
                maxLines: null,
                keyboardType: TextInputType.multiline,
                style: const TextStyle(fontSize: 18),
                decoration: const InputDecoration(
                    border: InputBorder.none, hintText: "Note"),
              ),
              const SizedBox(height: 20),
              Row(
                children: [
                  if (imagePath != null)
                    Image.file(
                      File(imagePath!),
                      width: 200,
                      height: 200,
                      fit: BoxFit.cover,
                    ),
                ],
              ),
            ],
          ),
        ),
      ),
      persistentFooterButtons: [
        Row(
          children: [
            Row(
              children: [
                const Icon(Icons.colorize),
                GestureDetector(
                  onTap: () async {
                    Color? color = await showDialog(
                      context: context,
                      builder: (context) => AlertDialog(
                        title: const Text('Select a Color'),
                        content: SingleChildScrollView(
                          child: Wrap(
                            spacing: 10,
                            runSpacing: 10,
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
                              Colors.blueGrey,
                            ]
                                .map((color) => GestureDetector(
                              onTap: () =>
                                  Navigator.pop(context, color),
                              child: Container(
                                width: 25,
                                height: 25,
                                decoration: BoxDecoration(
                                  color: color,
                                  border: Border.all(),
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
                    width: 25,
                    height: 25,
                    decoration: BoxDecoration(
                        color: selectedColor,
                        border: Border.all(
                          color: Colors.white,
                          width: 2,
                        ),
                        borderRadius: BorderRadius.circular(50)),
                  ),
                ),
              ],
            ),
            IconButton(
              icon: const Icon(Icons.add_a_photo),
              onPressed: pickImage,
            ),
          ],
        )
      ],
      floatingActionButton: FloatingActionButton(
          onPressed: () {
            // Check if the title or content is empty
            if (titleController.text.isEmpty &&
                contentController.text.isEmpty) {
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
              image: imagePath,
            );

            // Save the note to the database and await the operation
            DatabaseService.instance.updateNote(
              widget.note.id!,
              titleController.text,
              contentController.text,
              selectedColor.value,
              imagePath,
            );

            // Notify HomeScreen and close the NewNotesScreen
            widget.onNoteUpdated(note);
            if (Navigator.of(context).canPop()) {
              Navigator.of(context).pop();
            }
          },
          child: Icon(Icons.save)),
    );
  }
}
