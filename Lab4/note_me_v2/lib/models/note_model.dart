class Note {
  final int? id;
  final String title;
  final String content;
  final int color;
  final String? image;

  Note({
    this.id,
    required this.title,
    required this.content,
    required this.color,
    this.image,
  });

  // fromMap method to convert a map to a Note object
  factory Note.fromMap(Map<String, dynamic> map) {
    return Note(
      id: map['ID'] as int?,
      title: map['TITLE'] as String,
      content: map['CONTENT'] as String,
      color: map['COLOR'] as int,
      image: map['IMAGE'] as String?,
    );
  }
}
