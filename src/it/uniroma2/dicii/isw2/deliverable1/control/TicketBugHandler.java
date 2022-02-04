package it.uniroma2.dicii.isw2.deliverable1.control;

import it.uniroma2.dicii.isw2.deliverable1.entities.Commit;
import it.uniroma2.dicii.isw2.deliverable1.entities.Ticket;
import it.uniroma2.dicii.isw2.deliverable1.entities.jql.JIRAQuery;
import it.uniroma2.dicii.isw2.deliverable1.entities.jql.JIRAQueryType;
import it.uniroma2.dicii.isw2.deliverable1.utils.LoggerInst;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static it.uniroma2.dicii.isw2.deliverable1.entities.jql.JQLQuery.*;


/**
 * Class for ticket and bug handling, mainly fetching them from projects.
 */
public class TicketBugHandler {
    private static final Logger log = LoggerInst.getSingletonInstance();

    private TicketBugHandler() {
    }

    /**
     * Fetch project tickets from JIRA, parse them and assign them their related commits.
     *
     * @param projName   project name
     * @param commitList commits of the project
     * @return list containing the tickets (with related commits) for the projects
     */
    public static List<Ticket> fetchProjectTickets(String projName, List<Commit> commitList) {
        log.info(() -> "Initializing ticket (and related commits) list...");
        Integer i = 0;
        Integer j = 0;
        Integer total = 1;
        Double matchedTickets = 0.0;
        ArrayList<Ticket> tickets = new ArrayList<>();
        do {
            j = i + 1000;
            // Build a JIRA Query to found all the tickets related to the project
            JIRAQuery q = new JIRAQuery(JIRAQueryType.JIRA_QUERY_TYPE_SEARCH);
            q.getJqlQuery().setProjectName(projName);
            q.getJqlQuery().setIssueType(JQL_ISSUE_TYPE_BUG);
            q.getJqlQuery().setStatus(JQL_STATUS_CLOSED, JQL_STATUS_RESOLVED, JQL_STATUS_DONE);
            q.getJqlQuery().setResolution(JQL_RESOLUTION_FIXED, JQL_RESOLUTION_DONE);
            q.setFields(JQL_FIELD_KEY, JQL_FIELD_RESOLUTIONDATE, JQL_FIELD_FIXVERSIONS, JQL_FIELD_CREATED);
            q.setStartAt(i);
            q.setMaxResults(j);
            String url = q.compose().toString();
            try {
                JSONObject json = JSONHandler.readJsonFromUrl(url);
                JSONArray issues = json.getJSONArray("issues");
                total = json.getInt("total");
                for (; i < total && i < j; i++) {
                    // Iterate through each ticket
                    String key = issues.getJSONObject(i % 1000).get(JQL_FIELD_KEY).toString();
                    Ticket iteratedTicket = new Ticket(key);

                    // Search for commits related to ticket
                    List<Commit> iteratedCommitList = CommitHandler.fetchCommitsRelatedToTicket(iteratedTicket, commitList);
                    log.finest(() -> "Ticket " + iteratedTicket.getId() + ": " + iteratedCommitList.size() + " commit(s) found.");
                    if (!iteratedCommitList.isEmpty()) {
                        iteratedTicket.setCommitList(iteratedCommitList);
                        matchedTickets++;
                    }
                    tickets.add(iteratedTicket);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } while (i < total);

        Double finalMatchedTickets = matchedTickets;
        log.info(() -> "- Linkage probability = " + (finalMatchedTickets / tickets.size()));
        log.info(() -> "- " + tickets.size() + " tickets found. ");
        return tickets;
    }
}


