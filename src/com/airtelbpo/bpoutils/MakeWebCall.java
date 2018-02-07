package com.airtelbpo.bpoutils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MakeWebCall implements Runnable{
	private HttpURLConnection httpUrlConnection;
	private String URL_STRING = "https://gcm-http.googleapis.com/gcm/send";
	private String APP_SERVER_KEY = "AIzaSyA9nrpMjKK5XXSi_eEya7MSX3Uog95eaSE";
	private String deviceToken,numId,forwardedNum;
	private JSONObject jsonPayload,jsonData,jsonContent;
	private URL url;
	private HttpURLConnection httpURLConnection;
	public MakeWebCall(String deviceToken,String numId,String forwardedNum) {
		// TODO Auto-generated constructor stub
		this.numId = numId;
		this.forwardedNum = forwardedNum;
		this.deviceToken = deviceToken;
		jsonPayload = new JSONObject();
		jsonData = new JSONObject();
		jsonContent = new JSONObject();
		System.out.println("deviceToken: "+deviceToken+" num_Id: "+numId+" forwardedNum: "+forwardedNum);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("sending data to server initiated!");
		try {
			
			jsonContent.put("ID", numId);
			jsonContent.put("NUMBER", forwardedNum);
			jsonPayload.put("to", deviceToken);
			jsonData.put("message", jsonContent.toString());
			jsonPayload.put("data", jsonData);
	
			url = new URL(URL_STRING);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Content-type", MediaType.APPLICATION_JSON);
			httpURLConnection.setRequestProperty("Authorization","key="+APP_SERVER_KEY);
			httpURLConnection.connect();
			
			DataOutputStream output = null;
	        DataInputStream input = null;
	        output = new DataOutputStream(httpURLConnection.getOutputStream());
	        /*Construct the POST data.*/
	 
	            output.writeBytes(jsonPayload.toString());
	            output.flush();
	            output.close();
	             
	            /*Get response data.*/
	            
	            String response = null;
	            StringBuilder stringBuilder = new StringBuilder();
	            input = new DataInputStream (httpURLConnection.getInputStream());
	            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(input));
	            
	            while (null != ((response = responseBuffer.readLine()))) {
	            	stringBuilder.append(response);
	            }
	            System.out.println("Server Response : "+response);
	            input.close ();
		 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
