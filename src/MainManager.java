import java.io.IOException;
import java.net.UnknownHostException;

public class MainManager {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		ManagerP2P m = new ManagerP2P(12333);

		m.traitement();
	}

}
