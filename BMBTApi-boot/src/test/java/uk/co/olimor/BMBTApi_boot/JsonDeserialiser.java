package uk.co.olimor.BMBTApi_boot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.model.Test;
import uk.co.olimor.BMBTApi_boot.model.TestAnalysis;
import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * Class which provides utility methods to map from Json strings to Object representation.
 * 
 * @author leonmorley
 *
 * @param <T>
 */
@Log4j2
public class JsonDeserialiser {

	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json - the object to deserialize.
	 *
	 * @return - the {@link User} object.
	 *
	 * @throws IOException - the exception thrown.
	 */
	public User deserialiseToUser(final Map<String, String> json)
			throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((User) mapper.convertValue(json, 
				new TypeReference<User>() {}));
	}

	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json - object to deserialize.
	 *
	 * @return - a {@link List} or {@link User} objects.
	 * 
	 * @throws IOException - an exception thrown.
	 */
	@SuppressWarnings("unchecked")
	public List<User> deserialiseToUsers(final List<User> json)
			throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((List<User>) mapper.convertValue(json, 
				new TypeReference<List<User>>() {}));
	}
	
	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json - the json to deserialize.
	 * 
	 * @return - the {@link ResultsAnalysis} object to return.
	 * 
	 * @throws IOException - an exception thrown.
	 */
	@SuppressWarnings("unchecked")
	public List<TestAnalysis> deserialiseToTestAnalysisList(final List<TestAnalysis> json)
			throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((List<TestAnalysis>) mapper.convertValue(json, 
				new TypeReference<List<TestAnalysis>>() {}));
	}

	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json - the json object to deserialize.
	 * 
	 * @return - the {@link List} of {@link Test} objects.
	 * 
	 * @throws IOException - the exception thrown.
	 */
	@SuppressWarnings("unchecked")
	public List<Test> deserialiseJsonToTests(final List<Test> json)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((List<Test>) mapper.convertValue(json, 
				new TypeReference<List<Test>>() {}));
	}

}
