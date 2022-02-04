package it.uniroma2.dicii.isw2.deliverable1.control;

import it.uniroma2.dicii.isw2.deliverable1.entities.Commit;
import it.uniroma2.dicii.isw2.deliverable1.entities.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for commit handling.
 */
public class CommitHandler {
    /**
     * Given a ticket and a commit list, return a sublist of the commits that are related to the ticket
     *
     * @param ticket     ticket
     * @param commitList list of commits
     * @return sublist of <code>commitList</code> with commits related to <code>ticket</code>
     */
    public static List<Commit> fetchCommitsRelatedToTicket(Ticket ticket, List<Commit> commitList) {
        List<Commit> ret = new ArrayList<>();
        for (Commit iteratedCommit : commitList) {
            if (iteratedCommit.getCommitMsg().contains(ticket.getID() + (":"))
                    || iteratedCommit.getCommitMsg().contains(ticket.getID() + " ")
                    || iteratedCommit.getCommitMsg().contains(ticket.getID() + "]")) {
                ret.add(iteratedCommit);
            }
        }
        return ret;
    }
}
