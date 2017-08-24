package uk.co.olimor.BMBTApi_boot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.Test;
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
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public User deserialiseToUser(final Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((User) mapper.convertValue(json, 
				new TypeReference<User>() {}));
	}

	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public ResultsAnalysis deserialiseToResultsAnalysis(final Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((ResultsAnalysis) mapper.convertValue(json, 
				new TypeReference<ResultsAnalysis>() {}));
	}

	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<Test> deserialiseJsonToTests(final List<Test> json)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((List<Test>) mapper.convertValue(json, 
				new TypeReference<List<Test>>() {}));
	}

}
