package com.praveen.snoretrack.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class AudioPlayer_Factory implements Factory<AudioPlayer> {
  @Override
  public AudioPlayer get() {
    return newInstance();
  }

  public static AudioPlayer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AudioPlayer newInstance() {
    return new AudioPlayer();
  }

  private static final class InstanceHolder {
    private static final AudioPlayer_Factory INSTANCE = new AudioPlayer_Factory();
  }
}
