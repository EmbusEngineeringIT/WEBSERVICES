package com.airtelbpo.bposervice;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.airtelbpo.bpoutils.AirtelStatusCodes;
import com.airtelbpo.bpoutils.BpoUtils;
import com.airtelbpo.bpoutils.CsvFileReader;
import com.airtelbpo.pojo.AirtelNumberData;
import com.airtelbpo.pojo.CallRemainderData;
import com.airtelbpo.pojo.UserDataForCallTransfer;

@Path("/airtelbposervices")
public class AirtelServices {
		
	private JSONObject finalResponseObject;

	public AirtelServices() {
		
		//String fileName = System.getProperty("user.home")+"/number_dump.csv";
		
		//o.println("\nRead CSV file:"+"file_name_"+fileName);
		
		//System.getProperty("user.home") + File.separatorChar + "My Documents"
		//CsvFileReader.readCsvFile(fileName);
		
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/chkLogin")
	public Response checkLogin(@FormParam("USER_EMAIL") String userEmail,@FormParam("USER_PASSWORD") String userPassword) {		
		
		//System.out.print("post"+userEmail);
		String response = null;
		JSONObject jsonObjectResponse = null;  	
		//JSONArray jsonArrayNumberDump = new JSONArray();
		JSONObject jsonObjectFinalResponse =new JSONObject();	
		java.sql.Connection con=null;
		String errorMessage=null;
		JSONObject jsonObjectErrorResponse = null;
		//JSONObject jsonObject = JSONString.getJSON(incomingData);
		try {
			//logger.debug("check.login");
			jsonObjectResponse = new JSONObject();
				int isemail = BpoUtils.checkEmail(userEmail);
				//System.out.println("tryblock");
				jsonObjectErrorResponse = new JSONObject();
				//isvalid = com.tband_connectins.util.AppMethods.isValid(TEmail_ID);
				/*if (!isvalid) {
					jsonObjectErrorResponse.put("invalid email");
					com.tband_connectins.util.StatusCode.STATUS = 	com.tband_connectins.util.StatusCode.BADREQUEST_CODE;
					com.tband_connectins.util.StatusCode.MESSAGE = "fail";
					com.tband_connectins.util.StatusCode.SUCCESSMESSAGE = "invalid email id";
				}
	*/		
				if (isemail == 0) {
					
					jsonObjectErrorResponse.put("errorMessage","invalid email id");
					//System.out.println("Plese enter correct emailid");
					AirtelStatusCodes.STATUS = 	AirtelStatusCodes.BADREQUEST_CODE;
					AirtelStatusCodes.MESSAGE = "fail";
					AirtelStatusCodes.ERRORMESSAGE = "invalid email id";
					errorMessage="invalid email id";
				
				}
				boolean pwd = BpoUtils.checkUserPassword(userPassword, userEmail);
				if (!pwd && isemail == 1) {
					jsonObjectErrorResponse.put("errorMessage","invalid password");
					AirtelStatusCodes.STATUS = 	AirtelStatusCodes.BADREQUEST_CODE;
					AirtelStatusCodes.MESSAGE = "fail";
					AirtelStatusCodes.ERRORMESSAGE = "invalid password";
					errorMessage="invalid password";
				}
				
				if (isemail == 1 && pwd) {
					
					try{
						String ID = null;
						String USER_EMAIL=null;
						String USER_NAME = null;

					   con=BpoUtils.getConnection();
					//	 PreparedStatement ps=con.prepareStatement("insert into tblusers(firstName,lastName,emailID,phoneNumber,address,username,password,Location,Qualification,Specialization,Gender,Timing,AvailabilityStatus) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");  
					   PreparedStatement ps=con.prepareStatement("select * from users where USER_EMAIL=?;");
	             //   ps.setInt(1,Integer.parseInt(ID.trim()));
					   ps.setString(1, userEmail);
					    ResultSet rs=ps.executeQuery();
					    rs.beforeFirst();
					    if (rs.next()) {
					    	  rs.beforeFirst();
					    	while(rs.next()){
					    		jsonObjectResponse=new JSONObject();
					    		ID=rs.getString("ID");
					    		jsonObjectResponse.put("ID",ID);
					    		USER_EMAIL=rs.getString("USER_EMAIL");
					    		USER_NAME = rs.getString("USERNAME");
					    		jsonObjectResponse.put("EMAIL",USER_EMAIL);
					    		jsonObjectResponse.put("USER_NAME",USER_NAME);
					    		jsonObjectResponse.put("statuscode", "200");
								jsonObjectResponse.put("statusMessage","Success");
								jsonObjectResponse.put("successMessage", "Login Success");
								//jsonObjectResponse.put("error", jsonObjectErrorResponse);
							}
					    	//jsonArray.put(jsonObjectResponse);
					    	//jsonObjectFinalResponse.put("UserInfo", jsonArray);	
					    	
					    	JSONArray numberDump = BpoUtils.getNumberDump(userEmail);
					    	jsonObjectResponse.put("phone_num_dump", numberDump);
							response=jsonObjectResponse.toString();

					    	//jsonObjectFinalResponse.put("ID", ID);
							//response=jsonObjectFinalResponse.toString();	
						}else{
							System.out.println("No  records available");
						}
						 return Response.status(200).entity(response).build();	 
				   }
				   catch(SQLException sqle){
					   sqle.printStackTrace();
					   return Response.status(500).entity(response).build();
				   }
				 
					// jsonObjectResponse.put("id", SampleUtil.getLastid());
					//	com.tband_connectins.util.StatusCode.SUCCESSMESSAGE = "Login success";
					//com.tband_connectins.util.StatusCode.STATUS = com.tband_connectins.util.StatusCode.SUCCESS_CODE;
					//com.tband_connectins.util.StatusCode.MESSAGE = "success";

				}

				//jsonObjectFinalResponse.put("response", jsonObjectResponse);
				jsonObjectFinalResponse.put("statuscode", AirtelStatusCodes.STATUS);
				jsonObjectFinalResponse.put("statusMessage",AirtelStatusCodes.MESSAGE);
				jsonObjectFinalResponse.put("successMessage", AirtelStatusCodes.SUCCESSMESSAGE);
				jsonObjectFinalResponse.put("errorMessage", errorMessage);
				response = jsonObjectFinalResponse.toString();
				
			return Response.status(AirtelStatusCodes.STATUS).entity(response).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				response = jsonObjectResponse.put("error", e.toString()).toString();
				jsonObjectResponse.put("statuscode", AirtelStatusCodes.SERVERERROR_CODE);
				jsonObjectResponse.put("errorMessage", AirtelStatusCodes.ERRORMESSAGE);
				System.out.println("execption_block_fired");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return Response.status(500).entity(response).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registerUser")
	public Response registerUser(@FormParam("USERNAME") String userEmail,@FormParam("PASSWORD") String userPassword) {
		JSONObject response = new JSONObject();
		System.out.println("registration_fired_"+userEmail+" "+userPassword);
		if(userEmail != null && userPassword != null) {
			if(BpoUtils.resgisterNewUser(userEmail, userPassword) != 0) {
				response = buildRegResponse("1");
				System.out.println("Registration Success!");
			}else {
				response = buildRegResponse("0");
				System.out.println("Registration Fail!");
			}
			return Response.ok("200").entity(response).build();
		}else {
			System.out.println("registration_params_"+null);
		}
		return null;
	}
	
	public JSONObject buildRegResponse(String statusCode) {
		
		JSONObject response = new JSONObject();
		try {
			response.put("response_status",statusCode);
			return response;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/submitForm")
	public Response submitForm(@FormParam("USER_EMAIL") String userEmail,@FormParam("CALL_NUMBER") String customerNumber,@FormParam("NETWORK_TYPE") String customerNetworkType,@FormParam("NETWORK_GENERATION") String customerNetworkGen,@FormParam("AVG_MONEY_SPENT") String customerAvgMnySpnetPerMnth,@FormParam("PLAN_SELECTION") String customerSelectedPlan,@FormParam("COMMENT") String customerComment,@FormParam("FOLLOW_UP_DATE") String customerFollowUpDate,@FormParam("FOLLOW_UP_TIME") String  customerFollowUpTime,@FormParam("CUSTOMER_NAME") String  customerName,@FormParam("CUSTOMER_NUMBER") String  customerContactNumber,@FormParam("CUSTOMER_ALT_NUMBER") String  customerAltContactNumber,@FormParam("ADRS_FLAT_NO") String  customerAdrsFlatNo,@FormParam("ADRS_STREET_NAME") String  customerAdrsStreetNme,@FormParam("ADRS_LLD_MARK") String  customerAdrsLldMark,@FormParam("ADRS_CITY") String  customerAdrsCity,@FormParam("ADRS_PINCODE") String customerAdrsPincode,@FormParam("NETWORK_NAME") String networkName,@FormParam("MANUAL_CALL_STATUS") String manualCallStatus,@FormParam("CALL_BACK_COMMENT") String customerCallBackComment) {
		
		int leadCount = 0;
		JSONObject response = new JSONObject();
		String errorMessage=null;
		
		if(userEmail != null) {
			
			if(customerComment.contains("Call Back")) {
				System.out.println("Call Back Selected!");
				if(BpoUtils.executeCallBack(userEmail,customerFollowUpDate,customerFollowUpTime,customerName,customerNumber,customerCallBackComment) != 0) {
					System.out.println("Call Back Added!");
				}
			}else {
				//System.out.println("It's not callback record!");
			}
			
			if(BpoUtils.submitAForm(userEmail, customerNumber,customerNetworkType,customerNetworkGen,customerAvgMnySpnetPerMnth,customerSelectedPlan,customerComment,customerFollowUpDate,customerFollowUpTime,customerName,customerContactNumber,customerAltContactNumber,customerAdrsFlatNo,customerAdrsStreetNme,customerAdrsLldMark,customerAdrsCity,customerAdrsPincode,networkName,manualCallStatus,customerCallBackComment) != 0) {
				//response = buildRegResponse("1");
				leadCount = BpoUtils.getLeadCount(userEmail);
				try {
					response.put("USERNAME",userEmail);
					response.put("statuscode", "200");
					response.put("leadCount", leadCount);
					response.put("statusMessage","Success");
					response.put("successMessage", "Submission Success");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("Form Submission Success!");
			}else {
				//response = buildRegResponse("0");
				try {
					errorMessage="please enter details";
					response.put("statusMessage","Fail");
					response.put("errorMessage", errorMessage);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				System.out.println("Registration Fail!");
			}
			return Response.ok("200").entity(response).build();
		}else {
			System.out.println("registration_params_"+null);
		}
		
		return null;
		
		/*JSONObject response = new JSONObject();
		System.out.println("registration_fired_"+userEmail+" "+userPassword);
		if(userEmail != null && userPassword != null) {
			if(BpoUtils.resgisterNewUser(userEmail, userPassword) != 0) {
				response = buildRegResponse("1");
				System.out.println("Registration Success!");
			}else {
				response = buildRegResponse("0");
				System.out.println("Registration Fail!");
			}
			return Response.ok("200").entity(response).build();
		}else {
			System.out.println("registration_params_"+null);
		}
		return null;*/
	}
	
	
	@POST
	@Path("/callForward")
	public Response forwardCall(@FormParam("USER_EMAIL") String userEmail,@FormParam("FORWARDED_NUMBER") String forwardedNumber) {
	
		JSONObject response = new JSONObject();
		System.out.println("call_forwarding_fired");
		if(userEmail != null && forwardedNumber != null) {
			if(BpoUtils.executeCallForward(userEmail,forwardedNumber) != 0) {
				
				try {
					response.put("statuscode", "200");
					response.put("statusMessage","Success");
					response.put("successMessage", "Call Forward Success");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Call Forwarding Success!");
			}else {
				try {
					response.put("statuscode", AirtelStatusCodes.BADREQUEST_CODE);
					response.put("statusMessage","Call Forward Fail");
					response.put("errorsMessage", "Something Went Wrong!");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Call Forwarding Fail!");
			}
			return Response.ok("200").entity(response).build();
		}else {
			System.out.println("Call Forwarding_"+null);
		}
		return null;
	}
	
	
	@POST
	@Path("/updateStatusReport")
	public Response updateStatusReport(@FormParam("USER_EMAIL") String userEmail,@FormParam("USER_LOGIN_TIME") String userLoginTime,@FormParam("USER_LOGOUT_TIME") String userLogoutTime,@FormParam("DATE") String statusDate,@FormParam("TEA_BREAK") String userTeaBrkTime,@FormParam("LUNCH_BREAK") String userLunchBrkTime,@FormParam("NO_OF_CALLS") String userNoFCalls,@FormParam("NO_OF_LEADS") String userNoFLeads,@FormParam("AVG_TIME_PER_CALL") String avgTimePerCal) {
	
		JSONObject response = new JSONObject();
		System.out.println("update_status_report"+userEmail+" LGINTIME_"+userLoginTime+" LGOTTIME_"+userLogoutTime+" DATE"+statusDate+" TE_BRK_"+userTeaBrkTime+" LUNCH_BREAK_"+userLunchBrkTime+" NO_OF_CALLS_"+userNoFLeads+" AVG_TIME_PER_CALL_"+avgTimePerCal);
		if(userEmail != null && userLoginTime != null && userLogoutTime != null && userTeaBrkTime != null && userLunchBrkTime != null && userNoFCalls != null && userNoFLeads != null && avgTimePerCal != null && statusDate!= null) {
			if(BpoUtils.updateStatusReport(userEmail,userLoginTime,userLogoutTime,statusDate,userTeaBrkTime,userLunchBrkTime,userNoFCalls,userNoFLeads,avgTimePerCal) != 0) {
				//response = buildRegResponse("1");
				try {
					response.put("statuscode", "200");
					response.put("statusMessage","Success");
					response.put("successMessage", "Call Forward Success");
					System.out.println("Update Status Report Success!");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				try {
					response.put("statuscode", AirtelStatusCodes.BADREQUEST_CODE);
					response.put("statusMessage","Fail");
					response.put("errorsMessage", "Something Went Wrong!");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Call Forwarding Fail!");
				System.out.println("Update Status Report Fail!");
			}
			return Response.ok("200").entity(response).build();
		}else {
			System.out.println("Call Forwarding_"+null);
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadDump")
	public Response uploadDBDump() {
		
		String fileName = System.getProperty("user.home") + File.separatorChar + "My Documents"+File.separatorChar+"number_data_dump.csv";
		JSONObject response = new JSONObject();
		System.out.println("db_upload_dump_fired_");
		System.out.println("\nRead CSV file:"+"file_name_"+fileName);
		CsvFileReader.readCsvFile(fileName);
		//BpoUtils.setNumberDumpToUsers();
		response = buildRegResponse("1");
		return Response.ok("200").entity(response).build();
	
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUsersForCallTransfer")
	public Response getUsersForCallTransfer(@QueryParam("USER_EMAIL") String email) {
		JSONObject finalResponse = new JSONObject();
		JSONArray jsonResponseArray = new JSONArray(); 
		ArrayList<UserDataForCallTransfer> userDataForCalTransfer = BpoUtils.getUserDataForCallTransfer(email);
		if(userDataForCalTransfer != null) {
			try {
				finalResponse.put("statusMessage", "Success");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(UserDataForCallTransfer userData: userDataForCalTransfer) {
				JSONObject jsonObject =new JSONObject();
				try {
					jsonObject.put("Id", userData.getId());
					jsonObject.put("userName",userData.getUserName());
					jsonObject.put("userEmail",userData.getUserEmail());
					jsonObject.put("userLang", userData.getUserKnownLanguage());
					jsonResponseArray.put(jsonObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				finalResponse.put("userData", jsonResponseArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return Response.ok("200").entity(finalResponse).build();
			
		}else {
			System.out.println("user_data_is_null");
			try {
				finalResponse.put("statusMessage", "fail");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return Response.ok("400").entity(finalResponse).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNumberDump")
	public Response getNumberDump() {
		JSONArray jsonArrayResponse = new JSONArray();
		finalResponseObject = null;
		ArrayList<AirtelNumberData> requiredData = BpoUtils.getNumberDatabase();
		
		if(requiredData != null && requiredData.size() != 0) {
			
			for(AirtelNumberData singleDta: requiredData) {
				JSONObject jObj = new JSONObject();
				try {
					jObj.put("Id", singleDta.getId());
					jObj.put("PhoneNumber", singleDta.getNumber());
					jsonArrayResponse.put(jObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			finalResponseObject = new JSONObject();
			try {
				finalResponseObject.put("number_dump", jsonArrayResponse);
				finalResponseObject.put("status_code","200");
				finalResponseObject.put("status_message","Success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(finalResponseObject != null) {
				return Response.ok(200).entity(finalResponseObject).build();
			}
		}else {
			
			try {
				finalResponseObject.put("status_code","400");
				finalResponseObject.put("status_message","No Data Found");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return Response.ok(400).entity(finalResponseObject).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkForFCall")
	public Response checkForCallForwardStatus(@QueryParam("EMAIL") String email) {
		JSONObject responseObj = new JSONObject();
		AirtelNumberData callForwardedData = BpoUtils.checkForCallForwrdNumbers(email);
		if(callForwardedData != null) {
			try {
				responseObj.put("statusMessage", "1");
				responseObj.put("Id",callForwardedData.getId());
				responseObj.put("forwardedNumber",callForwardedData.getNumber());
				return Response.ok("200").entity(responseObj).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				responseObj.put("statusMessage", "0");
				return Response.ok("200").entity(responseObj).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkForCallRemainder")
	public Response checkForCallRemainder(@QueryParam("USER_EMAIL") String email,@QueryParam("CALL_BACK_DATE") String callBackDate,@QueryParam("CALL_BACK_TIME") String callBackTime) {
		JSONObject jsonResponse = new JSONObject();
		//System.out.println("email_"+email+" Date_"+callBackDate+" Time_"+callBackTime);
		CallRemainderData callRemainderDta = BpoUtils.checkForCallRemainder(email,callBackDate,callBackTime);
		if(callRemainderDta != null) {
			try {
				
				jsonResponse.put("statusMessage", "success");
				jsonResponse.put("Id",callRemainderDta.getId());
				jsonResponse.put("userEmail",callRemainderDta.getUserEmail());
				jsonResponse.put("userName",callRemainderDta.getUserCustomerName());
				jsonResponse.put("userContactNumber",callRemainderDta.getUserContactNumber());
				jsonResponse.put("userCalBackComment",callRemainderDta.getUserRemainderComment());
				
				return Response.ok("200").entity(jsonResponse.toString()).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			//System.out.println("call remainder data is null");
			try {
				jsonResponse.put("statusMessage", "Fail");
				return Response.ok("200").entity(jsonResponse.toString()).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Response.ok("400").build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateCallRemainderData")
	public Response updateCallRemainderData(@FormParam("ID") String emailId,@FormParam("CALL_BACK_DATE") String date,@FormParam("CALL_BACK_TIME") String time) {
		
		JSONObject jsonResponse = new JSONObject();		
		if(BpoUtils.updateCallBackData(emailId,date,time) != 0) {
			System.out.println("Callback update Is finished!");
			try {
				jsonResponse.put("statusCode", "200");
				jsonResponse.put("statusMessage", "Success!");
				return Response.ok("200").entity(jsonResponse.toString()).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//System.out.println("Callback update Is not finished!");
			try {
				jsonResponse.put("statusCode", "400");
				jsonResponse.put("statusMessage", "Un Successful!");
				return Response.ok("400").entity(jsonResponse.toString()).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Response.ok("400").build();
	}
	
	@GET
	@Path("/getNextDump")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextSetOfNumbers(@QueryParam("USER_EMAIL") String userEmail) {
		
		JSONObject jsonFinalResponse = new JSONObject();
		JSONArray numberDump = BpoUtils.getNumberDump(userEmail);
    	try {
    		jsonFinalResponse.put("statusCode", "200");
    		jsonFinalResponse.put("statusMessage", "Success!");
    		jsonFinalResponse.put("phone_num_dump", numberDump);
			return Response.ok("200").entity(jsonFinalResponse.toString()).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	try {
			jsonFinalResponse.put("statusCode", "400");
			jsonFinalResponse.put("statusMessage", "Un Success!");
			jsonFinalResponse.put("phone_num_dump", "null");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok("400").entity(jsonFinalResponse.toString()).build();
	}
	
	@POST
	@Path("/sendUniqueGcmDeviceToken")
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveDeviceTokenId(@FormParam("USER_EMAIL") String userEmail,@FormParam("DEVICE_TOKEN_ID") String deviceTokenId) {
		JSONObject finalResponse = new JSONObject();
		System.out.println("email_"+userEmail+"_device_id_"+deviceTokenId);
		if(BpoUtils.saveDeviceTokenIdToDB(userEmail, deviceTokenId) >= 1) {
			try {
				finalResponse.put("statusCode", "200");
				finalResponse.put("statusMessage", "Request Success!");
				finalResponse.put("successMessage", "Device Token Sent To Server!");
				Response.ok("200").entity(finalResponse.toString()).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else {
			System.out.print("something_went_wrong_in_saving_device Id");
			try {
				finalResponse.put("statusCode", "400");
				finalResponse.put("statusMessage", "Request UnSuccess!");
				finalResponse.put("successMessage", "Device Token Not Sent To Server!");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Response.ok("400").entity(finalResponse.toString()).build();
	}
	
}