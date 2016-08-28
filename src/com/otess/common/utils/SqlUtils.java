package com.otess.common.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.JsonKit;
import com.otess.common.bean.DBType;

public final class SqlUtils {

	// 定义需要的变量
	private Connection conn = null;
	private Statement st=null;
	private ResultSet rs = null;
	private Integer dbtype;
	private String dbaddress;
	private String dbport;
	private String dbusername;
	private String dbpassword;
	private String dbname;
	JDBCUtils db =null;

    
	public SqlUtils(Integer dbtype, String dbaddress, String dbport, String dbusername, String dbpassword,
			String dbname) {
		//db=JDBCUtils.getInstance(dbtype, dbaddress, dbport, dbusername, dbpassword, dbname);
		this.dbtype=dbtype;
		this.dbaddress=dbaddress;
		this.dbport=dbport;
		this.dbusername=dbusername;
		this.dbpassword=dbpassword;
		this.dbname=dbname;
		db=new JDBCUtils(dbtype,dbaddress,dbport, dbusername,dbpassword, dbname);
	}
	@SuppressWarnings({ "rawtypes", "rawtypes" })
	public List ResultSetToList(ResultSet rs) throws SQLException {
        try {
            ResultSetMetaData md = rs.getMetaData();    // 获得结果集结构信息（字段数、字段名等）
            //System.out.println(JsonKit.toJson(rs));
            int columnCount = md.getColumnCount();
            List list = new ArrayList();
            while (rs.next()) {
                Map rowData = new LinkedHashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }

            System.out.println(JsonKit.toJson(list));
            return list;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally{
        	if (rs != null) rs.close();
        	if (st != null) st.close();
        	if (conn != null) conn.close();
        	db.free(conn, st, rs);
        }
    }
	/**
	 * 查询
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException{
        try  
        {  
        	System.out.println(sql);
        	conn = db.getConnection();   
        	st= conn.createStatement(  
                    ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);  
            rs = st.executeQuery(sql);              
        }  
        catch (SQLException e)  
        {  
        	System.out.println(e.getMessage());
            e.printStackTrace();  
        }  
        return rs; 
	}
	
	/**
	 * 获取全部表
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQueryTables() throws SQLException{ 
        try  
        {  
        	conn = db.getConnection();                
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(conn.getCatalog(), "root", null, new String[]{"TABLE"});
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        	throw new SQLException(e.getMessage()); 
        }
        return rs; 
	}
	/**
	 * 获取全部列
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQueryColumns(String tableName) throws SQLException{ 
        try  
        {  
        	conn = db.getConnection();              
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(null, "%", tableName, "%");
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return rs; 
	}
}
