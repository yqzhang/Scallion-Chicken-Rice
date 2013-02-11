package bit.scallionchickenrice.raph.communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessSocket {
	private static final int PORT = 7797;// 端口监听  
    private List<Socket> mList = new ArrayList<Socket>();// 存放客户端socket  
    private ServerSocket server = null;  
    private ExecutorService mExecutorService = null;// 线程池  
	
	public ProcessSocket() {
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    mExecutorService = Executors.newCachedThreadPool();// 创建一个线程池  
	    System.out.println("Server Start...");
	    Socket client = null; 
	    while (true) {  
	        try {
				client = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        System.out.println("client " + client.getInetAddress() + " receive..."); 
	        mList.add(client);
	        mExecutorService.execute(new Service(client));// 开启一个客户端线程.  
	    }
	}
	
	public class Service implements Runnable {  
		  
        private Socket socket;  
        private String msg = "";  
  
        public Service(Socket socket) {  
            this.socket = socket;  
        }  
  
        public void run() {  
            // TODO Auto-generated method stub  
            try {  
            	DataInputStream in = new DataInputStream(socket.getInputStream());
                if (( msg = in.readUTF()) != null) {
                	//System.out.println(msg);
                	ProcessMessage pm = new ProcessMessage(socket);
                	pm.query(msg, socket.getInetAddress().toString().replace("/", ""));
               		socket.close();  
               		mList.remove(socket);  
                }  
            } catch (Exception ex) {  
                System.out.println("server 读取数据异常");  
                ex.printStackTrace();  
            }  
        }  
    }  
}
