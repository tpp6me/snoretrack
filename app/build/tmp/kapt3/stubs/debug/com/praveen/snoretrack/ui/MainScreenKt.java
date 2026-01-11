package com.praveen.snoretrack.ui;

import android.text.format.DateUtils;
import androidx.compose.animation.core.RepeatMode;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import com.praveen.snoretrack.data.Session;
import java.text.SimpleDateFormat;
import java.util.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0007\u001a4\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\u0012H\u0007\u001a\u001c\u0010\u0014\u001a\u00020\t2\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0007\"\u0011\u0010\u0000\u001a\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0003\"\u0011\u0010\u0004\u001a\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0003\"\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"StopIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "getStopIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "TrashIcon", "getTrashIcon", "_stopIcon", "_trashIcon", "MainScreen", "", "viewModel", "Lcom/praveen/snoretrack/ui/MainViewModel;", "SessionItem", "session", "Lcom/praveen/snoretrack/data/Session;", "isPlaying", "", "onPlayClick", "Lkotlin/Function0;", "onDeleteClick", "WaveformGraph", "modifier", "Landroidx/compose/ui/Modifier;", "app_debug"})
public final class MainScreenKt {
    @org.jetbrains.annotations.Nullable()
    private static androidx.compose.ui.graphics.vector.ImageVector _stopIcon;
    @org.jetbrains.annotations.Nullable()
    private static androidx.compose.ui.graphics.vector.ImageVector _trashIcon;
    
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.ui.graphics.vector.ImageVector getStopIcon() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.ui.graphics.vector.ImageVector getTrashIcon() {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void MainScreen(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.ui.MainViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SessionItem(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.Session session, boolean isPlaying, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPlayClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void WaveformGraph(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.ui.MainViewModel viewModel) {
    }
}