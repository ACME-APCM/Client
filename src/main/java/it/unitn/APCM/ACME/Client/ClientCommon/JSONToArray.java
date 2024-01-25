package it.unitn.APCM.ACME.Client.ClientCommon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class JSONToArray extends ArrayList<String> {

	public JSONToArray() {}

	//Used to convert JSON response in the clientResponse required
	public ClientResponse convertToClientResponse(String str) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(str, ClientResponse.class);
	}
}
