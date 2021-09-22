package com.oreid.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.createHttpServer()
        .requestHandler(handler -> {})
        .listen(8888, http -> {
          if (http.succeeded()) {
            startPromise.complete();

//            vertx.deployVerticle("com.oreid.async.PromiseVerticle");
//            vertx.deployVerticle("com.oreid.async.ComposablePromiseVerticle");
          } else {
            startPromise.fail(http.cause());
          }
        });
  }
}
