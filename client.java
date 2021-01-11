import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;


public class client
{
	public static void main(String args[]) throws Exception
	{
		if (args.length < 3)
		{
			System.out.println("Usage: java client serverIP serverPORT peerPORT");
			return;
		}
		String serverAddr = args[0];
		int serverPort = Integer.valueOf(args[1]);
		int peerPort = Integer.valueOf(args[2]);

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket(peerPort);
		InetAddress IPAddress = InetAddress.getByName(serverAddr);
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		String sentence = "";
		
		System.out.println("Client is started! Port: " + clientSocket.getLocalPort());
		DatagramPacket sendPacket = null;
		DatagramPacket receivePacket = null;

		sentence = "<Hi!>";
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
		clientSocket.send(sendPacket);

		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("Server says " + modifiedSentence);
		ArrayList<clientinfo> clients = null;
		clientinfo client = null;

		do
		{
			sentence = inFromUser.readLine();
			sendData = sentence.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
			clientSocket.send(sendPacket);

			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);

			ByteArrayInputStream bis = new ByteArrayInputStream(receiveData);
			ObjectInput in = new ObjectInputStream(bis);

			clients = (ArrayList<clientinfo>) in.readObject(); 

			for (int i=0; i < clients.size(); i++)
			{
				client = clients.get(i);
				System.out.println("Your partner: " + client.address + ":" + client.port);
			}

			bis.close();
			in.close();

		} while(clients.size() < 1);

		clientsender sender = new clientsender(clientSocket, client.address, client.port, serverPort);

		Thread threadSend = new Thread(sender);
		threadSend.start(); 

		while(true)
		{
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);

			if(receivePacket.getPort() != clientSocket.getLocalPort())
			{
				sender.address = receivePacket.getAddress();
				sender.port = receivePacket.getPort();
				String reply = new String(receivePacket.getData(), 0 ,receivePacket.getLength());
				System.out.println(receivePacket.getAddress().getHostAddress() + ":" + receivePacket.getPort() + ": " + reply);
				//System.out.println(InetAddress.getLocalHost().getHostAddress() + ":" + clientSocket.getLocalPort());
			}
		}
	}
}
