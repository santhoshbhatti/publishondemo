package publishondemo;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class PublishOnDemo {
	
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.<Integer>create(fluxSync -> {
			
			for (int k = 0; k < 10; k++) {
				printThreadName("pipeline creation: "+k);
				fluxSync.next(k);
				sleepSeconds(1);
			}
			fluxSync.complete();
		})
				.doFirst(() -> printThreadName("first1"))
				.doOnNext(i -> printThreadName("nextElement1: "+i));


		
		flux.doFirst(() -> printThreadName("first2"))
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(i -> printThreadName("nextElement2: "+i))
				.publishOn(Schedulers.newParallel("pubon2"))
				.doFirst(() -> printThreadName("first3"))
				.subscribe(i -> printThreadName("subscriber 1: "+i));
	}
	
	public static void printThreadName(String prefix) {
		System.out.println(prefix + " : " + Thread.currentThread().getName());
	}
	
	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
