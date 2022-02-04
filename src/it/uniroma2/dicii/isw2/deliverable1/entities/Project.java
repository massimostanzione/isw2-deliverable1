package it.uniroma2.dicii.isw2.deliverable1.entities;

import it.uniroma2.dicii.isw2.deliverable1.control.TicketBugHandler;

import java.util.List;

/**
 * A project that is being analyzed.
 */
public class Project {
    private String name;
    private String gitHubURL;
    private final String gitHubVersion;
    private List<Ticket> ticketList;
    private GitWorkingCopy workingCopy;

    private List<Commit> commitList;

    public Project(String name, String gitHubURL, String gitHubVersion) {
        super();
        this.name = name;
        this.gitHubURL = gitHubURL;
        this.gitHubVersion = gitHubVersion;
    }

    public void initializeBugList() {
        this.ticketList = TicketBugHandler.fetchProjectTickets(this.name, this.commitList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGitHubURL() {
        return gitHubURL;
    }

    public void setGitHubURL(String gitHubURL) {
        this.gitHubURL = gitHubURL;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public GitWorkingCopy getWorkingCopy() {
        return workingCopy;
    }

    public void setWorkingCopy(GitWorkingCopy workingCopy) {
        this.workingCopy = workingCopy;
    }

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> commitList) {
        this.commitList = commitList;
    }

    public String getGitHubVersion() {
        return this.gitHubVersion;
    }
}
