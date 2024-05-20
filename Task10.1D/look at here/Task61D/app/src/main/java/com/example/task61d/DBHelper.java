package com.example.task61d;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //table users
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_NAME = "User";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    //table user interests
    private static final String TABLE_USER_INTERESTS = "user_interests";
    private static final String COLUMN_USER_INTEREST_USERNAME = "username";
    private static final String COLUMN_USER_INTEREST_INTEREST_NAME = "interest_name";

    // table user_quiz_content
    private static final String TABLE_USER_QUIZ_CONTENT = "user_quiz_content";
    private static final String COLUMN_USER_QUIZ_USER_ID = "user_id";
    private static final String COLUMN_USER_QUIZ_QUESTION = "question";
    private static final String COLUMN_USER_QUIZ_ANSWER_A = "answer_a";
    private static final String COLUMN_USER_QUIZ_ANSWER_B = "answer_b";
    private static final String COLUMN_USER_QUIZ_ANSWER_C = "answer_c";
    private static final String COLUMN_USER_QUIZ_ANSWER_D = "answer_d";
    private static final String COLUMN_USER_QUIZ_USER_ANSWER = "user_answer";
    private static final String COLUMN_USER_QUIZ_CORRECT_ANSWER = "correct_answer";

    private static final String COLUMN_USER_QUIZ_TIME = "time";
    private static final String COLUMN_USER_QUIZ_TOPIC = "topic";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PASSWORD + " TEXT)";

    private static final String createUserInterestsTableQuery = "CREATE TABLE " + TABLE_USER_INTERESTS + " (" +
            COLUMN_USER_INTEREST_USERNAME + " TEXT," +
            COLUMN_USER_INTEREST_INTEREST_NAME + " TEXT," +
            "PRIMARY KEY(" + COLUMN_USER_INTEREST_USERNAME + ", " + COLUMN_USER_INTEREST_INTEREST_NAME + ")," +
            "FOREIGN KEY(" + COLUMN_USER_INTEREST_USERNAME + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_USERNAME + "))";

    private static final String CREATE_USER_QUIZ_CONTENT_TABLE = "CREATE TABLE " + TABLE_USER_QUIZ_CONTENT + " (" +
            COLUMN_USER_QUIZ_USER_ID + " INTEGER, " +
            COLUMN_USER_QUIZ_QUESTION + " TEXT, " +
            COLUMN_USER_QUIZ_ANSWER_A + " TEXT, " +
            COLUMN_USER_QUIZ_ANSWER_B + " TEXT, " +
            COLUMN_USER_QUIZ_ANSWER_C + " TEXT, " +
            COLUMN_USER_QUIZ_ANSWER_D + " TEXT, " +
            COLUMN_USER_QUIZ_USER_ANSWER + " TEXT, " +
            COLUMN_USER_QUIZ_CORRECT_ANSWER + " TEXT, " +
            COLUMN_USER_QUIZ_TIME + " TEXT, " +
            COLUMN_USER_QUIZ_TOPIC + " TEXT, " +
            "PRIMARY KEY(" +COLUMN_USER_QUIZ_TIME + ")," +
            "FOREIGN KEY(" + COLUMN_USER_QUIZ_USER_ID + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + "))";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(createUserInterestsTableQuery);
        db.execSQL(CREATE_USER_QUIZ_CONTENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_QUIZ_CONTENT);
        onCreate(db);
    }

    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        long userId = db.insert(TABLE_NAME, null, values);
        db.close();
        return userId != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public void insertUserInterest(String username, String interestName) {
        DBHelper dbHelper = this;
        if (!dbHelper.isInterestExist(username, interestName)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_INTEREST_USERNAME, username);
            values.put(COLUMN_USER_INTEREST_INTEREST_NAME, interestName);
            db.insert(TABLE_USER_INTERESTS, null, values);
            db.close();
        }
    }

    public List<String> getUserInterests(String username) {
        List<String> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_INTERESTS, new String[]{COLUMN_USER_INTEREST_INTEREST_NAME},
                COLUMN_USER_INTEREST_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String interestName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_INTEREST_INTEREST_NAME));
                interests.add(interestName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return interests;
    }

    public boolean isInterestExist(String username, String interestName) {
        String query = "SELECT 1 FROM user_interests WHERE username = ? AND interest_name = ? LIMIT 1";
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{username, interestName});
        boolean exist = cursor.moveToFirst();
        cursor.close();
        return exist;
    }

    @SuppressLint("Range")
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }

    public void insertQuizContent(String username, String question, List<String> options, String userAnswer, String correctAnswer, String time, String topic) {
        if (options.size() != 4) {
            throw new IllegalArgumentException("Options list must contain exactly 4 items.");
        }
        try {
            int userId = getUserIdByUsername(username);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_QUIZ_USER_ID, userId);
            values.put(COLUMN_USER_QUIZ_QUESTION, question);
            values.put(COLUMN_USER_QUIZ_ANSWER_A, options.get(0));
            values.put(COLUMN_USER_QUIZ_ANSWER_B, options.get(1));
            values.put(COLUMN_USER_QUIZ_ANSWER_C, options.get(2));
            values.put(COLUMN_USER_QUIZ_ANSWER_D, options.get(3));
            values.put(COLUMN_USER_QUIZ_USER_ANSWER, userAnswer);
            values.put(COLUMN_USER_QUIZ_CORRECT_ANSWER, correctAnswer);
            values.put(COLUMN_USER_QUIZ_TIME, time);
            values.put(COLUMN_USER_QUIZ_TOPIC, topic);
            long result = db.insert(TABLE_USER_QUIZ_CONTENT, null, values);
            db.close();
        }
        catch (Exception exception){
            throw exception;
        }
    }
