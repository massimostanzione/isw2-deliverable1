package it.uniroma2.dicii.isw2.deliverable1.control;

import it.uniroma2.dicii.isw2.deliverable1.entities.Commit;
import it.uniroma2.dicii.isw2.deliverable1.entities.jql.JIRAQuery;
import it.uniroma2.dicii.isw2.deliverable1.entities.jql.JIRAQueryType;
import it.uniroma2.dicii.isw2.deliverable1.entities.jql.JQLQuery;
import it.uniroma2.dicii.isw2.deliverable1.utils.LoggerInst;
import it.uniroma2.dicii.isw2.deliverable1.entities.Ticket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Class for ticket and bug handling, mainly fetching them from projects.
 */
public class TicketBugHandler {
    private static final Logger LOG = LoggerInst.getSingletonInstance();

    /**
     * Fetch project tickets from JIRA, parse them and assign them their related commits.
     *
     * @param projName   project name
     * @param commitList commits of the project
     * @return list containing the tickets (with related commits) for the projects
     */
    public static List<Ticket> fetchProjectTickets(String projName, List<Commit> commitList) {
        LOG.info("Initializing ticket (and related commits) list...");
        Integer i = 0, j = 0, total = 1;
        Double matchedTickets = 0.0;
        ArrayList<Ticket> tickets = new ArrayList<>();
        do {
            j = i + 1000;
            // Build a JIRA Query to found all the tickets related to the project
            JIRAQuery q = new JIRAQuery(JIRAQueryType.JIRA_QUERY_TYPE_SEARCH);
            q.getJQLQuery().setProjectName(projName);
            q.getJQLQuery().setIssueType(JQLQuery.JQL_ISSUE_TYPE_BUG);
            q.getJQLQuery().setStatus(JQLQuery.JQL_STATUS_CLOSED, JQLQuery.JQL_STATUS_RESOLVED, JQLQuery.JQL_STATUS_DONE);
            q.getJQLQuery().setResolution(JQLQuery.JQL_RESOLUTION_FIXED, JQLQuery.JQL_RESOLUTION_DONE);
            q.setFields(JQLQuery.JQL_FIELD_KEY, JQLQuery.JQL_FIELD_RESOLUTIONDATE, JQLQuery.JQL_FIELD_FIXVERSIONS, JQLQuery.JQL_FIELD_CREATED);
            q.setStartAt(i);
            q.setMaxResults(j);
            String url = q.compose().toString();
            try {
                JSONObject json = JSONHandler.readJsonFromUrl(url);
                JSONArray issues = json.getJSONArray("issues");
                total = json.getInt("total");
                for (; i < total && i < j; i++) {
                    // Iterate through each ticket
                    String key = issues.getJSONObject(i % 1000).get(JQLQuery.JQL_FIELD_KEY).toString();
                    String creat = issues.getJSONObject(i % 1000).getJSONObject(JQLQuery.JQL_FIELDS).get(JQLQuery.JQL_FIELD_CREATED).toString();
                    String res = issues.getJSONObject(i % 1000).getJSONObject(JQLQuery.JQL_FIELDS).get(JQLQuery.JQL_FIELD_RESOLUTIONDATE)
                            .toString();
                    Ticket iteratedTicket = new Ticket(key);

                    // Search for commits related to ticket
                    List<Commit> iteratedCommitList = CommitHandler.fetchCommitsRelatedToTicket(iteratedTicket, commitList);
                    LOG.finest("Ticket " + iteratedTicket.getID() + ": " + iteratedCommitList.size() + " commit(s) found.");
                    if (iteratedCommitList.size() > 0) {
                        iteratedTicket.setCommitList(iteratedCommitList);
                        matchedTickets++;
                    }
                    tickets.add(iteratedTicket);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } while (i < total);

        LOG.info("- Linkage probability = " + (matchedTickets / tickets.size()));
        LOG.info("- " + tickets.size() + " tickets found. ");
        return tickets;
    }
}


