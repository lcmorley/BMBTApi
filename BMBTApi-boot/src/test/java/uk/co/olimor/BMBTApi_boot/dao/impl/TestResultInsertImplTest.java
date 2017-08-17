package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.reflection.Whitebox;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.model.TestResult;

/**
 * Test class for the {@link TestResultInsertImpl} class.
 * 
 * @author leonmorley
 *
 */
@Log4j2
public class TestResultInsertImplTest {
	
	@Test
	public void testSaveTestResult() throws Exception {
		log.traceEntry();
		
		final TestResult result = new TestResult(1, 1, 4, 4, 3, 10.5f);
		
		final DataSource mockDataSource = mock(MysqlDataSource.class);
		final Connection mockConnection = mock(Connection.class);
		final Statement mockStatement = mock(Statement.class);
		
		when(mockStatement.executeUpdate(anyString())).thenReturn(1);
		when(mockConnection.createStatement()).thenReturn(mockStatement);
		when(mockDataSource.getConnection()).thenReturn(mockConnection);
		
		final TestResultInsertImpl instance = new TestResultInsertImpl();
		
		Whitebox.setInternalState(instance, "datasource", mockDataSource);
		
		assertEquals(1, instance.saveTestResult(result));
		
		log.traceExit();
	}
	
}
