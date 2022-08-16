package tv.batman.digitoon.android.Session;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import tv.batman.digitoon.android.ApplicationLoader;
import tv.batman.digitoon.android.Models.VideoModel;
import tv.batman.digitoon.android.Utils.AppLog;

// created by morti

public class Database extends SQLiteOpenHelper {

  private static volatile Database Instance;

  public static synchronized Database getInstance() {
    Database localInstance = Instance;
    if (localInstance == null) {
      synchronized (Database.class) {
        localInstance = Instance;
        if (localInstance == null) {
          Instance = localInstance = new Database();
        }
      }
    }
    return localInstance;
  }

  // db setting
  private static final String DATABASE_NME = "database.db";
  private static final int DATABASE_VERSION = 1;

  // db constants
  private static final String T_VIDEOS = "t_videos";
  private static final String ID = "_id";
  private static final String IMDB_ID = "imdb_id";
  private static final String TITLE = "title";
  private static final String YEAR = "year";
  private static final String TYPE = "type";
  private static final String POSTER = "poster";
  private static final String RATED = "rated";
  private static final String RELEASED = "released";
  private static final String RUNTIME = "runtime";
  private static final String GENRE = "genre";
  private static final String DIRECTOR = "director";
  private static final String WRITER = "writer";
  private static final String ACTORS = "actors";
  private static final String PLOT = "plot";
  private static final String LANGUAGE = "language";
  private static final String COUNTRY = "country";
  private static final String AWARDS = "awards";
  private static final String RATINGS = "ratings";
  private static final String METASCORE = "metascore";
  private static final String IMDB_RATING = "imdb_rating";
  private static final String IMDB_VOTES = "imdb_votes";
  private static final String DVD = "dvd";
  private static final String TOTAL_SEASONS = "total_seasons";
  private static final String BOX_OFFICE = "box_office";
  private static final String PRODUCTION = "production";
  private static final String WEBSITE = "website";

