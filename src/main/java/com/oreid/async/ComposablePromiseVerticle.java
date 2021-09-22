package com.oreid.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ComposablePromiseVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    startPromise.complete();

    // Create a promise
    boolean someBool = true;
    Promise<String> promise = Promise.promise();

    vertx.setTimer(5000, id -> {
      if (someBool) {
        promise.complete("Succeeded!");
      } else {
        promise.fail(new RuntimeException("Something went wrong..."));
      }
    });

    // Test .all
    CompositeFuture.all(doSuccessfully(1000), doUnsuccessfully(2000), doSuccessfully(3000))
        .onSuccess(h -> print("ALL: All were successful"))
        .onFailure(h -> print("ALL: At least one failed"));

    // Test .any
    CompositeFuture.any(doSuccessfully(1000), doUnsuccessfully(2000), doSuccessfully(3000))
        .onSuccess(h -> print("ANY: At least one was successful"))
        .onFailure(h -> print("ANY: All failed"));

    // Test .any
    CompositeFuture.join(doSuccessfully(1000), doUnsuccessfully(2000), doSuccessfully(3000))
        .onSuccess(h -> print("JOIN: All were successful"))
        .onFailure(h -> print("JOIN: At least one failed"));
  }

  private Future<String> doSuccessfully(long time) {
    Promise< String> promise = Promise.promise();
    vertx.setTimer(time, h -> {
      promise.complete();
    });
    return promise.future();
  }

  private Future<String> doUnsuccessfully(long time) {
    Promise< String> promise = Promise.promise();
    vertx.setTimer(time, h -> {
      promise.fail("Oopsie");
    });
    return promise.future();
  }

  private void print(String message) {
    System.out.printf("%s %s%n", message, getTime());
  }

  private String getTime() {
    return DateTimeFormatter.ofPattern("hh:mm:ss")
        .withZone(ZoneId.systemDefault())
        .format(Instant.now());
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }

}
