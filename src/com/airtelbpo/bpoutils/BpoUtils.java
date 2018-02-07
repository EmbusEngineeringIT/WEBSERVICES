package com.airtelbpo.bpoutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.airtelbpo.pojo.AirtelNumberData;
import com.airtelbpo.pojo.CallRemainderData;
import com.airtelbpo.pojo.UserDataForCallTransfer;

public class BpoUtils {
	
	static Connection con=null;
	
public static Connection getConnection(){
	
		try{
			//System.out.println("enter into connection method");
			Class.forName("com.mysql.jdbc.Driver");
			
			/*Here we wre giving datanase connection here 192.168.2.184 is Ip address of your database and 3306 is port number of database  master is database name and smartadmin is username and smartadmin password*/
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bpodb?useUnicode=true&amp;characterEncoding=utf-8","root","cheemaladb");
			//System.out.println("connection estab");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return con;
		
	}
	
	public static int checkEmail(String email) {
		
		System.out.println("email_"+email);
		Connection conn = getConnection();
		String sql = "select USER_EMAIL from users where USER_EMAIL=?";
		int status = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			ResultSet res = stmt.executeQuery();
			while (res.next()) {
				status = 1;
				System.out.println("executed!");
			}
			System.out.println("came");
			res.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("get email address exception1");
		}

		return status;
	}
	
	
	public static boolean checkUserPassword(String password, String emailid) {
		boolean status = false;
		Connection conn = getConnection();
		String sql = "select USER_PASSWORD from users where  USER_PASSWORD=? and USER_EMAIL=?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setString(2, emailid);
			ResultSet res = stmt.executeQuery();
			while (res.next()) {
				status = true;
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("checkUserPassword exception");
		}
		return status;
	}

