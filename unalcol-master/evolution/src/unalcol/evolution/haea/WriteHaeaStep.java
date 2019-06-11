package unalcol.evolution.haea;

import java.io.IOException;
import java.io.Writer;

import unalcol.io.Write;
import unalcol.services.MicroService;

public class WriteHaeaStep<T,R> extends MicroService<HaeaStep<T,R>> implements Write<HaeaStep<T,R>> {
	@Override
	public void write(Writer writer) throws IOException {
		Write.to(caller().operators(), writer);
	}
}