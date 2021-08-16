package com.petershino.projectreactorplayground;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class FluxTest {
    @Test
    void firstFlux() {
        Flux.just("A", "B", "C")
                .log()
                .subscribe(System.out::println, e -> System.out.println("error occurred: " +e));
    }

    @Test
    void firstError() {
        Flux.just("A", "B", "C")
                .doOnRequest(l -> System.out.println("doOnRequest: " + l))
                .log()
                .then(Mono.error(new RuntimeException("runtime error")))
                .log()
                .subscribe(System.out::println,
                        e -> System.out.println("error occurred: " +e),
                        () -> System.out.println("onComplete"),
                        s -> s.request(1)
                );
    }
    @Test
    public void fluxFromIterable() {
        Flux.fromIterable(Arrays.asList("a", "b", "c"))
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromRange() {
        Flux.range(0,5)
                .log()
                .take(2)
                .subscribe(System.out::println);
    }

    @Test
    public void fluxFromRange2() {
        Flux.range(10,5)
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void fluxFromInterval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(2)
                .subscribe(); // running in reactor's parallel thread
        Thread.sleep(5000); // main
    }

    @Test
    public void fluxRequest() {
        Flux.range(1, 5)
                .log()
                .subscribe(null, null,
                        null, s -> s.request(3)
                );
    }

    @Test
    public void fluxCustomSubscriber() {
        Flux.range(1, 10)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    private int elementsToProcess = 3;
                    private int counter = 0;

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed");
                        this.request(elementsToProcess);
                        super.hookOnSubscribe(subscription);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        counter += 1;
                        if (counter == elementsToProcess) {
                            counter = 0;

                            Random r = new Random();
                            elementsToProcess = r.ints(1, 4)
                                    .findFirst().getAsInt();
                            request(elementsToProcess);
                        }
                    }
                });
    }

    @Test
    public void fluxLimitRate() {
        Flux.range(1, 5)
                .log()
                .limitRate(3)
                .subscribe();
    }
}
