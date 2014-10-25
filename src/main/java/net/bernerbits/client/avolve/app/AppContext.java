package net.bernerbits.client.avolve.app;

import net.bernerbits.client.avolve.model.Bucket;

public abstract class AppContext {

	protected final Bucket bucket;

	protected AppContext(Bucket bucket) {
		this.bucket = bucket;
	}

}
