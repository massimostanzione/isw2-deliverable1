package it.uniroma2.dicii.isw2.deliverable1.control;

import it.uniroma2.dicii.isw2.deliverable1.entities.Commit;
import it.uniroma2.dicii.isw2.deliverable1.entities.GitWorkingCopy;
import it.uniroma2.dicii.isw2.deliverable1.io.CSVExporterPrinter;
import it.uniroma2.dicii.isw2.deliverable1.utils.CollectionSorter;
import it.uniroma2.dicii.isw2.deliverable1.utils.LoggerInst;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class as a "middleware layer" with the GitHub environment, to handle and
 * manage working copies of the analyzed projects.
 */
public class GitHubMiddleware {
    public static final String DEFAULT_GIT_WORKINGCOPY_PATH = "/tmp/isw2-deliverable1";
    private static GitWorkingCopy ret;

    private static Logger log = LoggerInst.getSingletonInstance();

    private GitHubMiddleware() {
    }

    /**
     * Download a project and instantiate local working copy, if it does not already exists.
     *
     * @param projName project name
     * @param remote   URL of the project
     * @return reference to the project's local working copy
     */
    public static GitWorkingCopy createWorkingCopy(String projName, String remote, String version) {
        ret = new GitWorkingCopy();
        log.info(() -> "Checking for working copy...");
        try {
            if (!Files.exists(Path.of(DEFAULT_GIT_WORKINGCOPY_PATH + "/" + projName))) {
                log.info(() -> "- Creating working copy. It may take a while, please wait...");
                Git.cloneRepository()
                        .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.err)))
                        .setURI(remote).setBranch(version).setDirectory(new File(DEFAULT_GIT_WORKINGCOPY_PATH + "/" + projName)).call();
                log.info(() -> "- Working copy is ready.");
            } else {
                log.info(() -> "- Working copy already exists locally at " + DEFAULT_GIT_WORKINGCOPY_PATH + "/" + projName + ".");
            }
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repo = builder
                    .setGitDir(new File(GitHubMiddleware.DEFAULT_GIT_WORKINGCOPY_PATH + "/" + projName + "/.git"))
                    .setMustExist(true).build();
            Git git = new Git(repo);
            ret.setGit(git);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Extract raw commits from the working copy, and convert them in a more manageable
     * <code>Commit</code> form.
     *
     * @param projName project name
     * @return list of "non-raw" commits related to the project
     */
    public static List<Commit> extractCommits(String projName) {
        log.info(() -> "Extracting commits...");
        List<Commit> commits = new ArrayList<>();
        Iterator<RevCommit> iter = null;
        // Extract raw RevCommit iterator
        try {
            iter = ret.getGit().log().all().call().iterator();
        } catch (RevisionSyntaxException | GitAPIException | IOException e) {
            e.printStackTrace();
        }
        // Iterate through raw commits and convert them
        try {
            while (iter.hasNext()) {
                RevCommit iteratedCommit = iter.next();// ?
                if (iteratedCommit.getParentCount() > 0) {
                    Commit c = new Commit(iteratedCommit);
                    commits.add(c);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // Sort commit by their date
        try {
            CollectionSorter.sort(commits, Commit.class.getDeclaredMethod("getDate"));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        CSVExporterPrinter.convertAndExport(commits, "/output/" + projName + "/inspection/commits.csv");
        log.info(() -> "- " + commits.size() + " commits found.");
        return commits;
    }
}
