import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	private static final double LOSS_RATE = 0.3;
	private static final int AVERAGE_DELAY = 100;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("porta");
			return;
		}
		int port = Integer.parseInt(args[0]);
		Random random = new Random();
		DatagramSocket socket = new DatagramSocket(port);
		while (true) {
			DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
			socket.receive(request);
			printData(request);

			if (random.nextDouble() < LOSS_RATE) {
				System.out.println("Resposta não enviada");
				continue;
			}

			Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));
			InetAddress clientHost = request.getAddress();
			int clientPort = request.getPort();
			byte[] buf = request.getData();
			DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
			socket.send(reply);
			System.out.println("Resposta enviada.");
		}
	}

	private static void printData(DatagramPacket request) throws Exception {
		byte[] buf = request.getData();
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
		System.out.println("Recebido " + request.getAddress().getHostAddress() + ": " + new String(line));
	}

}
