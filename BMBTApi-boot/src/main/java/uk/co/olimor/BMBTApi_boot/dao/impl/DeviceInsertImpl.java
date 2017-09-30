package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.DeviceInsert;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;

@Log4j2
@Service
public class DeviceInsertImpl extends AbstractUpdate<String> implements DeviceInsert {

	/**
	 * Insert statement with placeholders.
	 */
	private static final String INSERT_STATEMENT = "INSERT INTO device VALUES ('%s', '%s')";

	@Override
	public int insertDevice(final String deviceId) throws ApiException {
		log.entry(deviceId);		
		return log.traceExit(update(deviceId));
	}

	@Override
	protected String buildUpdate(String deviceId) {
		log.entry(deviceId);
		
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final String currentTime = format.format(new Date());
		
		return log.traceExit(String.format(INSERT_STATEMENT, deviceId, currentTime));
	}

}
