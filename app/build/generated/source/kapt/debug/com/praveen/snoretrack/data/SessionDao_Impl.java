package com.praveen.snoretrack.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SessionDao_Impl implements SessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Session> __insertionAdapterOfSession;

  private final EntityInsertionAdapter<SnoreEvent> __insertionAdapterOfSnoreEvent;

  private final EntityDeletionOrUpdateAdapter<Session> __deletionAdapterOfSession;

  private final EntityDeletionOrUpdateAdapter<Session> __updateAdapterOfSession;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldSessions;

  public SessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSession = new EntityInsertionAdapter<Session>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sessions` (`id`,`startTime`,`endTime`,`snoreScore`,`fullAudioPath`,`isSnippet`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        statement.bindLong(3, entity.getEndTime());
        statement.bindDouble(4, entity.getSnoreScore());
        if (entity.getFullAudioPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFullAudioPath());
        }
        final int _tmp = entity.isSnippet() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
    this.__insertionAdapterOfSnoreEvent = new EntityInsertionAdapter<SnoreEvent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `snore_events` (`id`,`sessionId`,`timestamp`,`amplitude`,`frequency`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SnoreEvent entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindDouble(4, entity.getAmplitude());
        statement.bindDouble(5, entity.getFrequency());
      }
    };
    this.__deletionAdapterOfSession = new EntityDeletionOrUpdateAdapter<Session>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sessions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSession = new EntityDeletionOrUpdateAdapter<Session>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sessions` SET `id` = ?,`startTime` = ?,`endTime` = ?,`snoreScore` = ?,`fullAudioPath` = ?,`isSnippet` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        statement.bindLong(3, entity.getEndTime());
        statement.bindDouble(4, entity.getSnoreScore());
        if (entity.getFullAudioPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFullAudioPath());
        }
        final int _tmp = entity.isSnippet() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteOldSessions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sessions WHERE startTime < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final Session session, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSession.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEvent(final SnoreEvent event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSnoreEvent.insert(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSession(final Session session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSession(final Session session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldSessions(final long threshold,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldSessions.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, threshold);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldSessions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Session>> getAllSessions() {
    final String _sql = "SELECT * FROM sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<List<Session>>() {
      @Override
      @NonNull
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfSnoreScore = CursorUtil.getColumnIndexOrThrow(_cursor, "snoreScore");
          final int _cursorIndexOfFullAudioPath = CursorUtil.getColumnIndexOrThrow(_cursor, "fullAudioPath");
          final int _cursorIndexOfIsSnippet = CursorUtil.getColumnIndexOrThrow(_cursor, "isSnippet");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Session _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            final float _tmpSnoreScore;
            _tmpSnoreScore = _cursor.getFloat(_cursorIndexOfSnoreScore);
            final String _tmpFullAudioPath;
            if (_cursor.isNull(_cursorIndexOfFullAudioPath)) {
              _tmpFullAudioPath = null;
            } else {
              _tmpFullAudioPath = _cursor.getString(_cursorIndexOfFullAudioPath);
            }
            final boolean _tmpIsSnippet;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSnippet);
            _tmpIsSnippet = _tmp != 0;
            _item = new Session(_tmpId,_tmpStartTime,_tmpEndTime,_tmpSnoreScore,_tmpFullAudioPath,_tmpIsSnippet);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllSessionsSuspend(final Continuation<? super List<Session>> $completion) {
    final String _sql = "SELECT * FROM sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Session>>() {
      @Override
      @NonNull
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfSnoreScore = CursorUtil.getColumnIndexOrThrow(_cursor, "snoreScore");
          final int _cursorIndexOfFullAudioPath = CursorUtil.getColumnIndexOrThrow(_cursor, "fullAudioPath");
          final int _cursorIndexOfIsSnippet = CursorUtil.getColumnIndexOrThrow(_cursor, "isSnippet");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Session _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            final float _tmpSnoreScore;
            _tmpSnoreScore = _cursor.getFloat(_cursorIndexOfSnoreScore);
            final String _tmpFullAudioPath;
            if (_cursor.isNull(_cursorIndexOfFullAudioPath)) {
              _tmpFullAudioPath = null;
            } else {
              _tmpFullAudioPath = _cursor.getString(_cursorIndexOfFullAudioPath);
            }
            final boolean _tmpIsSnippet;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSnippet);
            _tmpIsSnippet = _tmp != 0;
            _item = new Session(_tmpId,_tmpStartTime,_tmpEndTime,_tmpSnoreScore,_tmpFullAudioPath,_tmpIsSnippet);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SnoreEvent>> getEventsForSession(final long sessionId) {
    final String _sql = "SELECT * FROM snore_events WHERE sessionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"snore_events"}, new Callable<List<SnoreEvent>>() {
      @Override
      @NonNull
      public List<SnoreEvent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfAmplitude = CursorUtil.getColumnIndexOrThrow(_cursor, "amplitude");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final List<SnoreEvent> _result = new ArrayList<SnoreEvent>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SnoreEvent _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final double _tmpAmplitude;
            _tmpAmplitude = _cursor.getDouble(_cursorIndexOfAmplitude);
            final double _tmpFrequency;
            _tmpFrequency = _cursor.getDouble(_cursorIndexOfFrequency);
            _item = new SnoreEvent(_tmpId,_tmpSessionId,_tmpTimestamp,_tmpAmplitude,_tmpFrequency);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
