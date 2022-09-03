import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Server extends JFrame{
	
	private static final String KeyListener = null;
	ServerSocket server;
	Socket socket;
	BufferedReader br;		//For reading
	PrintWriter out;		//For writing
	
	//Declare GUI Components
	private JLabel heading = new JLabel("Server Side");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Work Sans", Font.PLAIN, 15);
	private Font head = new Font("Work Sans", Font.BOLD, 20);
	private Font msg_font = new Font("Work Sans", Font.PLAIN, 18);
	
	
	//Constructor of the Server
	public Server() {
		try{			
			server=new ServerSocket(7777);
			System.out.println("Server is ready to accept connection.");
			System.out.println("Waiting...");
			createGUI();
			socket=server.accept();
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//Byte data from Socket changed to character handled by BufferedReader
			out= new PrintWriter(socket.getOutputStream());
			
			handleEvents();
			initiateReading();
//			initiateWriting();
			
		} catch (Exception e) {
			//	e.printStackTrace();
			System.out.println("Connection Closed!");
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
		this.setTitle("Server Messenger");
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
		messageArea.setBackground(new Color(217, 226, 214));
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


	public void initiateWriting() {
		// TODO Auto-generated method stub
		// thread that receives data from user and sends it to client
		Runnable r2=()->{
			System.out.println("Initiated Writer...");
			try {
				while(!socket.isClosed()) {
						BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
						String content = br1.readLine();	//Data read from console sent to client
					
						out.println(content);
						out.flush();
						
						if(content.equals("Exit")) {
							socket.close();
							break;
						}
				}
				
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("Connection Closed!");
			}
		};
		new Thread(r2).start();				//Start the thread
	}

	public void initiateReading() {
		// TODO Auto-generated method stub
		// thread that keeps providing us the data it is reading
		Runnable r1=()->{
			System.out.println("Initiated Reader...");
			try {
				while(true) {
					String message;
						message = br.readLine();
						if(message.equals("Exit")) {
							System.out.print("Client has terminated the chat!");
							this.setLocationRelativeTo(null);	
							JOptionPane.showMessageDialog(this, "Client terminated the chat!");
							messageInput.setEnabled(false);
							socket.close();
							dispose();
							break;
						}
						
//						System.out.println("Client: "+message);
						messageArea.append("  Client : "+message+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		new Thread(r1).start(); 		//Start the thread
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("This is server.");
		new Server();
	}

}
