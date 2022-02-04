package it.uniroma2.dicii.isw2.deliverable1;

import it.uniroma2.dicii.isw2.deliverable1.entities.DeliverableOneOutput;
import it.uniroma2.dicii.isw2.deliverable1.entities.Project;
import it.uniroma2.dicii.isw2.deliverable1.control.GitHubMiddleware;
import it.uniroma2.dicii.isw2.deliverable1.utils.LoggerInst;
import it.uniroma2.dicii.isw2.deliverable1.entities.Commit;
import it.uniroma2.dicii.isw2.deliverable1.entities.Ticket;
import it.uniroma2.dicii.isw2.deliverable1.io.CSVExporterPrinter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Central "control" class, orchestrating all the operations proper of the deliverable I.
 */
public class ProjectAnalyzer {
    private static Logger LOG = LoggerInst.getSingletonInstance();
    private final Project p;

    public ProjectAnalyzer(Project p) {
        this.p = p;
    }


    /**
     * The main program workflow.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        LOG.info("Running analysis for " + p.getName() + ".");
        initializeProjectData();
        p.initializeBugList();
        p.setTicketList(arrangeFixTimestamp(p.getTicketList()));
        groupAndExport(p.getTicketList());
        LOG.info("Finished.");
    }

    /**
     * Assign each ticket a fix date based on related commits, and sort them based on it.
     *
     * @param tickets ticket list
     * @return ticket list with fix dates
     */
    private List<Ticket> arrangeFixTimestamp(List<Ticket> tickets) {
        int count = 0;
        for (Ticket t : tickets) {
            List<Commit> relatedCommits = t.getCommitList();
            if (relatedCommits != null) {
                count += t.getCommitList().size();
                Commit first = relatedCommits.get(0);
                Date last = first.getDate();
                for (Integer j = 0; j < relatedCommits.size(); j++) {
                    if (relatedCommits.get(j).getDate() != null) {
                        if (relatedCommits.get(j).getDate().after(last)) {
                            last = relatedCommits.get(j).getDate();
                        }
                    }
                }
                t.setFixTimestamp(last);
            } else {
                LOG.fine("- Skipped ticket " + t.getID() + ": no commit assigned.");
                t.setFixTimestamp(new Date(0));
            }
        }
        CSVExporterPrinter.convertAndExport(tickets, "/output/" + p.getName() + "/inspection/tickets.csv");

        // Sort by date
        Collections.sort(tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket o1, Ticket o2) {
                return o1.getFixTimestamp().compareTo(o2.getFixTimestamp());
            }
        });
        return tickets;
    }

    /**
     * Group tickets by month and year of fix and export the dataset.
     *
     * @param tickets ticket list
     */
    public void groupAndExport(List<Ticket> tickets) {
        List<DeliverableOneOutput> ret = new ArrayList();
        Format f = new SimpleDateFormat("MMM yyyy");
        for (Ticket t : tickets) {
            boolean found = false;
            for (DeliverableOneOutput item : ret) {
                if ((item.getDate().getYear()) == (t.getFixTimestamp().getYear())
                        && item.getDate().getMonth() == t.getFixTimestamp().getMonth()) {
                    found = true;
                    item.setOccurrencies(item.getOccurrencies() + 1);
                    break;
                }
            }
            if (!found) {
                ret.add(new DeliverableOneOutput(t.getFixTimestamp(), 1));
            }
        }
        CSVExporterPrinter.convertAndExport(ret, "/output/" + p.getName() + "/output.csv");
    }

    /**
     * Data setup: generate working copy and fetch commit and version list for the project.
     */
    private void initializeProjectData() {
        p.setWorkingCopy(GitHubMiddleware.createWorkingCopy(p.getName(), p.getGitHubURL(), p.getGitHubVersion()));
        p.setCommitList(GitHubMiddleware.extractCommits(p.getName(), p.getWorkingCopy()));
    }
}
