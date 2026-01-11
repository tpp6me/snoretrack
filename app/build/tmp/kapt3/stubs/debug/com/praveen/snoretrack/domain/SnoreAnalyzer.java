package com.praveen.snoretrack.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0017\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/praveen/snoretrack/domain/SnoreAnalyzer;", "", "()V", "AMPLITUDE_THRESHOLD", "", "FREQUENCY_THRESHOLD", "analyze", "Lcom/praveen/snoretrack/domain/AnalysisResult;", "buffer", "", "sampleRate", "", "calculateRMS", "calculateZeroCrossingRate", "app_debug"})
public final class SnoreAnalyzer {
    private final double AMPLITUDE_THRESHOLD = 500.0;
    private final double FREQUENCY_THRESHOLD = 600.0;
    
    public SnoreAnalyzer() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.praveen.snoretrack.domain.AnalysisResult analyze(@org.jetbrains.annotations.NotNull()
    short[] buffer, int sampleRate) {
        return null;
    }
    
    private final double calculateRMS(short[] buffer) {
        return 0.0;
    }
    
    private final double calculateZeroCrossingRate(short[] buffer, int sampleRate) {
        return 0.0;
    }
}