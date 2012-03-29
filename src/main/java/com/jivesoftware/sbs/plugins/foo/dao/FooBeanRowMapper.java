package com.jivesoftware.sbs.plugins.foo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jivesoftware.sbs.plugins.foo.FooBean;

public class FooBeanRowMapper implements RowMapper<FooBean> {

    @Override
    public FooBean mapRow(ResultSet rs, int rowNum) throws SQLException {
        FooBean foo = new FooBean();
        int i = 1;

        foo.setID(rs.getLong(i++));
        foo.setContainerID(rs.getLong(i++));
        foo.setContainerType(rs.getInt(i++));
        foo.setUserID(rs.getLong(i++));
        foo.setStatusID(rs.getInt(i++));
        foo.setCreationDate(new Date(rs.getLong(i++)));
        foo.setModificationDate(new Date(rs.getLong(i++)));
        foo.setTitle(rs.getString(i++));
        foo.setDescription(rs.getString(i++));

        return foo;
    }
}
