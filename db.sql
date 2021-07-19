-- db.sql

-- 회원 테이블
select * from tblUser;

-- 게시판 테이블 > 단계 + 확장
-- 기본 계시판
drop table tblBorad;
drop sequence seqBoard;

create table tblBoard (
    seq number primary key,                                 -- 글번호(PK)
    id varchar2(30) not null references tblUser(id),        -- 아이디(FK)
    subject varchar2(500) not null,                         -- 제목
    content varchar2(500) not null,                         -- 내용
    regdate date default sysdate not null,                  -- 작성시간
    readcount number default 0 not null,                    -- 조회수
    tag varchar2(1) not null check(tag in ('y', 'n'))       -- 글 내용에 HTML 태그 허용 유무    
);

create sequence seqBoard;

commit;

select seq, (select name from tblUser where id = tblBoard.id) as name, subject, readcount, regdate from tblBoard order by seq desc;

create or replace view vwBoard
as
    select
    seq,
    id,
    (select name from tblUser where id = b.id) as name,
    subject,
    readcount,
    regdate,
    sysdate - regdate as isnew
    from tblBoard b;

select * from tblBoard;
select * from vwBoard;