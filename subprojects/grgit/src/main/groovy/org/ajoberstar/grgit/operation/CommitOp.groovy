package org.ajoberstar.grgit.operation

import org.ajoberstar.gradle.util.ObjectUtil
import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.lib.PersonIdent
import org.gradle.api.GradleException
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.util.ConfigureUtil

class CommitOp {
	private Repository repo

	Object message = null
	Object reflogComment = null

	boolean amend = false
	boolean commitAll = false
	boolean insertChangeId = false

	Person committer = null
	Person author = null

	CommitOp(Repository repo) {
		this.repo = repo
	}

	Commit call() {
		CommitCommand cmd = repo.git.commit()
		cmd.message = message
		cmd.reflogComment = reflogComment
		cmd.all = commitAll
		cmd.amend = amend
		cmd.insertChangeId = insertChangeId
		if (committer) {
			cmd.setCommitter(committer.name, committer.email)
		}
		if (author) {
			cmd.setAuthor(author.name, author.email)
		}
		try {
			cmd.call()
		} catch (GitAPIException e) {
			throw new GrGitException('Problem committing changes.', e)
		}
	}
}