//        try {
////          db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            int userId = getUserIdByUsername(username);
//            values.put(COLUMN_USER_QUIZ_USER_ID, userId);
//            values.put(COLUMN_USER_QUIZ_QUESTION, question);
//            values.put(COLUMN_USER_QUIZ_ANSWER_A, options.get(0));
//            values.put(COLUMN_USER_QUIZ_ANSWER_B, options.get(1));
//            values.put(COLUMN_USER_QUIZ_ANSWER_C, options.get(2));
//            values.put(COLUMN_USER_QUIZ_ANSWER_D, options.get(3));
//            values.put(COLUMN_USER_QUIZ_USER_ANSWER, userAnswer);
//            values.put(COLUMN_USER_QUIZ_CORRECT_ANSWER, correctAnswer);
//            values.put(COLUMN_USER_QUIZ_TIME, time);
//            values.put(COLUMN_USER_QUIZ_TOPIC, topic);
//            long result = db.insert(TABLE_USER_QUIZ_CONTENT, null, values);
//            Log.d(String.format("Value of Result: %s"), String.valueOf(result));
//            if (result == -1) {
//                System.exit(1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (db != null && db.isOpen()) {
//               db.close();
//            }
//        }
//    }


    public List<Question> getUserALLContents(String username) {
        List<Question> quizContents = new ArrayList<>();
        int userId = getUserIdByUsername(username);
        if (userId != -1) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USER_QUIZ_CONTENT,
                    new String[]{COLUMN_USER_QUIZ_QUESTION, COLUMN_USER_QUIZ_ANSWER_A, COLUMN_USER_QUIZ_ANSWER_B, COLUMN_USER_QUIZ_ANSWER_C, COLUMN_USER_QUIZ_ANSWER_D, COLUMN_USER_QUIZ_USER_ANSWER, COLUMN_USER_QUIZ_CORRECT_ANSWER, COLUMN_USER_QUIZ_TIME, COLUMN_USER_QUIZ_TOPIC},
                    COLUMN_USER_QUIZ_USER_ID + "=?",
                    new String[]{String.valueOf(userId)}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_QUESTION));
                    @SuppressLint("Range") String answerA = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_A));
                    @SuppressLint("Range") String answerB = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_B));
                    @SuppressLint("Range") String answerC = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_C));
                    @SuppressLint("Range") String answerD = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_D));
                    @SuppressLint("Range") String userAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_USER_ANSWER));
                    @SuppressLint("Range") String correctAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_CORRECT_ANSWER));
                    @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_TIME));
                    @SuppressLint("Range") String topic = cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_TOPIC));

                    List<String> options = new ArrayList<>();
                    options.add(answerA);
                    options.add(answerB);
                    options.add(answerC);
                    options.add(answerD);

                    Question quizContent = new Question(question, options, correctAnswer);
                    quizContent.setUser_answer(userAnswer);
                    quizContent.setTime(time);
                    quizContent.setTopic(topic);
                    quizContents.add(quizContent);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } else {
            // 用户名不存在的情况下处理逻辑
            System.out.println("用户名不存在");
        }
        return quizContents;
    }


    @SuppressLint("Range")
    public List<Question> getCorrectQuestionsByUserId(String username) {
        List<Question> correctQuestions = new ArrayList<>();
        int userId = getUserIdByUsername(username);
        if (userId != -1) {
            String selectQuery = "SELECT * FROM " + TABLE_USER_QUIZ_CONTENT + " WHERE " + COLUMN_USER_QUIZ_USER_ID + " = ?";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                do {
                    // 将数据库中的每一行数据转换为 Question 对象
                    Question question = new Question();
                    question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_QUESTION)));
                    List<String> options = new ArrayList<>();
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_A)));
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_B)));
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_C)));
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_D)));
                    question.setOptions(options);
                    question.setUser_answer(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_USER_ANSWER)));
                    question.setCorrect_answer(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_CORRECT_ANSWER)));
                    question.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_TIME)));
                    question.setTopic(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_TOPIC)));

                    // Compare user_answer and correct_answer in code
                    if (question.getUserAnswer().equals(question.getCorrectAnswer())) {
                        correctQuestions.add(question);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return correctQuestions;
    }

    @SuppressLint("Range")
    public List<Question> getIncorrectQuestionsByUserId(String username) {
        List<Question> incorrectQuestions = new ArrayList<>();
        int userId = getUserIdByUsername(username);
        if (userId != -1) {
            String selectQuery = "SELECT * FROM " + TABLE_USER_QUIZ_CONTENT + " WHERE " + COLUMN_USER_QUIZ_USER_ID + " = ?";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                do {
                    // 将数据库中的每一行数据转换为 Question 对象
                    Question question = new Question();
                    question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_QUESTION)));
                    List<String> options = new ArrayList<>();
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_A)));
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_B)));
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_C)));
                    options.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_ANSWER_D)));
                    question.setOptions(options);
                    question.setUser_answer(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_USER_ANSWER)));
                    question.setCorrect_answer(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_CORRECT_ANSWER)));
                    question.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_TIME)));
                    question.setTopic(cursor.getString(cursor.getColumnIndex(COLUMN_USER_QUIZ_TOPIC)));

                    // Compare user_answer and correct_answer in code
                    if (!question.getUserAnswer().equals(question.getCorrectAnswer())) {
                        incorrectQuestions.add(question);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return incorrectQuestions;
    }

    @SuppressLint("Range")
    public String getUserEmailById(String username) {
        String email = null;
        int userId = getUserIdByUsername(username);
        if (userId != -1) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_EMAIL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            }
            cursor.close();
            db.close();
        }
        return email;
    }

}