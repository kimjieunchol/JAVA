package Music;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataDAO {

	private Connection conn;
	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs;
	private static DataDAO dao;
	private static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String user = "music";
	private static final String pwd = "1234";

	private DataDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, pwd);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void disConnection() {
		try {
			if (stmt != null)
				stmt.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DataDAO newInstance() {
		if (dao == null)
			dao = new DataDAO();

		return dao;

	}
	
	public boolean tableCheck() {
		String bool = null;
		try {
			getConnection();

			String sql = "select count(*) from USER_TABLES where table_name = 'MUSIC'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bool = rs.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return "1".equals(bool);
	}
	
	public void tableCreated() {
		try {
			getConnection();
			stmt = conn.createStatement();
			
			String sql = "CREATE TABLE MUSIC(no_music NUMBER(4),"
					+ "ranking VARCHAR2(100),"
					+ "name VARCHAR2(500),"
					+ "artist VARCHAR2(500),"
					+ "album VARCHAR2(200))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE SEQUENCE no_music INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOORDER";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public boolean myTableCheck() {
		String bool = null;
		try {
			getConnection();

			String sql = "select count(*) from USER_TABLES where table_name = 'MYMUSIC'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bool = rs.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return "1".equals(bool);
	}
	
	public void myTableCreated() {
		try {
			getConnection();
			stmt = conn.createStatement();
			
			String sql = "CREATE TABLE mymusic(no_mymusic NUMBER(4),"
					+ "ranking VARCHAR2(100),"
					+ "name VARCHAR2(500),"
					+ "artist VARCHAR2(500),"
					+ "album VARCHAR2(200))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE SEQUENCE no_mymusic INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOORDER";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void initialization() {
		try {
			getConnection();
			stmt = conn.createStatement();

			String sql = "DROP SEQUENCE no_music";
			stmt.executeUpdate(sql);

			sql = "CREATE SEQUENCE no_music INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOORDER";
			stmt.executeUpdate(sql);

			sql = "DELETE FROM music";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void musicInsert(MusicVO se) {
		try {
			getConnection();
			String sql = "insert into MUSIC values(no_music.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, se.getRanking());
			pstmt.setString(2, se.getName());
			pstmt.setString(3, se.getArtist());
			pstmt.setString(4, se.getAlbum());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void getMusic() {
		try {
			getConnection();
			String sql = "SELECT * FROM MUSIC where not ranking ='추가' ORDER BY no_music";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) { // 한줄씩 읽는다 (튜플)

				System.out.println("================================");
				System.out.println("순위:       " + rs.getString(2));
				System.out.println("곡:         " + rs.getString(3));
				System.out.println("가수(그룹): " + rs.getString(4));
				System.out.println("앨범:       " + rs.getString(5));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");
	}
	
	public List<List<String>> addPrintMusic() {
		int count = 0;
		List<List<String>> addlist = new ArrayList<>();
		try {
			getConnection();
			String sql = "SELECT * FROM MUSIC WHERE ranking LIKE '%추가%'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) { // 한줄씩 읽는다 (튜플)
				addlist.add(new ArrayList<String>(Arrays.asList(rs.getString(1), String.valueOf(++count),
						rs.getString(3), rs.getString(4), rs.getString(5))));
			}

			for (int i = 0; i < addlist.size(); i++) { // 한줄씩 읽는다 (튜플)
				System.out.println("================================");
				System.out.println("번호:       " + addlist.get(i).get(1));
				System.out.println("곡:         " + addlist.get(i).get(2));
				System.out.println("가수(그룹): " + addlist.get(i).get(3));
				System.out.println("앨범:       " + addlist.get(i).get(4));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");

		return addlist;
	}
	
	public List<List<String>> getMyMusic() {
		int count = 0;
		List<List<String>> list = new ArrayList<>();
		try {
			getConnection();
			String sql = "SELECT * FROM mymusic ORDER BY ranking";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) { // 한줄씩 읽는다 (튜플)
				System.out.println("================================");
				System.out.println("순위:       " + rs.getString(2));
				System.out.println("곡:         " + rs.getString(3));
				System.out.println("가수(그룹): " + rs.getString(4));
				System.out.println("앨범:       " + rs.getString(5));

				list.add(new ArrayList<String>(Arrays.asList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), String.valueOf(count))));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");

		return list;
	}
	
	public void searchMusic(String[] name) {
		List<List<String>> list = new ArrayList<>();
		try {
			getConnection();
			for (String i : name) {
				String sql = "SELECT * FROM MUSIC WHERE name LIKE '%" + i + "%' or artist LIKE '%" + i
						+ "%' or album LIKE '%" + i + "%'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				while (rs.next()) { // 한줄씩 읽는다 (튜플)
					list.add(new ArrayList<String>(
							Arrays.asList(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5))));
				}

			}

			List<List<String>> newList = list.stream().distinct().collect(Collectors.toList());

			for (int i = 0; i < newList.size(); i++) { // 한줄씩 읽는다 (튜플)
				System.out.println("================================");
				System.out.println("순위:       " + newList.get(i).get(0));
				System.out.println("곡:         " + newList.get(i).get(1));
				System.out.println("가수(그룹): " + newList.get(i).get(2));
				System.out.println("앨범:       " + newList.get(i).get(3));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");
	}
	
	public void addMusic(String name, String artist, String album) {
		String sql = "insert into MUSIC values(no_music.nextval,?,?,?,?)";

		try {
			getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "추가");
			pstmt.setString(2, name);
			pstmt.setString(3, artist);
			pstmt.setString(4, album);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void retouchMusic(List<List<String>> list, String[] value) {
		String tmp, no = null;

		if (value[1].equals("1")) {
			tmp = "name";
		} else if (value[1].equals("2")) {
			tmp = "artist";
		} else if (value[1].equals("3")) {
			tmp = "album";
		} else {
			System.out.println("오류");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get(1).equals(value[0])) {
				no = list.get(i).get(0);
			}
		}

		try {
			getConnection();
			String sql = "UPDATE MUSIC SET " + tmp + "='" + value[2] + "' WHERE no_music='" + no + "'";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}

	public void deleteMusic(List<List<String>> list, String value) {
		String no = null;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get(1).equals(value)) {
				no = list.get(i).get(0);
			}
		}

		try {
			getConnection();
			String sql = "delete from MUSIC WHERE no_music='" + no + "'";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}

	public List<List<String>> printMyMusic() {
		int count = 0;
		List<List<String>> list = new ArrayList<>();
		try {
			getConnection();
			String sql = "SELECT * FROM mymusic ORDER BY no_mymusic";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) { // 한줄씩 읽는다 (튜플)
				System.out.println("================================");
				System.out.println("번호:       " + ++count);
				System.out.println("곡:         " + rs.getString(3));
				System.out.println("가수(그룹): " + rs.getString(4));
				System.out.println("앨범:       " + rs.getString(5));

				list.add(new ArrayList<String>(Arrays.asList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), String.valueOf(count))));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");

		return list;
	}
	
	public void deleteMyMusic(List<List<String>> list, String value) {
		String no = null;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get(5).equals(value)) {
				no = list.get(i).get(0);
			}
		}

		try {
			getConnection();
			String sql = "delete from mymusic WHERE no_mymusic='" + no + "'";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void mtInitialization() {
		try {
			getConnection();
			stmt = conn.createStatement();

			String sql = "DROP SEQUENCE no_mymusic";
			stmt.executeUpdate(sql);

			sql = "CREATE SEQUENCE no_mymusic INCREMENT BY 1 START WITH 1 MAXVALUE 999 NOORDER";
			stmt.executeUpdate(sql);

			sql = "DELETE FROM mymusic";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void addMyMusic(String name, String artist, String album) {
		String sql = "insert into mymusic values(no_mymusic.nextval,?,?,?,?)";

		try {
			getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "추가");
			pstmt.setString(2, name);
			pstmt.setString(3, artist);
			pstmt.setString(4, album);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public List<List<String>> SearchMyMusic(String[] name) {
		int count = 0;
		List<List<String>> list = new ArrayList<>();

		try {
			getConnection();
			for (String i : name) {
				String sql = "SELECT * FROM MUSIC WHERE name LIKE '%" + i + "%' or artist LIKE '%" + i
						+ "%' or album LIKE '%" + i + "%'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				while (rs.next()) { // 한줄씩 읽는다 (튜플)
					list.add(new ArrayList<String>(Arrays.asList(String.valueOf(++count), rs.getString(2),
							rs.getString(3), rs.getString(4), rs.getString(5))));
				}
			}

			list = list.stream().distinct().collect(Collectors.toList()); // 중복제거

			for (int i = 0; i < list.size(); i++) { // 한줄씩 읽는다 (튜플)
				System.out.println("================================");
				System.out.println("번호:       " + list.get(i).get(0));
				System.out.println("곡:         " + list.get(i).get(2));
				System.out.println("가수(그룹): " + list.get(i).get(3));
				System.out.println("앨범:       " + list.get(i).get(4));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");
		return list;
	}
	
	public void myListAddMusic(List<List<String>> list) {
		String sql = "insert into mymusic values(no_mymusic.nextval,?,?,?,?)";
		getConnection();
		try {
			for (int i = 0; i < list.size(); i++) { // 한줄씩 읽는다 (튜플)
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, list.get(i).get(1));
				pstmt.setString(2, list.get(i).get(2));
				pstmt.setString(3, list.get(i).get(3));
				pstmt.setString(4, list.get(i).get(4));
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public List<List<String>> randomMusic() {
		List<List<String>> addlist = new ArrayList<>();
		try {
			getConnection();
			String sql = "select * from (select * from music order by dbms_random.value) where rownum <= 30 ORDER BY no_music";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) { // 한줄씩 읽는다 (튜플)
				addlist.add(new ArrayList<String>(Arrays.asList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5))));
			}

			for (int i = 0; i < addlist.size(); i++) { // 한줄씩 읽는다 (튜플)

				System.out.println("================================");
				// System.out.println("번호: " + addlist.get(i).get(1));
				System.out.println("순위:       " + addlist.get(i).get(1));
				System.out.println("곡:         " + addlist.get(i).get(2));
				System.out.println("가수(그룹): " + addlist.get(i).get(3));
				System.out.println("앨범:       " + addlist.get(i).get(4));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disConnection();
		}
		System.out.println("================================");
		return addlist;
	}

}