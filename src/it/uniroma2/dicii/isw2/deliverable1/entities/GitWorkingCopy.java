package it.uniroma2.dicii.isw2.deliverable1.entities;

import org.eclipse.jgit.api.Git;

/**
 * A single Git working copy.
 */
public class GitWorkingCopy {
    private String path;
    private Git git;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Git getGit() {
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }


}
