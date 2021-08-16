package com.petershino.projectreactorplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoTests {

    @Test
    public void basicMono() {
        Mono.just("A")
                .log()
                .subscribe();
    }

    @Test
    public void monoWithConsumer() {
        Mono.just("A")
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void monoWithDoOn() {
        Mono.just("DATA")
                .doOnSubscribe(s -> System.out.println("Subscribed: " + s))
                .doOnRequest(r -> System.out.println("Requested: " + r))
                .doOnSuccess(c -> System.out.println("Completed: " + c))
                .subscribe(System.out::println);
    }

    @Test
    public void emptyMono() {
        Mono.empty()
                .log()
                .subscribe(System.out::println);
    }

    @Test
    void emptyCompleteConsumerMono() {
        Mono.empty()
                .log()
                .subscribe(System.out::println,
                        null,
                        () -> System.out.println("Done")
                );
    }

    @Test
    void errorRuntimeExceptionMono() {
        Mono.error(new RuntimeException())
                .log()
                .subscribe();
    }

    @Test
    void errorExceptionMono() {
        Mono.error(new Exception())
                .log()
                .subscribe();
    }

    @Test
    void errorConsumerMono() {
        Mono.error(new Exception())
                .log()
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e)
                );
    }

    @Test
    void errorDoOnErrorMono() {
        Mono.error(new Exception())
                .doOnError(e -> System.out.println("Error: " + e))
                .log()
                .subscribe();
    }

    @Test
    void errorOnErrorResumeMono() {
        Mono.just("A")
                .log()
                .then(Mono.error(new Exception()))
                .onErrorResume(e -> {
                    System.out.println("Caught: " + e);
                    return Mono.just("B");
                })
                .log()
                .subscribe(System.out::println);
    }

    @Test
    void errorOnErrorReturnMono() {
        Mono.error(new Exception())
                .onErrorReturn("B")
                .log()
                .subscribe();
    }
}
