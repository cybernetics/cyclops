package com.aol.cyclops.reactor;

import static com.aol.cyclops.util.stream.Streamable.fromStream;
import static java.util.stream.Stream.concat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;

import com.aol.cyclops.control.FutureW;
import com.aol.cyclops.control.ReactiveSeq;
import com.aol.cyclops.control.monads.transformers.ListT;
import com.aol.cyclops.control.monads.transformers.seq.ListTSeq;
import com.aol.cyclops.data.collections.extensions.standard.ListX;
import com.aol.cyclops.data.collections.extensions.standard.SetX;
import com.aol.cyclops.reactor.Reactor.ForFlux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorTest {

	@Test
	public void amb(){
		

		Stream<List<Integer>> stream = Stream.of(Arrays.asList(1,2,3),Arrays.asList(10,20,30));
		
		Stream.map(list->list.stream().reduce(0,(a,b)->a+b).collect(Collectors.toList()));
		
		SetX.fromPublisher(Flux.amb(ReactiveSeq.of(1,2,3),Flux.just(10,20,30))); 
	}
	@Test
	public void fluxEx(){
		
		ListX.of(1,2)
		 	 .flatMapPublisher(i->Flux.range(i, 4-i))
		     .forEach(System.out::println);
	}
	@Test
	public void presentation(){
		
		fromStream(Stream.of(1,2,3));
		concat(Stream.of(1,2,3), Stream.of(4,5,6));
		
		ListX.of(1,2)
			 .flatMapPublisher(i->Flux.range(i, 4))
			 .forEach(System.out::println);
		
		
		
		
		Flux.just(1,2,3,4)
			.flatMap(i->ReactiveSeq.range(i, 10))
			.consume(System.out::println);
		
		
		
		
		FutureW.ofResult(1)
			   .map(i->i*2);
		
		//FutureW[2]
		
		
		
		
		Flux<Data> stream = Flux.amb(Flux.just(10,20,30,50).map(this::loadRemote), 
										Flux.just(10,20,30,50).map(this::findRemote));
		
		SetX<Data> data = SetX.fromPublisher(stream);
		
		
		
		data.printOut();
		
		Flux.just(1,2,3,4)
			.flatMap(i->Flux.range(i, 10))
			.consume(System.out::println);
		
		
		
	}
	
	class Data{}
	public Data loadRemote(int in){
		return null;
	}
	public Data findRemote(int in){
		return null;
	}
	
	
	@Test
	public void flux() {
		assertThat(Reactor.flux(Flux.just(1,2,3)).toListX(),equalTo(ListX.of(1,2,3)));
	}
	@Test
	public void mono() {
		assertThat(Reactor.mono(Mono.just(1)).toListX(),equalTo(ListX.of(1)));
	}
	@Test
	public void fluxT() {
		System.out.println(Reactor.fluxT(Flux.just(Flux.just(1,2,3),Flux.just(10,20,30))).map(i->i*3));
		assertThat(Reactor.fluxT(Flux.just(Flux.just(1,2,3),Flux.just(10,20,30))).toListX(),equalTo(ListX.of(1,2,3,10,20,30)));
	}
	@Test
	public void monoT() {
		
		Reactor.monoT(Flux.just(Mono.just(1),Mono.just(10)))
			  .map(i->i*2);
		
		//FutureWTSeq[Flux[FutureW[1],FutureW[20]]
		
		
		
		assertThat(Reactor.monoT(Flux.just(Mono.just(1),Mono.just(10))).toListX(),equalTo(ListX.of(1,10)));
	}

	@Test
	public void fluxComp(){
		
		 // = Stream.of(Arrays.asList(1,2,3),Arrays.asList(10,20,30));
		
		
		Stream<List<Integer>> nestedLists;
		/**
		Stream<List<Integer>> doubled =  nestedLists.map(list->list.stream()
																   .map(i->i*2)
																   .collect(Collectors.toList()));
		
		
		
		**/
		
		//Stream<Integer> summed = nestedLists.map(list->list.stream().reduce(0,(a,b)->a+b));
		
		
		
		
		ListTSeq<Integer> listT = ListT.fromStream(nestedLists);
		// = listT.map(i->i*2);
		
		
		
		ListTSeq<Integer> doubled;
		
		
		Stream<ListX<Integer>> doubledStream = doubled.toNestedListX()
					 								  .stream();
		
		
		Stream<Integer>   summed = listT.reduce(0,(a,b)->a+b)
									    .stream();
		
		
		
		
		
		
		ForFlux.each2(Flux.range(1,10), i->Flux.range(i, 10), Tuple::tuple);
		
		
		
		
		
		Flux<Tuple2<Integer,Integer>> stream = ForFlux.each2(Flux.range(1,10), i->Flux.range(i, 10), Tuple::tuple);
		Flux<Integer> result = Reactor.ForFlux.each2(Flux.just(10,20),a->Flux.<Integer>just(a+10),(a,b)->a+b);
		assertThat(result.toList().get(),equalTo(ListX.of(30,50)));
	}
	@Test
	public void monoComp(){
		Mono<Integer> result = Reactor.ForMono.each2(Mono.just(10),a->Mono.<Integer>just(a+10),(a,b)->a+b);
		assertThat(result.get(),equalTo(30));
	}
}
