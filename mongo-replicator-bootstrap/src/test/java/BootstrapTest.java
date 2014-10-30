import flipkart.mongo.replicator.bootstrap.ReplicatorBootstrap;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class BootstrapTest {

    public static void main(String[] args) throws Exception {
        ReplicatorBootstrap bootstrap = new ReplicatorBootstrap("w3-cart-svc10.nm.flipkart.com", 27200, null);
        bootstrap.initialize();
    }
}
