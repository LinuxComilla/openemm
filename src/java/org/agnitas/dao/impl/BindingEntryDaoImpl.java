/*********************************************************************************
 * The contents of this file are subject to the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.openemm.org/cpal1.html. The License is based on the Mozilla
 * Public License Version 1.1 but Sections 14 and 15 have been added to cover
 * use of software over a computer network and provide for limited attribution
 * for the Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenEMM.
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is AGNITAS AG. All portions of
 * the code written by AGNITAS AG are Copyright (c) 2007 AGNITAS AG. All Rights
 * Reserved.
 * 
 * Contributor(s): AGNITAS AG. 
 ********************************************************************************/

package org.agnitas.dao.impl;

import java.util.List;
import java.util.Map;

import org.agnitas.beans.BindingEntry;
import org.agnitas.dao.BindingEntryDao;
import org.agnitas.util.AgnUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nse
 */
public class BindingEntryDaoImpl implements BindingEntryDao {

    /**
     * Getter for one BindingEntry.
     *
     * @return The BindingEntry
     */
    public BindingEntry get(int recipientID, int companyID, int mailinglistID,
            int mediaType) {

        JdbcTemplate jdbc = AgnUtils.getJdbcTemplate(this.applicationContext);
        BindingEntry aEntry = null;

        String sql = "select user_type, user_status, timestamp, exit_mailing_id, user_remark from customer_"
                + companyID
                + "_binding_tbl where customer_id=? and mailinglist_id=? and mediatype=?";
        try {
            List list = jdbc.queryForList(sql, new Object[] {
                    new Integer(recipientID), new Integer(mailinglistID),
                    new Integer(mediaType) });
            if (list.size() > 0) {
                Map map = (Map) list.get(0);

                                aEntry=(BindingEntry) applicationContext.getBean("BindingEntry");
                aEntry.setUserType((String) map.get("user_type"));
                aEntry.setUserStatus(((Integer) map.get("user_status"))
                        .intValue());
                aEntry.setChangeDate((java.sql.Date) map.get("timestamp"));
                aEntry.setExitMailingID(((Integer) map.get("exit_mailing_id"))
                        .intValue());
                aEntry.setUserRemark((String) map.get("user_remark"));
            }
        } catch (Exception e) {
            AgnUtils.logger().error("sql: " + e.getMessage());
        }
        return aEntry;
    }

    public void save(int companyID, BindingEntry entry) {
        JdbcTemplate jdbc = AgnUtils.getJdbcTemplate(this.applicationContext);
        String currentTimestamp = AgnUtils.getSQLCurrentTimestamp();

        String sql = "select * from customer_"
                + companyID
                + "_binding_tbl where customer_id=? and mailinglist_id=? and mediatype=?";
        try {
            List list = jdbc.queryForList(sql, new Object[] {
                    new Integer(entry.getCustomerID()),
                    new Integer(entry.getMailinglistID()),
                    new Integer(entry.getMediaType()) });
            if (list.size() > 0) {
                sql = "update customer_"
                        + companyID
                        + "_binding_tbl set usertype=?, user_status=?, timestamp=sysdate, exit_mailing_id=?, user_remark=?";
                jdbc.update(sql, new Object[] { entry.getUserType(),
                        new Integer(entry.getUserStatus()),
                        new Integer(entry.getExitMailingID()),
                        entry.getUserRemark() });
            } else {
                sql = "insert into customer_"
                        + companyID
                        + "_binding_tbl (mailinglist_id, customer_id, user_type, user_status, timestamp, user_remark, creation_date, exit_mailing_id, mediatype) VALUES (?, ?, ?, ?, "
                        + currentTimestamp + ", ?, " + currentTimestamp
                        + ", ?, ?)";
                jdbc.update(sql, new Object[] {
                        new Integer(entry.getMailinglistID()),
                        new Integer(entry.getCustomerID()),
                        entry.getUserType(),
                        new Integer(entry.getUserStatus()),
                        entry.getUserRemark(),
                        new Integer(entry.getExitMailingID()),
                        new Integer(entry.getMediaType()) });
            }
        } catch (Exception e) {
            AgnUtils.logger().error("sql: " + e.getMessage());
        }
    }

    /**
     * Holds value of property applicationContext.
     */
    protected ApplicationContext applicationContext;

    /**
     * Setter for property applicationContext.
     *
     * @param applicationContext
     *            New value of property applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

}
