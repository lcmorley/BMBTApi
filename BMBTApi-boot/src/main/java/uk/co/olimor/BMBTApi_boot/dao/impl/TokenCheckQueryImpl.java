package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.TokenCheckQuery;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.DatabaseToken;

@Log4j2
@Service
public class TokenCheckQueryImpl extends AbstractQuery<DatabaseToken> implements TokenCheckQuery {

	private static final String QUERY = "SELECT * FROM deviceSecurity WHERE deviceId = '%s'";
	
	@Override
	public DatabaseToken getTokenFromDeviceId(String deviceId) throws ApiException {
		log.traceEntry(deviceId);
		
		final List<DatabaseToken> tokens = query(String.format(QUERY, deviceId));
		
		if (tokens.size() == 0) {
			log.error("Token not found on the database.");
			throw new ApiException();
		}
					
		return log.traceExit(tokens.get(0));		
	}

	@Override
	protected List<DatabaseToken> buildResult(ResultSet result) throws Exception {
		log.entry(result);

		final List<DatabaseToken> results = new ArrayList<>();

		try {
			while (result.next()) {
				final DatabaseToken dbToken = new DatabaseToken();
				dbToken.setDeviceId(result.getString(1));
				dbToken.setToken(result.getString(2));
				
				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
				dbToken.setExpiryDate(format.parse(result.getString(3)));
				
				results.add(dbToken);
			}
		} catch (final SQLException | ParseException e) {
			log.error("An exception occurred whilst attempting to retrieve the deviceSecurity record.", e);
			throw e;
		}

		return log.traceExit(results);
	}

}
