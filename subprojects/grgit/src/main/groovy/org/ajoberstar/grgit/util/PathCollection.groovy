package org.ajoberstar.grgit.util

class PathCollection {
	Path rootDir
	Iterable<String> includes
	Iterable<String> excludes

	Set<Path> getPaths() {
		List<PatternMatcher> includeMatchers = getMatchers(includes)
		List<PatternMatcher> excludeMatchers = getMatchers(excludes)
		FileVisitor visitor = new FilePatternVistor(includeMatchers, excludeMatchers)
		Files.walkFileTree(rootDir, visitor)
		return visitor.paths
	}

	private List<PathMatcher> getMatchers(Iterable<? extends String> patterns) {
		return patterns.collect { pattern ->
			rootDir.fileSystem.getPathMatcher("glob:${pattern}")
		}
	}

	private static FilePatternVistor extends SimpleFileVisitor<Path> {
		private List<PathMatcher> includes
		private List<PathMatcher> excludes

		final Set<Path> paths = []

		FilePatternVistor(List<PathMatcher> includes, List<PathMatcher> excludes) {
			this.includes = includes
			this.excludes = excludes
		}

		FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			def included = includes ? includes.any { it.matches(file) } : true
			def excluded = excludes ? excludes.any { it.matches(file) } : false
			if (included && !excluded) {
				paths << file
			}
			return FileVisitResult.CONTINUE
		}
	}
}
