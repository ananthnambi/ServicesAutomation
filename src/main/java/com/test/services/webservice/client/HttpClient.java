package com.test.services.webservice.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.test.services.model.service.Response;
import com.test.services.util.ServicesConstants;

/**
 * @author ananthnambi@gmail.com
 *
 *         This class send the given HTTP request to the given endpoint.
 *
 */
@Component
public class HttpClient {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

	/**
	 * This method is called by service layer to send the required message as a
	 * HTTP request to the given endpoint.
	 * 
	 * @param source
	 *            - The endpoint url.
	 * @param requestMethod
	 *            - HTTP request method to use.
	 * @param contentType
	 *            - Type of content being sent.
	 * @param returnType
	 *            - Return type expected.
	 * @param body
	 *            - Body of the request.
	 * @param authorizationKey
	 *            - Authorization key to be set to the header.
	 * @param secure
	 *            - Whether the request is to be secure.
	 * @return response String
	 * @throws Exception
	 */
	public Response send(String source, String requestMethod, String contentType, String returnType, String body,
			String authorizationKey, boolean secure) throws Exception {

		LOGGER.info("HTTP request for : " + source + " and " + body);

		Response result = null;

		HttpURLConnection con = null;

		try {

			URL url = new URL(source);

			if (secure) {

				con = (HttpsURLConnection) url.openConnection();

			} else {

				con = (HttpURLConnection) url.openConnection();

			}

			// TODO - ideal connection timeout
			con.setConnectTimeout(60000);

			if (returnType != null) {

				con.setRequestProperty(ServicesConstants.HTTP_REQUEST_ACCEPT, returnType);

			}

			if (authorizationKey != null) {

				con.setRequestProperty(ServicesConstants.HTTP_REQUEST_AUTHORIZATION, authorizationKey);

			}

			switch (requestMethod) {

			case "GET":

				break;

			case "POST": {

				con.setDoOutput(true);
				con.setRequestMethod(ServicesConstants.HTTP_METHOD_POST);

				if (contentType != null) {

					con.setRequestProperty(ServicesConstants.HTTP_REQUEST_CONTENT_TYPE, contentType);

				}

				OutputStream os = con.getOutputStream();
				os.write(body.getBytes());
				os.flush();

				if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED
						&& con.getResponseCode() != HttpURLConnection.HTTP_OK) {

					System.out.println(con.getContentType());

					result = Response.builder().request(body).responseCode(con.getResponseCode())
							.response(IOUtils.toString(con.getErrorStream(), ServicesConstants.UTF8)).build();

					return result;

				}

				break;

			}

			case "PUT": {

				con.setDoOutput(true);
				con.setRequestMethod(ServicesConstants.HTTP_METHOD_PUT);

				if (contentType != null) {

					con.setRequestProperty(ServicesConstants.HTTP_REQUEST_CONTENT_TYPE, contentType);

				}

				OutputStream os = con.getOutputStream();
				os.write(body.getBytes());
				os.flush();

				if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED
						&& con.getResponseCode() != HttpURLConnection.HTTP_OK) {

					result = Response.builder().request(body).responseCode(con.getResponseCode())
							.response(IOUtils.toString(con.getErrorStream(), ServicesConstants.UTF8)).build();

					return result;

				}

				break;

			}

			default:

				throw new RuntimeException("Invalid Request method !");

			}

			result = Response.builder().request((body == null) ? source : body).responseCode(con.getResponseCode())
					.response(IOUtils.toString(con.getInputStream(), ServicesConstants.UTF8)).build();

		} catch (IOException ex) {

			if (con != null) {

				result = Response.builder().request((body == null) ? source : body).responseCode(con.getResponseCode())
						.response((con.getErrorStream() != null)
								? IOUtils.toString(con.getErrorStream(), ServicesConstants.UTF8)
								: con.getResponseMessage())
						.build();

			}

		}

		return result;

	}

}