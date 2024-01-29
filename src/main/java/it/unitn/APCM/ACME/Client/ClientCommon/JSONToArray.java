package it.unitn.APCM.ACME.Client.ClientCommon;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The type Json to array.
 */
public class JSONToArray extends ArrayList<String> {

	/**
	 * Instantiates a new Json to array.
	 */
	public JSONToArray() {}

	/**
	 * Convert to client response the response.
	 *
	 * @param str the str recieved as response
	 * @return the client response
	 * @throws JsonProcessingException the json processing exception
	 */
	public ClientResponse convertToClientResponse(String str) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(str, ClientResponse.class);
	}
}
