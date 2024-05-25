package org.example.audit;

import org.example.database.MySQLDatabase;
import org.example.exceptions.PermissionException;
import org.example.input.ParseInput;
import org.example.repository.AuditRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.List;

public class AuditManager {
    private AuditRepository auditRepository;

    public AuditManager() {
        this.auditRepository = AuditRepository.getInstance();
    }

    public int getNrPagini(User user) {
        return auditRepository.getNrPagini("audit", "user_id", user.getId());
    }

    public boolean insertIntoAudit(String command, boolean rulatOk) {
        return auditRepository.addAudit(new Audit(User.currentUser.getId(), command, rulatOk));
    }

    public boolean reRunAuditCommand(int id) {
        Audit audit = auditRepository.getAuditById(id);
        if (audit == null) {
            System.out.println("Audit not found!");
            return false;
        }

        if (audit.getUserId() != User.currentUser.getId()) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        System.out.println("Re-running command: " + audit.getCommand());
        ParseInput.parseInput(audit.getCommand());
        return true;
    }

    public boolean getAuditsForUser(User user, int paginaCurenta) {
        if (User.currentUser.getUserTypeEnum() != UserTypeEnum.ADMIN) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        int nrPaginiTotal = getNrPagini(user);

        if (paginaCurenta < 0 || (paginaCurenta >= nrPaginiTotal && paginaCurenta != 0)) {
            System.out.println("Invalid page number!");
            return false;
        }

        List<Audit> audits = auditRepository.getAuditsForUser(user, paginaCurenta);

        if (audits.isEmpty()) {
            paginaCurenta = -1;
        }

        System.out.println("Page " + (paginaCurenta + 1) + " of " + nrPaginiTotal + " (" + MySQLDatabase.nrElementePagina + " items per page):");
        System.out.println("ID\tUser ID\tCommand\tRulatOK");
        for (Audit audit : audits) {
            System.out.println(audit.getId() + "\t" + audit.getUserId() + "\t" + audit.getCommand() + " " + audit.isRulatOk());
        }

        return true;
    }
}
