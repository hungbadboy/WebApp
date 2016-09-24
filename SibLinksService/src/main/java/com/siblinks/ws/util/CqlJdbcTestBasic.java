package com.siblinks.ws.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CqlJdbcTestBasic {
	public static void main(String[] args) {
		Connection con = null;
		try {
			Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");
			con = DriverManager
					.getConnection("jdbc:cassandra://54.213.94.216:9160/sib");
			String query = "SELECT empno,ename,deptno FROM emp";

			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {
				System.out.println(result.getString("empno"));
				System.out.println(result.getString("ename"));
				System.out.println(result.getString("deptno"));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = null;
			}
		}
	}
}