	public static int resgisterNewUser(String userEmail,String userPassword) {
		 
		Connection con = getConnection();
		PreparedStatement dynamicParameters = null;
		String insertQuery = "INSERT INTO users(USER_EMAIL,USER_PASSWORD) VALUES(?,?)";
		
		if(userEmail != null && userPassword != null) {
			try {
				dynamicParameters = con.prepareStatement(insertQuery);
				dynamicParameters.setString(1,userEmail);
				dynamicParameters.setString(2, userPassword);
				return dynamicParameters.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					
					con.close();
					dynamicParameters.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("userEmail_"+userEmail+" userPassword_"+userPassword+" null");
		}
		
		return 0;
	}

	public static int submitAForm(String userEmail, String customerNumber, String customerNetworkType,
			String customerNetworkGen, String customerAvgMnySpnetPerMnth, String customerSelectedPlan,
			String customerComment, String customerFollowUpDate, String customerFollowUpTime, String customerName,
			String customerContactNumber, String customerAltContactNumber, String customerAdrsFlatNo,
			String customerAdrsStreetNme, String customerAdrsLldMark, String customerAdrsCity, String customerAdrsPincode,String networkName,String manualCallStatus,String customerCalBackComment) {
		// TODO Auto-generated method stub
		
		Connection con = getConnection();
		PreparedStatement dynamicParameters = null;
		String insertQuery = "INSERT INTO form_submitions(USER_EMAIL,CALL_NUMBER,NETWORK_TYPE,NETWORK_GENERATION,AVG_MONEY_SPENT,PLAN_SELECTION,COMMENT,FOLLOW_UP_DATE,FOLLOW_UP_TIME,CUSTOMER_NAME,CUSTOMER_NUMBER,CUSTOMER_ALT_NUMBER,ADRS_FLAT_NO,ADRS_STREET_NAME,ADRS_LLD_MARK,ADRS_CITY,ADRS_PINCODE,SUBMITION_TIMESTAMP,NETWORK_NAME,MANUAL_CALL_STATUS,CALL_BACK_COMMENT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		//if(userEmail != null && userPassword != null) {
			try {
				dynamicParameters = con.prepareStatement(insertQuery);
				dynamicParameters.setString(1,userEmail);
				dynamicParameters.setString(2, customerNumber);
				dynamicParameters.setString(3, customerNetworkType);
				dynamicParameters.setString(4, customerNetworkGen);
				dynamicParameters.setString(5,customerAvgMnySpnetPerMnth);
				dynamicParameters.setString(6, customerSelectedPlan);
				dynamicParameters.setString(7, customerComment);
				dynamicParameters.setString(8, customerFollowUpDate);
				dynamicParameters.setString(9, customerFollowUpTime);
				dynamicParameters.setString(10, customerName);
				dynamicParameters.setString(11, customerContactNumber);
				dynamicParameters.setString(12, customerAltContactNumber);
				dynamicParameters.setString(13, customerAdrsFlatNo);
				dynamicParameters.setString(14, customerAdrsStreetNme);
				dynamicParameters.setString(15, customerAdrsLldMark);
				dynamicParameters.setString(16, customerAdrsCity);
				dynamicParameters.setString(17,customerAdrsPincode);
				dynamicParameters.setTimestamp(18, Timestamp.valueOf(java.time.LocalDateTime.now()));
				dynamicParameters.setString(19, networkName);
				dynamicParameters.setString(20,manualCallStatus);
				dynamicParameters.setString(21, customerCalBackComment);
				return dynamicParameters.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					
					con.close();
					dynamicParameters.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		/*}else {
			System.out.println("userEmail_"+userEmail+" userPassword_"+userPassword+" null");
		}*/
		
		
		return 0;
	}

	public static int getLeadCount(String userEmail) {
		//System.out.println("em:_"+userEmail);
		int count = 0;
		Connection con = getConnection();
		PreparedStatement dynamicParameters = null;
		//String leadQuery = "SELECT * FROM form_submitions WHERE USER_EMAIL=? and COMMENT=Shedule Pickup";
		String leadQuery = "SELECT COUNT(*) FROM form_submitions WHERE USER_EMAIL=? AND COMMENT=?";
		
		try {
			dynamicParameters = con.prepareStatement(leadQuery);
			dynamicParameters.setString(1,userEmail);
			dynamicParameters.setString(2, "Schedule Pickup");
			ResultSet rRet = dynamicParameters.executeQuery();
			rRet.next();
			count = rRet.getInt(1);
		
			//System.out.println("lead_count"+count);
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public static int executeCallForward(String userEmail,String forwardedNumber) {
		Connection con = getConnection();
		String addCallQuery = "INSERT INTO phone_number_dump(EMAIL,PHONE_NUMBER,FLAG,CALL_TRANSFER_FLAG) VALUES(?,?,?,?)";
		try {
			PreparedStatement addCallStmt = con.prepareStatement(addCallQuery);
			addCallStmt.setString(1, userEmail);
			addCallStmt.setString(2, forwardedNumber);
			addCallStmt.setBoolean(3, true);
			addCallStmt.setBoolean(4, false);
			int resultVal = addCallStmt.executeUpdate();
			if(resultVal != 0)
				pushdataToGCM(userEmail,forwardedNumber);
			return resultVal;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	private static void pushdataToGCM(String userEmail, String forwardedNumber) {
		// TODO Auto-generated method stub
		PreparedStatement dynamicParameters = null;
		String deviceIdQueryString = "SELECT DEVICE_ID FROM users WHERE USER_EMAIL=?";
		
		try {
			dynamicParameters = con.prepareStatement(deviceIdQueryString);
			dynamicParameters.setString(1,userEmail);
			ResultSet rRet = dynamicParameters.executeQuery();
			if(rRet.next()) {
				String deviceId = rRet.getString("DEVICE_ID");
				if(deviceId != null) {
					System.out.print("device_id_"+deviceId);
					AirtelNumberData callForwardedData = BpoUtils.getCallForwardedForwrdNumber(userEmail,forwardedNumber);
					if(callForwardedData != null) {
						MakeWebCall sendDataToGCMServer = new MakeWebCall(deviceId,callForwardedData.getId() ,callForwardedData.getNumber());
						Thread webCallThread = new Thread(sendDataToGCMServer);
						webCallThread.start();
					}
				}else {
					System.out.print("device_id_null_in_db");
				}
			}
		
			//System.out.println("device_id_null_in_db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static AirtelNumberData getCallForwardedForwrdNumber(String userEmail, String forwardedNumber) {
		// TODO Auto-generated method stub
		Connection con = getConnection();
		String query = "SELECT ID,PHONE_NUMBER FROM phone_number_dump WHERE CALL_TRANSFER_FLAG = ? and EMAIL = ? and PHONE_NUMBER = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setBoolean(1, false);
			ps.setString(2, userEmail);
			ps.setString(3, forwardedNumber);
			ResultSet result = ps.executeQuery();
			if(result != null) {
				result.next();
				AirtelNumberData forwardedCallData = new AirtelNumberData(result.getString(1), result.getString(2));		
				
				if(updateFlagValues(result.getString(1)) != 0) {
					//System.out.println("flag_upfdated_successfully");
				}else{
					//System.out.println("flag_upfdated_successfully");
				}
				
				return forwardedCallData;
			}else {
				System.out.println("result_is_null");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int updateStatusReport(String userEmail, String userLoginTime, String userLogoutTime,String date,
			String userTeaBrkTime, String userLunchBrkTime, String userNoFCalls, String userNoFLeads,
			String avgTimePerCal) {
		
		//SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
		  //String input = "2014-01-19";  // capture the value you pass from attendance application
		Connection con = getConnection();
		String addCallQuery = "INSERT INTO status_report(USER_EMAIL,USER_LOGIN_TIME,USER_LOGOUT_TIME,DATE,TEA_BREAK,LUNCH_BREAK,NO_OF_CALLS,NO_OF_LEADS,AVG_TIME_PER_CALL) VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement addCallStmt = con.prepareStatement(addCallQuery);
			addCallStmt.setString(1, userEmail);
			addCallStmt.setString(2, userLoginTime);
			addCallStmt.setString(3, userLogoutTime);
			addCallStmt.setDate(4,Date.valueOf(date));
			addCallStmt.setString(5, userTeaBrkTime);
			addCallStmt.setString(6, userLunchBrkTime);
			addCallStmt.setString(7, userNoFCalls);
			addCallStmt.setString(8, userNoFLeads);
			addCallStmt.setString(9, avgTimePerCal);
			return addCallStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static int importNumberDump(ArrayList<AirtelNumberData> numberDump) {
		
		Connection connection = getConnection();
		String insertDumpQuery = "INSERT INTO phone_number_dump(PHONE_NUMBER) VALUES(?)";
		
		if(numberDump != null && numberDump.size() != 0) {
			for(AirtelNumberData data: numberDump) {
				try {
					PreparedStatement ps = connection.prepareStatement(insertDumpQuery);
					ps.setString(1, data.getNumber());
					ps.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return 1;
		}else {
			System.out.println("error: number_dump_null");
		}
		
		return 0;
	}
	
	public static ArrayList<AirtelNumberData> getNumberDatabase(){
		Connection connection = getConnection();
		ArrayList<AirtelNumberData> data = new ArrayList<>();
		String retreiveDataQuery = "SELECT * FROM phone_number_dump";
		
		try {
			PreparedStatement selectQuery = connection.prepareStatement(retreiveDataQuery);
			ResultSet resultSet = selectQuery.executeQuery();
			if(resultSet != null) {
				resultSet.next();
				while(resultSet.next()) {
					if(resultSet.getBoolean(4) == false) {
						System.out.println("It's 0");
						AirtelNumberData airNumberdata = new AirtelNumberData(resultSet.getString(1), resultSet.getString(2));
						data.add(airNumberdata);
					}
					else {
						System.out.println("It's 1");
					}
				}
				return data;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static AirtelNumberData checkForCallForwrdNumbers(String emailValue) {
		
		Connection con = getConnection();
		String query = "SELECT ID,PHONE_NUMBER FROM phone_number_dump WHERE CALL_TRANSFER_FLAG = ? and EMAIL = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setBoolean(1, false);
			ps.setString(2, emailValue);
			ResultSet result = ps.executeQuery();
			if(result != null) {
				result.next();
				AirtelNumberData forwardedCallData = new AirtelNumberData(result.getString(1), result.getString(2));		
				
				if(updateFlagValues(result.getString(1)) != 0) {
					//System.out.println("flag_upfdated_successfully");
				}else{
					//System.out.println("flag_upfdated_successfully");
				}
				
				return forwardedCallData;
			}else {
				System.out.println("result_is_null");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int updateFlagValue(String Id) {
		String updateFlagQuery = "UPDATE phone_number_dump SET FLAG = ? WHERE ID = ?";
		Connection con = getConnection();
		
		try {
			PreparedStatement updatableValues = con.prepareStatement(updateFlagQuery);
			updatableValues.setBoolean(1, true);
			updatableValues.setString(2, Id);
			return updatableValues.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	public static int updateFlagValues(String Id) {
		String updateFlagQuery = "UPDATE phone_number_dump SET FLAG = ?,CALL_TRANSFER_FLAG = ? WHERE ID = ?";
		Connection con = getConnection();
		
		try {
			PreparedStatement updatableValues = con.prepareStatement(updateFlagQuery);
			updatableValues.setBoolean(1, true);
			updatableValues.setBoolean(2, true);
			updatableValues.setString(3, Id);
			return updatableValues.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static boolean setNumberDumpToUsers() {
		Connection con = getConnection();
		ArrayList<String> emailIds = new ArrayList<>();
		// TODO Auto-generated method stub
		String getUsrAvlUsers = "SELECT USER_EMAIL FROM users WHERE AVAILABILITY = ?";
		String getPhoneNumberCount = "SELECT COUNT(*) FROM phone_number_dump";
		String setUserToNumDump = "UPDATE phone_number_dump SET EMAIL = ? WHERE ID = ?";
		//JSONArray getResponseArray = new JSONArray();
		try {
			PreparedStatement selectAvlUsrsQuery = con.prepareStatement(getUsrAvlUsers);	
			selectAvlUsrsQuery.setInt(1, 1);
			ResultSet resultantCountObj = selectAvlUsrsQuery.executeQuery();
			
			while (resultantCountObj.next()) {
				emailIds.add(resultantCountObj.getString("USER_EMAIL"));
			}
			
			//System.out.println("Number of rows :" +emailIds.size());
			//phone_number_dump
			//for(int i = 0;i<)
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("exc:_"+e.getMessage());
		}
		
		try {
			
			PreparedStatement selectPhoneNumCount = con.prepareStatement(getPhoneNumberCount);	
			ResultSet phnCount = selectPhoneNumCount.executeQuery();
			phnCount.next();
			int totalCount = phnCount.getInt(1);
			int idCounter = 1;
			System.out.println("phn_num_count:_"+totalCount);
			
			if(totalCount%emailIds.size() == 0) {
		
				for(int i = 0;i < emailIds.size(); i++) {
					for(int j = 0;j < totalCount/emailIds.size(); j++) {
						PreparedStatement assignUserToNumDump = con.prepareStatement(setUserToNumDump);	
						assignUserToNumDump.setString(1, emailIds.get(i));
						assignUserToNumDump.setInt(2, idCounter);
						assignUserToNumDump.executeUpdate();
						idCounter++;
					}
				}
				
			}else {
				
				for(int i = 0;i < emailIds.size(); i++) {
					for(int j = 0;j < totalCount/emailIds.size(); j++) {
						PreparedStatement assignUserToNumDump = con.prepareStatement(setUserToNumDump);	
						assignUserToNumDump.setString(1, emailIds.get(i));
						assignUserToNumDump.setInt(2, idCounter);
						assignUserToNumDump.executeUpdate();
						idCounter++;
					}
				}
				
				for(int j = 0;j < (totalCount%emailIds.size()); j++) {
					PreparedStatement assignUserToNumDump = con.prepareStatement(setUserToNumDump);	
					assignUserToNumDump.setString(1, (emailIds.get(emailIds.size()-1)));
					assignUserToNumDump.setInt(2, idCounter);
					assignUserToNumDump.executeUpdate();
					idCounter++;
				}
				
			}
			
			
		
			
			System.out.println("Dump Is successful");
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("exc:_"+e.getMessage());
		}
		
		return false;
	}
	
/*	private static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    //System.out.println("date:_"+today.getDate());
	    return new java.sql.Date(today.getTime());
	}
	*/
	public static int getEndId(int startId) {
		
		int endId = startId+20;
		return endId;
		
	}

	public static JSONArray getNumberDump(String userEmail) {
		// TODO Auto-generated method stub
		System.out.println("test_email_"+userEmail);
		String getNumbersQueryForUser = "SELECT * FROM phone_number_dump WHERE FLAG = ?";
		String updateEmlNdFlagQuery = "UPDATE phone_number_dump SET EMAIL = ?, FLAG = ? WHERE ID = ?";
		//String setUserToNumDump = "UPDATE phone_number_dump SET EMAIL = ? WHERE ID = ?";
		Connection con = getConnection();
		JSONArray finalResponseArray = new JSONArray();
		try {
			PreparedStatement getNumberDump = con.prepareStatement(getNumbersQueryForUser);	
			getNumberDump.setBoolean(1, false);
			ResultSet resultantNumDump = getNumberDump.executeQuery();
			int counter = 0;
			while (resultantNumDump.next() && counter < 250) {
				//Update FLAG
				PreparedStatement updateFlag = con.prepareStatement(updateEmlNdFlagQuery);
				updateFlag.setString(1, userEmail);
				updateFlag.setBoolean(2, true);
				updateFlag.setInt(3, resultantNumDump.getInt("ID"));
				if(updateFlag.executeUpdate() != 0) {
					System.out.println("Flag_change_done");
				}else {
					System.out.println("Flag_change_not_done");
				}
				
				JSONObject individualContact = new JSONObject();
				individualContact.put("ID",resultantNumDump.getInt("ID"));
				individualContact.put("PHONE_NUMBER",resultantNumDump.getString("PHONE_NUMBER"));
				finalResponseArray.put(individualContact);
				counter++;
			}
			
			
			return finalResponseArray;
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("exc:_"+ex.getMessage());
		}
		return null;
	}

	public static ArrayList<UserDataForCallTransfer> getUserDataForCallTransfer(String email) {
		// TODO Auto-generated method stub
		String getUserDataQuery = "SELECT * FROM users WHERE USER_EMAIL !=?";
		ArrayList<UserDataForCallTransfer> usersResultantData = new ArrayList<>();
		
		Connection con = getConnection();
		try {
			PreparedStatement getUserDataQueryPS = con.prepareStatement(getUserDataQuery);
			getUserDataQueryPS.setString(1, email);
			ResultSet resultantDataSet = getUserDataQueryPS.executeQuery();
			if(resultantDataSet != null) {
				while(resultantDataSet.next()) {
					UserDataForCallTransfer userData = new UserDataForCallTransfer(resultantDataSet.getString("ID"),resultantDataSet.getString("USERNAME"),resultantDataSet.getString("USER_EMAIL"),resultantDataSet.getString("LANGUAGES_KNOWN"));
					usersResultantData.add(userData);
				}
				return usersResultantData;
			}else {
				System.out.println("user_data_is_null");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static int executeCallBack(String userEmail, String customerFollowUpDate, String customerFollowUpTime,String customerName, String customerNumber, String customerCallBackComment) {
		// TODO Auto-generated method stub
		
		String insertQuery = "INSERT INTO call_remainder(USER_EMAIL,CALL_BACK_DATE,CALL_BACK_TIME,CUSTOMER_NAME,CUSTOMER_NUMBER,CUSTOMER_COMMENT,FLAG) VALUES(?,?,?,?,?,?,?)";
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(insertQuery);
			ps.setString(1, userEmail);
			ps.setString(2, customerFollowUpDate);
			ps.setString(3, customerFollowUpTime);
			ps.setString(4, customerName);
			ps.setString(5, customerNumber);
			ps.setString(6, customerCallBackComment);
			ps.setBoolean(7, false);
			return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	
	}

	public static CallRemainderData checkForCallRemainder(String email, String callBackDate, String callBackTime) {
		// TODO Auto-generated method stub
		String checkClRemainderQuery = "SELECT * FROM call_remainder WHERE USER_EMAIL = ? AND CALL_BACK_DATE = ? AND CALL_BACK_TIME = ? AND FLAG = ?";
		//String checkClRemainderQuery = "SELECT * FROM call_remainder WHERE USER_EMAIL = ? AND CALL_BACK_DATE = ? AND CALL_BACK_TIME = ?";
		Connection con = getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(checkClRemainderQuery);
			ps.setString(1, email);
			ps.setString(2, callBackDate);
			ps.setString(3, callBackTime);
			ps.setBoolean(4, false);
			ResultSet resultantQuery = ps.executeQuery();
			if(resultantQuery != null) {
				resultantQuery.next();
				CallRemainderData requiredDta = new CallRemainderData(resultantQuery.getInt("ID"),resultantQuery.getString("USER_EMAIL"),resultantQuery.getString("CUSTOMER_NAME"),resultantQuery.getString("CUSTOMER_NUMBER"),resultantQuery.getString("CUSTOMER_COMMENT"));
				
				if(requiredDta != null) {
					if(BpoUtils.updateCallRemainderFlag(requiredDta.getId()) != 0) {
						System.out.println("update calback flag success!");
					}else {
						System.out.println("update calback flag fail!");
					}
				}

				return requiredDta;
			}else {
				System.out.println("resultant_data_is_null");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private static int updateCallRemainderFlag(int Id) {
		
		String updateFlagQuery = "UPDATE call_remainder SET FLAG = ? WHERE ID = ?";
		Connection con = getConnection();
		
		try {
			PreparedStatement updatableValues = con.prepareStatement(updateFlagQuery);
			updatableValues.setBoolean(1, true);
			updatableValues.setInt(2, Id);
			return updatableValues.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	public static int updateCallBackData(String Id, String date, String time) {
		System.out.print("update_email: "+Id+" date: "+date+" time"+time);
		String updateFlagQuery = "UPDATE call_remainder SET FLAG=?,CALL_BACK_DATE=?,CALL_BACK_TIME=? WHERE ID = ?";
		Connection con = getConnection();
		
		try {
			PreparedStatement updatableValues = con.prepareStatement(updateFlagQuery);
			updatableValues.setBoolean(1, false);
			updatableValues.setString(2, date);
			updatableValues.setString(3, time);
			updatableValues.setString(4,Id);
			return updatableValues.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
		// TODO Auto-generated method stub
		
	}
	
	public static int saveDeviceTokenIdToDB(String userEmail,String deviceTokenId) {
		String updateFlagQuery = "UPDATE users SET DEVICE_ID = ? WHERE USER_EMAIL = ?";
		Connection con = getConnection();
		
		try {
			PreparedStatement updateDeviceId = con.prepareStatement(updateFlagQuery);
			updateDeviceId.setString(1,deviceTokenId);
			updateDeviceId.setString(2, userEmail);
			return updateDeviceId.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
