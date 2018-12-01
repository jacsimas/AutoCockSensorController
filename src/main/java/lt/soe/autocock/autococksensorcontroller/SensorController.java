package lt.soe.autocock.autococksensorcontroller;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class SensorController {

    private static final byte TURN_OFF_WEIGHT_SENSOR = 0;
    private static final byte TURN_ON_WEIGHT_SENSOR = 1;
    private static final byte TURN_OFF_LIQUID_SENSOR = 2;
    private static final byte TURN_ON_LIQUID_SENSOR = 3;

    private static final byte[] REQUEST_FAILED = {0};
    private static final byte[] REQUEST_SUCCEEDED = {1};

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            socket.bind("tcp://*:5555");

            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv(0);
                byte requestCode = reply[0];

                switch (requestCode) {
                    case TURN_OFF_WEIGHT_SENSOR:
                        System.out.println("weight sensor switched off");
                        socket.send(REQUEST_SUCCEEDED, 0);
                        break;
                    case TURN_ON_WEIGHT_SENSOR:
                        System.out.println("weight sensor switched on");
                        socket.send(REQUEST_SUCCEEDED, 0);
                        break;
                    case TURN_OFF_LIQUID_SENSOR:
                        System.out.println("liquid sensor switched off");
                        socket.send(REQUEST_SUCCEEDED, 0);
                        break;
                    case TURN_ON_LIQUID_SENSOR:
                        System.out.println("liquid sensor switched on");
                        socket.send(REQUEST_SUCCEEDED, 0);
                        break;
                    default:
                        throw new IllegalStateException("invalid request code " + requestCode);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    socket.send(REQUEST_FAILED, 0);
                    e.printStackTrace();
                }
            }
        }
    }

}
