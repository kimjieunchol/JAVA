package Music;

import java.util.List;
import java.util.Scanner;

public class main {
	public static void main(String[] args) {
		DataDAO dao = DataDAO.newInstance();
		Bugs m = new Bugs();

		if (dao.tableCheck() == false) {
			dao.tableCreated();
		} else {
			dao.initialization();
		} // 테이블, SEQUENCE 초기화

		if (dao.myTableCheck() == false) {
			dao.myTableCreated();
		} else {
		}

		m.crawling();

		int key;
		Boolean end = true;
		Scanner scanner = new Scanner(System.in);
		System.out.println("음악 실시간 TOP100 차트 & MyPlaylist 프로그램");

		while (end) {
			System.out.println();
			System.out.println("1.List 보기 2.검색 3.음악 추가 4.수정 5.삭제 6.MyPlaylist 추가 7.종료");
			System.out.print("원하는 기능을 숫자로 입력 해주세요: ");
			key = scanner.nextInt();

			if (key == 1) { // List 보기
				while (true) {
					System.out.println();
					System.out.println("1.TOP100 차트 2.추가List 3.MyList (0.이전메뉴)");
					System.out.print("입력: ");
					int value = scanner.nextInt();

					if (value == 0) { //메뉴
						break;
					} else if (value == 1) { //TOP100 차트
						dao.getMusic();
						break;
					} else if (value == 2) { //추가List
						List<List<String>> tmp = dao.addPrintMusic();
						if (tmp.isEmpty()) {
							System.out.println("추가된 곡이 없습니다.");
						} else {}
						break;
					} else if (value == 3) { //MyList
						List<List<String>> tmp = dao.getMyMusic();
						if (tmp.isEmpty()) {
							System.out.println("MyPlaylist가 비어있습니다.");
						} else {}
						break;
					} else {
						System.out.println("오!류");
						break;
					}
				}
			}

			else if (key == 2) { // 검색
				System.out.println();
				System.out.println("곡, 가수(그룹), 앨범 택1 검색 가능. (0.이전메뉴)");
				System.out.print("입력: ");
				scanner.nextLine();
				String[] music = (scanner.nextLine()).split(",");

				if (music[0].equals("0")) { //메뉴
					continue;
				}

				dao.searchMusic(music);
			}

			else if (key == 3) { // 음악 추가
				System.out.println();
				System.out.println("양식에 맞게 입력하시오 ex) AIRPLANE MODE,윤하,RescuE (0.이전메뉴)");
				System.out.print("입력: ");
				scanner.nextLine();
				String[] music = (scanner.nextLine()).split(",");

				if (music[0].equals("0")) {
					continue;
				}

				m.dao.addMusic(music[0], music[1], music[2]);
				System.out.println("저장되었습니다.");
			}

			else if (key == 4) { // 수정
				System.out.println();
				String[] value = new String[3];
				List<List<String>> tmp = dao.addPrintMusic();
				if (tmp.isEmpty()) {
					System.out.println("수정은 추가된 음악만 가능합니다.");
					System.out.println();
				} else {
					System.out.println("수정하고 싶은 곡 번호 (0.이전메뉴)");
					System.out.print("입력:");
					scanner.nextLine();
					value[0] = scanner.nextLine();

					if (value[0].equals("0")) { //메뉴
						continue;
					}

					System.out.println("수정하고 싶은 항목(1.곡, 2.가수(그룹), 3.앨범 중 택1)");
					System.out.print("입력: ");
					value[1] = scanner.nextLine();

					System.out.print("택스트 입력: ");
					value[2] = scanner.nextLine();

					dao.retouchMusic(tmp, value);
					System.out.println("수정되었습니다.");
				}
			}

			else if (key == 5) { // 삭제
				Boolean key5 = true;
				while (key5) {
					System.out.println();
					System.out.println("1.추가된 음악 삭제 2.MyPlaylist 삭제 3.MyPlaylist 초기화 4.메뉴");
					System.out.print("입력: ");
					int value = scanner.nextInt();
					scanner.nextLine();
					if (value == 1) { //추가된 음악 삭제
						while (true) {
							List<List<String>> tmp = dao.addPrintMusic();
							if (tmp.isEmpty()) {
								System.out.println();
								System.out.println("삭제는 추가된 음악만 가능합니다.");
								break;
							} else {
								System.out.println("삭제하고 싶은 번호 (0.이전메뉴)");
								System.out.print("번호 입력(여러개 가능) ex) 1,2,3,4 : ");
								String[] music = (scanner.nextLine()).split(",");
								if (music[0].equals("0")) {
									key5 = false;
									break;
								} else {
									for (String i : music)
										dao.deleteMusic(tmp, i);
								}
								System.out.println("삭제완료");
								key5 = false;
								break;
							}
						}
					}

					else if (value == 2) { //MyPlaylist 삭제
						List<List<String>> tmp = dao.printMyMusic();

						if (tmp.isEmpty()) {
							System.out.println();
							System.out.println("MyPlaylist가 비어있습니다.");
							break;
						} else {
							System.out.println();
							System.out.println("삭제하고 싶은 번호 (0.이전메뉴)");
							System.out.print("번호 입력(여러개 가능) ex) 1,2,3,4 : ");
							String[] music = (scanner.nextLine()).split(",");
							if (music[0].equals("0")) {
								continue;
							} else {
								for (String i : music)
									dao.deleteMyMusic(tmp, i);
							}
							System.out.println("삭제완료");
							key5 = false;
							break;
						}
					}

					else if (value == 3) { //MyPlaylist 초기화
						dao.mtInitialization();
						System.out.println("MyPlaylist가 초기화 되었습니다.");
						key5 = false;
						break;
					}

					else if (value == 4) { //메뉴
						key5 = false;
						break;
					} else {
						System.out.println("오!류");
					}
				}
			}

			else if (key == 6) { // MyPlaylist 추가
				Boolean key6 = true;
				while (key6) {
					System.out.println();
					System.out.println("MyPlaylist 추가 입니다.");
					System.out.println("1.직접 추가 2.검색후 추가 3.랜덤 추가 4.메뉴");
					System.out.print("입력: ");
					int value = scanner.nextInt();
					scanner.nextLine();
					if (value == 1) { //직접 추가
						while (true) {
							System.out.println();
							System.out.println("양식에 맞게 입력하시오 ex) AIRPLANE MODE,윤하,RescuE (0.이전메뉴)");
							System.out.print("입력: ");
							String[] music = (scanner.nextLine()).split(",");
							if (music[0].equals("0")) { //이전메뉴
								break;
							} else if (music.length == 3) {
								dao.addMyMusic(music[0], music[1], music[2]);
								System.out.println("저장되었습니다. ");
							} else {
								System.out.println("오!류");
							}
						}
					}

					else if (value == 2) {
						System.out.println();
						System.out.println("곡, 가수(그룹), 앨범 택1 검색 가능. (0.이전메뉴)");
						System.out.print("입력: ");
						String[] music = (scanner.nextLine()).split(",");

						if (music[0].equals("0")) {
							continue;
						}

						List<List<String>> tmp = dao.SearchMyMusic(music);
						int index = tmp.size();
						System.out.println();
						System.out.println("추가하고 싶은 번호 입력 (여러개 가능) ex) 1,2,3,4 (0.이전메뉴)");
						System.out.print("입력: ");
						music = (scanner.nextLine()).split(",");

						if (music[0].equals("0")) {
							continue;
						}

						for (String i : music) {
							tmp.add(tmp.get(Integer.parseInt(i) - 1));
						}
						tmp.subList(0, index).clear();
						dao.myListAddMusic(tmp);
						System.out.println("추가 완료");
						key6 = false;
					}

					else if (value == 3) {
						while (true) {
							List<List<String>> tmp = dao.randomMusic();
							System.out.println();
							System.out.println("1.MyList에 추가 2.한번더 3.취소");
							System.out.print("입력: ");
							int valueIn = scanner.nextInt();
							if (valueIn == 1) {
								dao.myListAddMusic(tmp);
								System.out.println("MyList에 추가 되었습니다.");
								System.out.println();
								break;
							} else if (valueIn == 2) {
								tmp = dao.randomMusic();
							} else if (valueIn == 3) {
								break;
							}
						}
					}

					else if (value == 4) {
						key6 = false;
					} else {
						System.out.println("오!류");
					}
				}
			}

			else if (key == 7) { // 종료
				System.out.println("프로그램을 종료합니다.");
				dao.disConnection();
				end = false;
			}

			else {
				System.out.println("오!류");
			}
		}
	}

}
