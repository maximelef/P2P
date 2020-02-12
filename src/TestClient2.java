import java.io.IOException;
import java.net.Socket;

public class TestClient2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ClientTCP client = new ClientTCP (new Socket ("localhost", 12355));
		client.lancerConnexion();
		
		
		
	}

}