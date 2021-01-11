import java.io.Serializable;
import java.net.InetAddress;

public class clientinfo implements Serializable
{
	public InetAddress address;
	public Integer port;

	public clientinfo(InetAddress address, Integer port)
	{
		this.address = address;
		this.port = port;
	}
}
