package org.eclipse.test.performance;

public class Performance {
public static final Object EXPLAINS_DEGRADATION_COMMENT = new Object();

public static Performance getDefault() {
	return null;
}

public PerformanceMeter createPerformanceMeter(String scenarioId) {
	return null;
}

public void tagAsSummary(PerformanceMeter meter, String id, Object elapsedProcess) {
}

public void assertPerformance(PerformanceMeter meter) {
}

public void tagAsGlobalSummary(PerformanceMeter meter, String createCompositesAndWidgets, Object elapsedProcess) {
}

public void setComment(PerformanceMeter meter, Object comment, String s) {
}
}
