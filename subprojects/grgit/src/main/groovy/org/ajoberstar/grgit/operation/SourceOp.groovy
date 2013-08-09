package org.ajoberstar.grgit.operation

abstract class SourceOp {
	private Repository repo

	Iterable<String> includes
	Iterable<String> excludes

	protected SourceOp(Repository repo) {
		this.repo = repo
	}


}
