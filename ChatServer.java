import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer
{
	ServerSocket ss = null;
	static StringBuilder record = null;
	
	
	public static void main(String[] args) throws InterruptedException
	{
		ChatServer.record = new StringBuilder();
		new ChatServer().start();
	}
	
	public void start() //启动方法
	{
		try
		{
			ss = new ServerSocket(6666);
			while (true)
			{
				Client c = new Client(ss.accept());
				new Thread(c).start();
				
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	class Client implements Runnable //客户端内部类
	{
		Socket s = null;
		boolean connect = false;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		public Client(Socket s)
		{
			this.s = s;
			try
			{
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				connect = true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			new Thread(new Send()).start();
		}
		public void run()
		{
			String str = null;
			try
			{
				while ((str = dis.readUTF()) != null)
				{
					record.append(str+"\n");
//					System.out.println(str);
				}
			}
			catch (IOException e)
			{
				try
				{
					connect = false;
					s.close();
					dis.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				
			}
		}
		
		class Send implements Runnable
		{
				
			public void run()
			{
				while (connect)
				{
					try
					{
						Thread.sleep(100);
						dos.writeUTF(record.toString());
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
						
		}
	}
	


}


