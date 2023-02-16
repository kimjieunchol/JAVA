package Music;

import java.util.List;
import java.util.Scanner;

public class main2 {
	public static void main(String[] args) {
		Bugs m = new Bugs();
		m.dao.initialization(); // 테이블, SEQUENCE 초기화
		m.crawling();
		

	}

}
