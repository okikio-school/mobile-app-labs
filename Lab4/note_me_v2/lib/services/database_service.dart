

import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class DatabaseService {
  static final DatabaseService _instance = DatabaseService._constructor();
  static Database? _db;

  DatabaseService._constructor();

  static DatabaseService get instance => _instance;

  final String _notesTableName = "notes";
  final String _notesId = "ID";
  final String _notesTitle = "TITLE";
  final String _notesContent = "CONTENT";
  final String _notesColor = "COLOR";

  Future<Database> get database async {
    if (_db != null) {
      return _db!;
    }
    _db = await getDatabase();
    return _db!;
  }

  Future<Database> getDatabase() async {
    final databaseDirPath = await getDatabasesPath();
    final databasePath = join(databaseDirPath, "notes_db.db");
    final database = await openDatabase(
      databasePath,
      version: 2,
      onCreate: (db, version) {
        final String createTable = '''
            CREATE TABLE $_notesTableName (
              $_notesId INTEGER PRIMARY KEY AUTOINCREMENT,
              $_notesTitle TEXT,
              $_notesContent TEXT,
              $_notesColor INTEGER,
              IMAGE TEXT
            )
          ''';
        db.execute(createTable);
      },
      onUpgrade: (db, oldVersion, newVersion) {
        if (oldVersion < 2) {
          db.execute("ALTER TABLE notes ADD COLUMN IMAGE TEXT");
        }
      },
    );
    return database;
  }

  Future<void> addNote(
      String title,
      String content,
      int color,
      String? imagePath,
      ) async {
    if (title.isEmpty && content.isEmpty) {
      throw Exception("Note cannot be saved without title or content.");
    }

    final db = await database;
    await db.insert(_notesTableName, {
      _notesTitle: title,
      _notesContent: content,
      _notesColor: color,
      'IMAGE': imagePath
    });
  }

  Future<void> updateNote(int id, String title, String content, int color,
      String? imagePath) async {
    final db = await database;
    await db.update(
      _notesTableName,
      {
        _notesTitle: title,
        _notesContent: content,
        _notesColor: color,
        'IMAGE': imagePath,
      },
      where: '$_notesId = ?',
      whereArgs: [id],
    );
  }

  Future<List<Map<String, dynamic>>> fetchAllNotes() async {
    final db = await database;
    return await db.query(_notesTableName);
  }

  Future<List<Map<String, dynamic>>> searchNotes(String query) async {
    final db = await database;
    return await db.query(
      _notesTableName,
      where: "$_notesTitle LIKE ? OR $_notesContent LIKE ?",
      whereArgs: ['%$query%', '%$query%'],
    );
  }

  Future<void> deleteNoteById(int? id) async {
    if (id == null) return; // Do nothing if id is null
    final db = await database;
    await db.delete(
      _notesTableName,
      where: '$_notesId = ?',
      whereArgs: [id],
    );
  }
}