  private Database() {
    super(ApplicationLoader.applicationContext, DATABASE_NME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String videosQuery = " CREATE TABLE " + T_VIDEOS + " ( " +
      ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      IMDB_ID + " TEXT, " +
      TITLE + " TEXT, " +
      YEAR + " TEXT, " +
      TYPE + " TEXT, " +
      POSTER + " TEXT, " +
      RATED + " TEXT, " +
      RELEASED + " TEXT, " +
      RUNTIME + " TEXT, " +
      GENRE + " TEXT, " +
      DIRECTOR + " TEXT, " +
      WRITER + " TEXT, " +
      ACTORS + " TEXT, " +
      PLOT + " TEXT, " +
      LANGUAGE + " TEXT, " +
      COUNTRY + " TEXT, " +
      AWARDS + " TEXT, " +
      RATINGS + " TEXT, " +
      METASCORE + " TEXT, " +
      IMDB_RATING + " TEXT, " +
      IMDB_VOTES + " TEXT, " +
      DVD + " TEXT, " +
      TOTAL_SEASONS + " INTEGER, " +
      BOX_OFFICE + " TEXT, " +
      PRODUCTION + " TEXT, " +
      WEBSITE + " TEXT );";
    db.execSQL(videosQuery);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public void updateVideo(VideoModel video) {
    try {
      SQLiteDatabase db = getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(IMDB_ID, video.imdb_id);
      values.put(TITLE, video.title);
      values.put(YEAR, video.year);
      values.put(TYPE, video.type);
      values.put(POSTER, video.poster);
      values.put(RATED, video.rated);
      values.put(RELEASED, video.released);
      values.put(RUNTIME, video.runtime);
      values.put(GENRE, video.genre);
      values.put(DIRECTOR, video.director);
      values.put(WRITER, video.writer);
      values.put(ACTORS, video.actors);
      values.put(PLOT, video.plot);
      values.put(LANGUAGE, video.language);
      values.put(COUNTRY, video.country);
      values.put(AWARDS, video.awards);
      values.put(RATINGS, video.ratings);
      values.put(METASCORE, video.metascore);
      values.put(IMDB_RATING, video.imdb_rating);
      values.put(IMDB_VOTES, video.imdb_votes);
      values.put(DVD, video.dvd);
      values.put(TOTAL_SEASONS, video.total_seasons);
      values.put(BOX_OFFICE, video.box_office);
      values.put(PRODUCTION, video.production);
      values.put(WEBSITE, video.website);
      db.update(T_VIDEOS, values, IMDB_ID + " = '" + video.imdb_id + "'" ,null);
      db.close();
    } catch (Exception e) {
      AppLog.e(Database.class, e);
    }
  }

  public void insertVideos(List<VideoModel> list) {
    try {
      SQLiteDatabase db = getWritableDatabase();
      db.execSQL("DELETE FROM " + T_VIDEOS);
      for (VideoModel video : list) {
        ContentValues values = new ContentValues();
        values.put(IMDB_ID, video.imdb_id);
        values.put(TITLE, video.title);
        values.put(YEAR, video.year);
        values.put(TYPE, video.type);
        values.put(POSTER, video.poster);
        db.insert(T_VIDEOS,null ,values);
      }
    } catch (Exception e) {
      AppLog.e(Database.class, e);
    }
  }

  @SuppressLint("Range")
  public List<VideoModel> getVideos() {
    List<VideoModel> list = new ArrayList<>();
    try {
      SQLiteDatabase db = getWritableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + T_VIDEOS, null);
      while(cursor.moveToNext()) {
        VideoModel video = new VideoModel();
        video.imdb_id = cursor.getString(cursor.getColumnIndex(IMDB_ID));
        video.title = cursor.getString(cursor.getColumnIndex(TITLE));
        video.year = cursor.getString(cursor.getColumnIndex(YEAR));
        video.type = cursor.getString(cursor.getColumnIndex(TYPE));
        video.poster = cursor.getString(cursor.getColumnIndex(POSTER));
        list.add(video);
      }
      cursor.close();
    } catch (Exception e) {
      AppLog.e(Database.class, e);
    }
    return list;
  }

  @SuppressLint("Range")
  public VideoModel getVideo(String imdb_id) {
    try {
      SQLiteDatabase db = getWritableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + T_VIDEOS + " WHERE " + IMDB_ID + " = '" + imdb_id + "'", null);
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        VideoModel video = new VideoModel();
        video.imdb_id = cursor.getString(cursor.getColumnIndex(IMDB_ID));
        video.title = cursor.getString(cursor.getColumnIndex(TITLE));
        video.year = cursor.getString(cursor.getColumnIndex(YEAR));
        video.type = cursor.getString(cursor.getColumnIndex(TYPE));
        video.poster = cursor.getString(cursor.getColumnIndex(POSTER));
        video.rated = cursor.getString(cursor.getColumnIndex(RATED));
        video.released = cursor.getString(cursor.getColumnIndex(RELEASED));
        video.runtime = cursor.getString(cursor.getColumnIndex(RUNTIME));
        video.genre = cursor.getString(cursor.getColumnIndex(GENRE));
        video.director = cursor.getString(cursor.getColumnIndex(DIRECTOR));
        video.writer = cursor.getString(cursor.getColumnIndex(WRITER));
        video.actors = cursor.getString(cursor.getColumnIndex(ACTORS));
        video.plot = cursor.getString(cursor.getColumnIndex(PLOT));
        video.language = cursor.getString(cursor.getColumnIndex(LANGUAGE));
        video.country = cursor.getString(cursor.getColumnIndex(COUNTRY));
        video.awards = cursor.getString(cursor.getColumnIndex(AWARDS));
        video.ratings = cursor.getString(cursor.getColumnIndex(RATINGS));
        video.metascore = cursor.getString(cursor.getColumnIndex(METASCORE));
        video.imdb_rating = cursor.getString(cursor.getColumnIndex(IMDB_RATING));
        video.imdb_votes = cursor.getString(cursor.getColumnIndex(IMDB_VOTES));
        video.dvd = cursor.getString(cursor.getColumnIndex(DVD));
        video.total_seasons = cursor.getInt(cursor.getColumnIndex(TOTAL_SEASONS));
        video.box_office = cursor.getString(cursor.getColumnIndex(BOX_OFFICE));
        video.production = cursor.getString(cursor.getColumnIndex(PRODUCTION));
        video.website = cursor.getString(cursor.getColumnIndex(WEBSITE));
        return video;
      }
      cursor.close();
    } catch (Exception e) {
      AppLog.e(Database.class, e);
    }
    return null;
  }
}