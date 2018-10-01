package buildit.demo.scrape;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
//Higher level abstraction over a phaser to wait until all pages parsed
class Scheduler {
    private final Phaser phaser = new Phaser(1);
    private final ExecutorService executorService;

    public Scheduler schedule(Consumer<Scheduler> task) {
        increment();
        executorService.submit(() -> {
            try {
                task.accept(this);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                decrement();
            }
        });
        return this;
    }

    private void increment() {
        phaser.register();
    }

    private void decrement() {
        phaser.arriveAndDeregister();
    }

    public void waitForCompletionUpTo(int max, TimeUnit unit) {
        phaser.arrive();
        try {
            phaser.awaitAdvanceInterruptibly(0, max, unit);
        } catch (TimeoutException t) {
            log.error("This is a big site, timing out after waiting around for " + max + " " + unit + " to save a few trees");
            executorService.shutdown();
        } catch (InterruptedException e) {
            log.error("Interuptted");
        }

    }
}
