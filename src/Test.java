import java.sql.Connection;

import com.bill.upload.utils.DBUtils;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;
		DBUtils db=new DBUtils();
		conn=db.getConnection();
		System.out.println(conn);
	}

}
