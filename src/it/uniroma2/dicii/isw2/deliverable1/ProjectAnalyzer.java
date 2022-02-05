package it.uniroma2.dicii.isw2.deliverable1;

import it.uniroma2.dicii.isw2.deliverable1.control.GitHubMiddleware;
import it.uniroma2.dicii.isw2.deliverable1.entities.Commit;
import it.uniroma2.dicii.isw2.deliverable1.entities.DeliverableOneOutput;
import it.uniroma2.dicii.isw2.deliverable1.entities.Project;
import it.uniroma2.dicii.isw2.deliverable1.entities.Ticket;
import it.uniroma2.dicii.isw2.deliverable1.io.CSVExporterPrinter;
import it.uniroma2.dicii.isw2.deliverable1.utils.CollectionSorter;
import it.uniroma2.dicii.isw2.deliverable1.utils.LoggerInst;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Central "control" class, orchestrating all the operations proper of the deliverable I.
 */
public class ProjectAnalyzer {
    private static Logger log = LoggerInst.getSingletonInstance();
    private final Project p;

    public ProjectAnalyzer(Project p) {
        this.p = p;
    }


    /**
     * The main program workflow.
     *
     * @throws Exception
     */
    public void run() {
        log.info(() -> "Running analysis for " + p.getName() + ".");
        initializeProjectData();
        p.initializeBugList();
        p.setTicketList(arrangeFixTimestamp(p.getTicketList()));
        groupAndExport(p.getTicketList());
        log.info(() -> "Finished.");
    }

    /**
     * Assign each ticket a fix date based on related commits, and sort them based on it.
     *
     * @param tickets ticket list
     * @return ticket list with fix dates
     */
    private List<Ticket> arrangeFixTimestamp(List<Ticket> tickets) {
        Integer count = 0;
        for (Ticket t : tickets) {
            List<Commit> relatedCommits = t.getCommitList();
            if (relatedCommits != null) {
                count = count + t.getCommitList().size();
                Commit first = relatedCommits.get(0);
                Date last = first.getDate();
                for (Integer j = 0; j < relatedCommits.size(); j++) {
                    if (relatedCommits.get(j).getDate() != null && relatedCommits.get(j).getDate().after(last)) {
                        last = relatedCommits.get(j).getDate();
                    }
                }
                t.setFixTimestamp(last);
            } else {
                log.fine("- Skipped ticket " + t.getId() + ": no commit assigned.");
                t.setFixTimestamp(new Date(0));
            }
        }
        CSVExporterPrinter.getSingletonInstance().convertAndExport(tickets, "/output/" + p.getName() + "/inspection/tickets.csv");
        // Sort by date
        try {
            CollectionSorter.sort(tickets, Ticket.class.getDeclaredMethod("getFixTimestamp"));
        } catch (NoSuchMethodException e) {
            log.severe(e.getMessage());
        }
        return tickets;
    }

    /**
     * Group tickets by month and year of fix and export the dataset.
     *
     * @param tickets ticket list
     */
    public void groupAndExport(List<Ticket> tickets) {
        Calendar cItem = Calendar.getInstance();
        Calendar cTicket = Calendar.getInstance();
        List<DeliverableOneOutput> ret = new ArrayList<>();
        for (Ticket t : tickets) {
            boolean found = false;
            for (DeliverableOneOutput item : ret) {
                cItem.setTime(item.getDate());
                cTicket.setTime(t.getFixTimestamp());
                if (cItem.get(Calendar.MONTH) == cTicket.get(Calendar.MONTH)
                        && cItem.get(Calendar.YEAR) == cTicket.get(Calendar.YEAR)) {
                    found = true;
                    item.setOccurrencies(item.getOccurrencies() + 1);
                    break;
                }
            }
            if (!found) {
                ret.add(new DeliverableOneOutput(t.getFixTimestamp(), 1));
            }
        }
        CSVExporterPrinter.getSingletonInstance().convertAndExport(ret, "/output/" + p.getName() + "/output.csv");
    }

    /**
     * Data setup: generate working copy and fetch commit and version list for the project.
     */
    private void initializeProjectData() {
        p.setWorkingCopy(GitHubMiddleware.createWorkingCopy(p.getName(), p.getGitHubURL(), p.getGitHubVersion()));
        p.setCommitList(GitHubMiddleware.extractCommits(p.getName()));
    }
}
