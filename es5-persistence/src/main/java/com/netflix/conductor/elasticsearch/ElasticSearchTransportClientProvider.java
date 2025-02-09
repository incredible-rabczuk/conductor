package com.netflix.conductor.elasticsearch;

import com.google.inject.ProvisionException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;

public class ElasticSearchTransportClientProvider implements Provider<Client> {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchTransportClientProvider.class);

    private final ElasticSearchConfiguration configuration;

    @Inject
    public ElasticSearchTransportClientProvider(ElasticSearchConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Client get() {

        Settings settings = Settings.builder()
                .put("cluster.name", configuration.getClusterName())
                .put("client.transport.sniff", configuration.getSniffingEnabled())
                .put("client.transport.ignore_cluster_name", configuration.getClusterNameIgnored())
                .build();

        TransportClient tc = new PreBuiltTransportClient(settings);

        List<URI> clusterAddresses = configuration.getURIs();

        if (clusterAddresses.isEmpty()) {
            logger.warn(ElasticSearchConfiguration.ELASTIC_SEARCH_URL_PROPERTY_NAME +
                    " is not set.  Indexing will remain DISABLED.");
        }
        for (URI hostAddress : clusterAddresses) {
            int port = Optional.ofNullable(hostAddress.getPort()).orElse(9200);
            try {
                tc.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostAddress.getHost()), port));
            } catch (UnknownHostException uhe){
                throw new ProvisionException("Invalid host" + hostAddress.getHost(), uhe);
            }
        }
        return tc;
    }
}
