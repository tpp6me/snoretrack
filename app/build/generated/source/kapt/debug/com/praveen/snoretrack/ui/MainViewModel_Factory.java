package com.praveen.snoretrack.ui;

import android.content.Context;
import com.praveen.snoretrack.data.SessionDao;
import com.praveen.snoretrack.domain.AudioPlayer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<SessionDao> sessionDaoProvider;

  private final Provider<AudioPlayer> audioPlayerProvider;

  private final Provider<Context> contextProvider;

  public MainViewModel_Factory(Provider<SessionDao> sessionDaoProvider,
      Provider<AudioPlayer> audioPlayerProvider, Provider<Context> contextProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
    this.audioPlayerProvider = audioPlayerProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(sessionDaoProvider.get(), audioPlayerProvider.get(), contextProvider.get());
  }

  public static MainViewModel_Factory create(Provider<SessionDao> sessionDaoProvider,
      Provider<AudioPlayer> audioPlayerProvider, Provider<Context> contextProvider) {
    return new MainViewModel_Factory(sessionDaoProvider, audioPlayerProvider, contextProvider);
  }

  public static MainViewModel newInstance(SessionDao sessionDao, AudioPlayer audioPlayer,
      Context context) {
    return new MainViewModel(sessionDao, audioPlayer, context);
  }
}
