import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.stream.events.StartDocument;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.spec.DSAGenParameterSpec;
import java.util.jar.Attributes.Name;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatClient extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;

	private String name = null;
	boolean connect = false;
	private Socket s = null;
	private DataOutputStream dos = null;
	private StringBuilder record = null;
	private JTextArea textArea;
	
	public void connect()
	{
		try
		{
			s = new Socket("192.168.0.106", 6666);
			dos = new DataOutputStream(s.getOutputStream());
			connect =true;
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		try
		{
			dos.close();
			s.close();
			connect = false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public void recordUpData()
	{
		try
		{
			DataInputStream dis = new DataInputStream(s.getInputStream());
			String string = dis.readUTF();
			textArea.setText(string);
		}
		catch (IOException e)
		{
			
		}
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					ChatClient frame = new ChatClient();
					frame.launch();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void launch()
	{
		try
		{
			InputName();
			setVisible(true);
			connect();
			new Thread(new Show()).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Create the frame.
	 */
	public ChatClient()
	{
		record = new StringBuilder();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				disconnect();
				System.exit(0);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 483);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		contentPane.add(textArea, BorderLayout.CENTER);
		
		textField = new JTextField();
		textField.setFont(new Font("ËÎÌו", Font.PLAIN, 20));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int key = arg0.getKeyCode();
				if (key == KeyEvent.VK_ENTER)
				{
					String str = textField.getText().trim();
					
					record.append(str);
					textArea.setText(record.toString());
					textField.setText("");
					
					try
					{
						dos.writeUTF(name + ":" + str);
						dos.flush();
						
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					
				}
			}
		});
		contentPane.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
	}
	
	class Show implements Runnable
	{

		@Override
		public void run()
		{
			while (connect)
			{
				try
				{
					Thread.sleep(100);
					recordUpData();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public void InputName()
	{
		try
		{
			System.out.println("Please input your name:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			name = br.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
