package com.jivesoftware.sbs.plugins.foo.upgrade;

import java.io.InputStream;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jivesoftware.sqlgen.SQLGen;

import com.jivesoftware.base.database.ConnectionManager;

public class UpgradeDao {
    private static final Logger log = Logger.getLogger(UpgradeDao.class);

    public void applySchema(String schemaName) throws Exception {
        InputStream is = null;
        Connection con = null;

        try {
            is = this.getClass().getClassLoader().getResourceAsStream(schemaName);
            if(is == null) {
                throw new IllegalArgumentException("Could not load AddUrlField.xml");
            }

            con = ConnectionManager.getConnection();

            SQLGen sqlgen = new SQLGen();
            sqlgen.addInputStream(is);
            sqlgen.applySQLToDatabase(con);

        } catch (Exception e) {
            log.error("Could not complete upgrade task", e);
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    //ignore
                }
            }

            ConnectionManager.close(con);
        }

    }
}