package com.oreid.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class PromiseVerticle extends AbstractVerticle {

  private static final boolean OUTCOME = true;

  @Override
  public void start(Promise<Void> startPromise) {
    startPromise.complete();

    // Create a promise
    Promise<String> promise = Promise.promise();

    vertx.setTimer(5000, id -> {
      if (OUTCOME) {
        promise.complete("Votes counted!");
      } else {
        promise.fail(new RuntimeException("Stop the count!"));
      }
    });

    // These handlers are called when the promise completes or fails
    promise.future()
        .recover(mapper -> Future.succeededFuture("Recovered!"))
//        .map(String::toUpperCase)
//        .compose(this::doSomeMoreStuff) // .compose and .flatMap are synonymous
        .onSuccess(System.out::println)
        .onFailure(System.err::println);
  }

  private Future<String> doSomeMoreStuff(String value) {
    // We also do asynchronous stuff here
    Promise< String> promise = Promise.promise();

    vertx.setTimer(3000, h -> {
      promise.complete("\uD83D\uDC26 " + value + " \uD83D\uDC26");
    });

    return promise.future();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }

}
