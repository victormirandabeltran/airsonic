/*
 This file is part of Libresonic.

 Libresonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Libresonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Libresonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2016 (C) Libresonic Authors
 Based upon Subsonic, Copyright 2009 (C) Sindre Mehus
 */
package org.libresonic.player.dao;

import org.libresonic.player.Logger;
import org.libresonic.player.domain.InternetRadio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Provides database services for internet radio.
 *
 * @author Sindre Mehus
 */
public class InternetRadioDao extends AbstractDao {

    private static final Logger LOG = Logger.getLogger(InternetRadioDao.class);
    private static final String INSERT_COLUMNS = "name, stream_url, homepage_url, enabled, changed";
    private static final String QUERY_COLUMNS = "id, " + INSERT_COLUMNS;
    private final InternetRadioRowMapper rowMapper = new InternetRadioRowMapper();

    /**
     * Returns all internet radio stations.
     *
     * @return Possibly empty list of all internet radio stations.
     */
    public List<InternetRadio> getAllInternetRadios() {
        String sql = "select " + QUERY_COLUMNS + " from internet_radio";
        return query(sql, rowMapper);
    }

    /**
     * Creates a new internet radio station.
     *
     * @param radio The internet radio station to create.
     */
    public void createInternetRadio(InternetRadio radio) {
        String sql = "insert into internet_radio (" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?)";
        update(sql, radio.getName(), radio.getStreamUrl(), radio.getHomepageUrl(), radio.isEnabled(), radio.getChanged());
        LOG.info("Created internet radio station " + radio.getName());
    }

    /**
     * Deletes the internet radio station with the given ID.
     *
     * @param id The internet radio station ID.
     */
    public void deleteInternetRadio(Integer id) {
        String sql = "delete from internet_radio where id=?";
        update(sql, id);
        LOG.info("Deleted internet radio station with ID " + id);
    }

    /**
     * Updates the given internet radio station.
     *
     * @param radio The internet radio station to update.
     */
    public void updateInternetRadio(InternetRadio radio) {
        String sql = "update internet_radio set name=?, stream_url=?, homepage_url=?, enabled=?, changed=? where id=?";
        update(sql, radio.getName(), radio.getStreamUrl(), radio.getHomepageUrl(), radio.isEnabled(), radio.getChanged(), radio.getId());
    }

    private static class InternetRadioRowMapper implements RowMapper<InternetRadio> {
        public InternetRadio mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new InternetRadio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getTimestamp(6));
        }
    }

}
