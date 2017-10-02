package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.DeviceSecurityUpdate;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.DatabaseToken;

@Log4j2
@Service
public class DeviceSecurityUpdateImpl extends AbstractUpdate<DatabaseToken>
		implements DeviceSecurityUpdate {

	private static final String UPDATE_STATEMENT = "UPDATE deviceSecurity SET token = '%s', expiryDate = '%s' "
			+ "WHERE deviceId = '%s'";
	
	@Override
	public int updateDeviceSecurity(final DatabaseToken token) throws ApiException {
		log.entry(token);
		return log.traceExit(update(token));
	}

	@Override
	protected String buildUpdate(final DatabaseToken token) {
		log.entry(token);
		
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_WEEK, 1);
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final String currentTime = format.format(cal.getTime());
				
		return log.traceExit(String.format(UPDATE_STATEMENT, token.getToken(), currentTime, token.getDeviceId()));
	}

}
