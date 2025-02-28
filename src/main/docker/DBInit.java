package org.acme.reactive.crud;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class DBInit {

    private final PgPool client;
    private final boolean schemaCreate;

    public DBInit(PgPool client, @ConfigProperty(name = "myapp.schema.create", defaultValue = "true") boolean schemaCreate) {
        this.client = client;
        this.schemaCreate = schemaCreate;
    }

    void onStart(@Observes StartupEvent ev) {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS users").execute()
                .flatMap(r -> client.query("CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute())
                .flatMap(r -> client.query("INSERT INTO users (name) VALUES ('Kiwi')").execute())
                .flatMap(r -> client.query("INSERT INTO users (name) VALUES ('Durian')").execute())
                .flatMap(r -> client.query("INSERT INTO users (name) VALUES ('Pomelo')").execute())
                .flatMap(r -> client.query("INSERT INTO users (name) VALUES ('Lychee')").execute())
                .await().indefinitely();
    }
}
