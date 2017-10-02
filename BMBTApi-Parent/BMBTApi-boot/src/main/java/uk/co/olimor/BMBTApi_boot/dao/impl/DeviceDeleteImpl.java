package uk.co.olimor.BMBTApi_boot.dao.impl;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.DeviceDelete;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Service to remove a device and users and testresults from the db.
 * 
 * @author leonmorley
 *
 */
@Service
@Log4j2
public class DeviceDeleteImpl extends AbstractUpdate<String> implements DeviceDelete {

	/**
	 * Delete statement to remove from the device table and associated tables.
	 */
	private static final String DELETE_STATEMENT = "DELETE FROM device WHERE deviceId = '%s'";
	
	@Override
	public void deleteDevice(String deviceId) throws ApiException {
		log.entry(deviceId);
		update(deviceId);
		log.traceExit();
	}

	@Override
	protected String buildUpdate(String deviceId) {
		log.entry(deviceId);
		return log.traceExit(String.format(DELETE_STATEMENT, deviceId));
	}

}
