package com.test.myapp.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/board/list.do")
public class List extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/*
		 할 일
		 1. DB작업 > DAO 위임 > select
		 2. ArrayList<BoardDTO> 반환
		 3. JSP 호출 + 2번 전달
		 */
		
		// 1.
		BoardDAO dao = new BoardDAO();
		
		// 2.
		ArrayList<BoardDTO> list = dao.list();
		
		// 2.5
		/*
		 날짜 가공 업무 방법
		 1. SQL > 나중에 시분초 필요 > 선택x
		 2. DAO > 나중에 시분초 필요 + DAO에서 데이터 가공 안한다. 순수 데이터 입출력 전담. > 선택x
		 3. Servlet => 보통 데이터 조작, 가공은 Servlet이 담당
		 4. JSP > 출력만 전담 > 선택x
		 */
		for (BoardDTO dto : list) {
		
			// 날짜 > 가공
			String regdate = dto.getRegdate();
			regdate = regdate.substring(0, 10);
			dto.setRegdate(regdate);
			
			// 제목이 길면 자르기
			String subject = dto.getSubject();
			
			// 무조건 글 제목과 내용에 들어있는 <scrip>태그는 비활성화
			subject = subject.replace("<script", "&lt;script").replace("</script>", "&lt;/script&gt;");
			dto.setSubject(subject);
			
			if(subject.length() > 30) {
				subject = subject.substring(0, 30) + "...";
				dto.setSubject(subject);
			}
			
		}
		
		
		// 새로고침에 의한 조회 수 증가 방지 티켓
		HttpSession session = req.getSession();
		
		session.setAttribute("read", "n");
		
		
		// 3.
		req.setAttribute("list", list);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		dispatcher.forward(req, resp);
	}

}
