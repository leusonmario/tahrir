package tahrir.util.tools;

import tahrir.util.crypto.TrCrypto;
import tahrir.network.PhysicalNetworkLocation;
import tahrir.network.RemoteNodeAddress;
import tahrir.transport.rpc.TrPeerManager;
import tahrir.transport.messaging.udpV1.UdpNetworkLocation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

public class Generic {

    public static Integer randomInt(Integer i) {
        return new Random().nextInt(i);
    }

    public static Integer genericPort() {
        return randomInt(60000);
    }

    public static Integer genericSessionId() {
        return randomInt(1000);
    }

    public static Integer genericTopologyLocation() {
        return new Random().nextInt();
    }

    public static InetAddress genericInetAddress() {
        try {
            return InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static RemoteNodeAddress genericRemoteNodeAddress() {
        final RSAPublicKey pubKey = TrCrypto.createRsaKeyPair().a;
        final PhysicalNetworkLocation location = new UdpNetworkLocation(genericInetAddress(), genericPort());
        return new RemoteNodeAddress(location, pubKey);
    }

    public static TrPeerManager.TopologyLocationInfo genericTopologyLocationInfo() {
        return new TrPeerManager.TopologyLocationInfo(null);
    }
}