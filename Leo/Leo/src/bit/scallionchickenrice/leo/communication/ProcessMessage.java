package bit.scallionchickenrice.leo.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import bit.scallionchickenrice.leo.DAO.ControlDB;
import bit.scallionchickenrice.leo.DAO.LeoResultSet;
import bit.scallionchickenrice.leo.security.AuthorizeTool;

public class ProcessMessage {
	private Socket socket = null;

	public ProcessMessage(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void query(String str, String ipAddress) throws Exception {
		String[] temp = str.split(" ");
		System.out.println("Query: " + temp[0]);
		int type = Integer.parseInt(temp[0]);
		
		System.out.println("Context:" + str);

		switch (type) {
		//Active Device
		case (0): {
			//Active Device
			String activeKey = temp[1];
			String MACAddress = temp[2];
			String userType = temp[3];
			
			ControlDB dsm = ControlDB.getInstance();
			
			String sql = "SELECT * FROM keyInfo WHERE activeKey = '" + activeKey + "'";
			
			LeoResultSet rs = dsm.executeQuery(sql);
			
			if(rs.next()) {
				sql = "DELETE FROM keyInfo WHERE activeKey = '" + activeKey + "'";
				dsm.executeUpdate(sql);
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar calendar = Calendar.getInstance();
				String dateTime = df.format(calendar.getTime());
				
				sql = "INSERT INTO activeInfo(activeKey, activeTime, activeType, MACAddress) VALUES('" + activeKey + "','" + dateTime + "', '" + new String(userType.getBytes("GBK"),"ISO-8859-1") + "', '" + MACAddress + "')";
				dsm.executeUpdate(sql);
				
				DataOutputStream dos = null;
				DataInputStream dis = null;
				
		        try {
		        	dos = new DataOutputStream(socket.getOutputStream());
		        	dis = new DataInputStream(socket.getInputStream());
		            
		        	dos.write((new String("true")).getBytes());
		        	dos.flush();
		            dis.read();
		            
		        	dos.write(AuthorizeTool.generateBootFile(MACAddress));
		        	dos.flush();
		        	dos.close();
		        } catch (IOException e) {  
		            e.printStackTrace();
		        }
			}
			else {
				OutputStream out = null;  
		        try {  
		            out = socket.getOutputStream();
		            
		            out.write((new String("false")).getBytes());
		            out.flush();
		            out.close();
		        } catch (IOException e) {  
		            e.printStackTrace();
		        }
			}
			
			break;
		}
		}
	}
}
