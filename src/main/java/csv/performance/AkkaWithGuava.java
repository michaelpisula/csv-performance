package csv.performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Splitter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class AkkaWithGuava implements CsvReader {

	public class LineCountActor extends UntypedActor {

		int lines = 0;

		@Override
		public void onReceive(Object arg0) throws Exception {
			if (arg0 instanceof Iterable) {
				lines++;
			}
		}

	}

	public class CsvSplitter extends UntypedActor {

		@Override
		public void onReceive(Object arg0) throws Exception {
			if (arg0 instanceof String) {
				String line = (String) arg0;
				getSender().tell(Splitter.on(',').split(line), getSelf());

			}
		}

	}

	public class FileReaderActor extends UntypedActor {

		private ActorRef splitter = context().actorOf(
				Props.create(CsvSplitter.class).withDispatcher("dispatcher"));
		private ActorRef lineCounter = context().actorOf(
				Props.create(LineCountActor.class));

		@Override
		public void onReceive(Object arg0) throws Exception {

		}

	}

	private ActorSystem system = ActorSystem.create();

	private ActorRef reader = system.actorOf(Props
			.create(FileReaderActor.class));

	@Override
	public long processFile(String path) throws IOException {
		long lineCount = 0;
		try (BufferedReader in = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = in.readLine()) != null) {
				String[] fields = processLine(line);
				lineCount++;
			}
		}
		return lineCount;
	}

	@Override
	public String[] processLine(String line) {
		return line.split(",", -1);
	}
}