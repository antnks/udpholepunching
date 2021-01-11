import java.awt.List;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class server
{
	public static void main(String args[]) throws Exception
	{
		boolean newClient;
		DatagramSocket socket = new DatagramSocket(5666);
		ArrayList<clientinfo> clients = new ArrayList<clientinfo>();
		System.out.println("Server is started!");

		while (true)
		{
			newClient = true;
			byte[] receiveData = new byte[1024];

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);

			System.out.println("Server received a packet!");

			for (int i=0; i < clients.size(); i++)
			{
				clientinfo client = clients.get(i);
				if(client.address.equals(receivePacket.getAddress()) && client.port == receivePacket.getPort())
				{
					newClient = false;
					break;
				}
			}

			if(newClient)
				clients.add(new clientinfo(receivePacket.getAddress(), receivePacket.getPort()));

			Thread thread = new Thread(new serverresponder(socket, receivePacket, clients));
			thread.start(); 
		}
	}
}

