package uk.co.olimor.BMBTApi_Common.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the {@link TestAnalysis} class.
 * 
 * @author leonmorley
 *
 */
public class TestAnalysysTest {

	@Test
	public void testConstructor() {
		
		final TestAnalysis analysis = new TestAnalysis(1);
		Assert.assertEquals(1, analysis.getTestId());
		
	}
	
}
