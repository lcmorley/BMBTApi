package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.DeviceExistsQuery;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Class which implements the {@link DeviceExistsQuery} interface to ascertain
 * if a device exists on the db.
 * 
 * @author leonmorley
 *
 */
@Log4j2
@Service
public class DeviceExistsQueryImpl extends AbstractQuery<Boolean> implements DeviceExistsQuery {

	/**
	 * Query to query devices.
	 */
	private static final String QUERY = "SELECT COUNT(*) FROM device WHERE deviceId = '%s'";

	@Override
	public boolean deviceExists(String deviceId) throws ApiException {
		log.entry(deviceId);

		try {
			return log.traceExit(query(String.format(QUERY, deviceId)).get(0));
		} catch (final ApiException e) {
			throw (e);
		}
	}

	@Override
	protected List<Boolean> buildResult(ResultSet result) throws SQLException {
		log.entry(result);

		final List<Boolean> results = new ArrayList<>();

		try {
			while (result.next())
				results.add(result.getInt(1) == 1);
		} catch (final SQLException e) {
			log.error("An exception occurred whilst attempting to fins a test.", e);
			throw e;
		}

		return log.traceExit(results);
	}

}
