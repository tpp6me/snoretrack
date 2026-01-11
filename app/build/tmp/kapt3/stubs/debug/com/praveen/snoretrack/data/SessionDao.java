package com.praveen.snoretrack.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\fH\'J\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\rH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\r0\f2\u0006\u0010\u0012\u001a\u00020\u0005H\'J\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u0018"}, d2 = {"Lcom/praveen/snoretrack/data/SessionDao;", "", "deleteOldSessions", "", "threshold", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSession", "session", "Lcom/praveen/snoretrack/data/Session;", "(Lcom/praveen/snoretrack/data/Session;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllSessions", "Lkotlinx/coroutines/flow/Flow;", "", "getAllSessionsSuspend", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEventsForSession", "Lcom/praveen/snoretrack/data/SnoreEvent;", "sessionId", "insertEvent", "event", "(Lcom/praveen/snoretrack/data/SnoreEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSession", "updateSession", "app_debug"})
@androidx.room.Dao()
public abstract interface SessionDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertSession(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.Session session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSession(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.Session session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions ORDER BY startTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.praveen.snoretrack.data.Session>> getAllSessions();
    
    @androidx.room.Query(value = "SELECT * FROM sessions ORDER BY startTime DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllSessionsSuspend(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.praveen.snoretrack.data.Session>> $completion);
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEvent(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.SnoreEvent event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM snore_events WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.praveen.snoretrack.data.SnoreEvent>> getEventsForSession(long sessionId);
    
    @androidx.room.Query(value = "DELETE FROM sessions WHERE startTime < :threshold")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldSessions(long threshold, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSession(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.Session session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}