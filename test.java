import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class test
{
	public static void main(String args[]) throws Exception
	{
		if (args.length < 3)
		{
			System.out.println("Usage: java test srcPort peerAddr peerPORT");
			return;
		}
		int srcPort = Integer.valueOf(args[0]);
		String peerAddr = args[1];
		int peerPort = Integer.valueOf(args[2]);
		String payload = "aaaaaaaaaaaaa";
		
		System.out.println("Binding to " + srcPort);
		DatagramSocket sock = new DatagramSocket(srcPort);
		InetAddress dst = InetAddress.getByName(peerAddr);
		
		while(true)
		{
			DatagramPacket pack = new DatagramPacket(payload.getBytes(), payload.length(), dst, peerPort);
			sock.send(pack);
			Thread.sleep(1000);
		}

	}
}
