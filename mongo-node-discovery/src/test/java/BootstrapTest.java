import flipkart.mongo.node.discovery.bootstrap.ReplicatorBootstrap;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class BootstrapTest {

    public static void main(String[] args) {

        ReplicatorBootstrap bootstrap = new ReplicatorBootstrap("w3-cart-svc10.nm.flipkart.com", 27200);
        bootstrap.initialize();
    }
}
