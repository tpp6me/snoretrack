package com.praveen.snoretrack.service;

import com.praveen.snoretrack.data.SessionDao;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
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
public final class AudioRecorderService_MembersInjector implements MembersInjector<AudioRecorderService> {
  private final Provider<SessionDao> sessionDaoProvider;

  public AudioRecorderService_MembersInjector(Provider<SessionDao> sessionDaoProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
  }

  public static MembersInjector<AudioRecorderService> create(
      Provider<SessionDao> sessionDaoProvider) {
    return new AudioRecorderService_MembersInjector(sessionDaoProvider);
  }

  @Override
  public void injectMembers(AudioRecorderService instance) {
    injectSessionDao(instance, sessionDaoProvider.get());
  }

  @InjectedFieldSignature("com.praveen.snoretrack.service.AudioRecorderService.sessionDao")
  public static void injectSessionDao(AudioRecorderService instance, SessionDao sessionDao) {
    instance.sessionDao = sessionDao;
  }
}
