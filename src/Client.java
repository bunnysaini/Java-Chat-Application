import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {
	
	private static final String KeyListener = null;

	Socket socket;
	
	BufferedReader br;		//For reading
	PrintWriter out;		//For writing
	
	//Declare components of GUI
	private JLabel heading = new JLabel("Client Side");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Work Sans", Font.PLAIN, 15);
	private Font head = new Font("Work Sans", Font.BOLD, 20);
	private Font msg_font = new Font("Work Sans", Font.PLAIN, 18);
	
	//Constructor
	public Client() {
		try {
			
			System.out.println("Sending a request to server.");
//			socket = new Socket("127.0.0.1",7777);
			socket = new Socket("192.168.1.34",7777);
			System.out.println("Connection Established.");
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//Byte data from Socket changed to character handled by BufferedReader
			out= new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
			initiateReading();
//			initiateWriting();
			
		} catch (Exception e) {
			
		}
	}
	
	private void handleEvents() {
		// TODO Auto-generated method stub
		messageInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==10) {			//Once you press enter button in message box
					String contentToSend=messageInput.getText();
					if(contentToSend.equals("Exit")) {
						messageInput.setEnabled(false);
						dispose();
					}
					messageArea.append("  Me : "+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
		});
		
	}

	private void createGUI() {
		// TODO Auto-generated method stub
		//Generate GUI
		this.setTitle("Client Messenger");
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = Toolkit.getDefaultToolkit().getImage("C:/Users/HP/eclipse-workspace/ChatApp/src/icons/msg-icon.png");
		setIconImage(icon);
		heading.setIcon(new ImageIcon("C:/Users/HP/eclipse-workspace/ChatApp/src/icons/windowicon.png"));
		
		
		//Set text of components
		heading.setFont(head);
		messageArea.setFont(font);
		messageInput.setFont(msg_font);
		
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		messageArea.setBackground(new Color(223, 214, 226));
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Layout Arrangement
		this.setLayout(new BorderLayout());
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane = new JScrollPane(messageArea);
		this.add(jScrollPane, BorderLayout.CENTER);
//		jScrollPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5))
//		this.add(messageArea,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		//set frame to be visible
		this.setVisible(true);
	}

	//To start writing (Only for console)
	public void initiateWriting() {
		// TODO Auto-generated method stub
		// thread that receives data from user and sends it to client
		Runnable r2=()->{
			System.out.println("Initiated Writer...");
			try {
				while(!socket.isClosed()) {	
						BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
						String content = br1.readLine();	//Data read from console sent to server
					
						out.println(content);
						out.flush();
						
						if(content.equals("Exit")) {
							socket.close();
							break;
						}
				}
				System.out.println("Connection Closed!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		new Thread(r2).start();				//Start the thread
	}

	//To start reading
	public void initiateReading() {
		// TODO Auto-generated method stub
		// thread that keeps providing us the data it is reading
		Runnable r1=()->{
			System.out.println("Initiated Reader...");
			try{
				while(true) {
				String message;
					message = br.readLine();
					if(message.equals("Exit")) {
						System.out.println("Server has terminated the chat!");
						this.setLocationRelativeTo(null);	
						JOptionPane.showMessageDialog(this, "Server terminated the chat!");
						messageInput.setEnabled(false);
						socket.close();
						dispose();		//close main window
						break;
					}
					
//					System.out.println("Server: "+message);
					messageArea.append("  Server : "+message+"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("Connection Closed!");
			}
		};
		new Thread(r1).start(); 		//Start the thread
	}

	
	public static void main(String[] args) {
		System.out.println("This is Client!");
		new Client();
	}
}
