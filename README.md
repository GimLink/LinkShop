# LinkShop 쇼핑몰 프로젝트

<p>쇼핑몰 개인 프로젝트입니다.</p>
<p>프런트엔드 지식 이슈로 html 및 css 등은 이것저것 참고해서 긁어왔습니다.</p>

</br>

<h1>배포환경</h1>
<div>
    <ul>
        <li>AWS (비용 문제로 배포 중단)</li>
    </ul>
</div>
<br/>
<h1>사용한 기술스택</h1>
<div>
    <ul>
        <li>JAVA11</li>
        <li>Spring Boot</li>
        <li>Spring Security</li>
        <li>Spring Data Jpa</li>
        <li>Querydsl</li>
        <li>Thymeleaf</li>
        <li>Mysql</li>
    </ul>
</div>
<br/>
<br/>
<h1>구현 기능</h1>
<div>
    <ul>
        <li>
            <strong>계정 관련</strong>
            <ul>
                <li>로그인,로그아웃,회원가입,회원탈퇴</li>
                <li>ID찾기</li>
                <li>회원가입시 문자 인증(cool sms)</li>
                <li>MyPage(주문 목록, 작성한 QnA목록)</li>
                <li>관리자 페이지를 통한 상품 조회,배송,주문관리</li>
            </ul>
        </li>
        <li>
            <strong>게시판</strong>
            <ul>
                <li>게시글 CRUD</li>
                <li>비동기 댓글, 대댓글 CRUD 및 공개,비공개 설정</li>
                <li>버튼 페이징</li>
            </ul>
        </li>
        <li>
            <strong>상품</strong>
            <ul>
                <li>상품 검색</li>
                <li>카테고리별 상품 조회</li>
                <li>noOffset 페이징</li>
                <li>상품 등록,삭제</li>
                <li>최신순,오래된순 정렬</li>
                <li>리뷰, QnA, QnA 관리자 답글</li>
            </ul>
        </li>
        <li>
            <strong>장바구니</strong>
            <ul>
                <li>추가, 수량변경, 개별주문, 선택주문</li>
            </ul>
        </li>
        <li>
            <strong>결제</strong>
            <ul>
                <li>가상계좌</li>
            </ul>
        </li>
    </ul>
</div>
<br/>
<br/>
<h1>이미지</h1>
<p>상품 이미지는 AWS S3 이용했으나 비용문제로 임시 이미지로 대체했습니다.</p>
<br/>

<div>
    <ul>
        <li>
            <strong>메인 페이지</strong>
            <ul>
                <li>카테고리별 상품 조회 가능</li>
                <li>더보기 버튼 (noOffset 페이징)</li>
                <li>
                    <img width="1469" alt="image" src="https://github.com/user-attachments/assets/4b80f9cc-51fe-4934-8be0-70d638c2f37d">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>회원가입</strong>
            <ul>
                <li>문자인증 기능, 아이디 중복검사</li>
                <li>프론트, 서버 모두 유효성 검사</li>
                <li>
                    <img width="1468" alt="image" src="https://github.com/user-attachments/assets/fc85db82-6d14-4815-996d-9aa526efb251">
                    <img width="1466" alt="image" src="https://github.com/user-attachments/assets/f4e64f43-0a76-4200-b9cf-de7fde5f3345">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>로그인</strong>
            <ul>
                <li>카카오 로그인 추후 구현 예정입니다.</li>
                <li>프론트, 서버 모두 유효성 검사</li>
                <li>
                    <img width="1468" alt="image" src="https://github.com/user-attachments/assets/ccc881d6-8961-4592-aad9-c4f053ff3d49">
                    <img width="1464" alt="image" src="https://github.com/user-attachments/assets/a3a23048-5c9a-4502-b499-98cf13ad771f">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>MyPage</strong>
            <ul>
                <li>주문, QnA버튼 클릭시 비동기로 내용 가져오게끔 구현</li>
                <li>회원탈퇴 기능</li>
                <li>주문상세보기 클릭시 해당 주문 관련 정보들 표시</li>
                <li>더보기 버튼 (noOffset 페이징)</li>
                <li>
                    <img width="1468" alt="image" src="https://github.com/user-attachments/assets/bb6c8c9a-20a9-4559-b387-390a1c6c95ea">
                </li>
            </ul>
        </li>
        <br/>
         <li>
            <strong>자유게시판</strong>
            <ul>
                <li>기본적인 CRUD</li>
                <li>댓글, 대댓글 CRUD, 비공개 여부 설정가능</li>
                <li>작성자 아이디 마스킹처리</li>
                <li>페이징</li>
                <li>
                    <img width="1461" alt="image" src="https://github.com/user-attachments/assets/aa2b1e1c-78d6-4bb5-8d89-89510acc41dd">
                </li>
                <li>
                    <img width="1468" alt="image" src="https://github.com/user-attachments/assets/4bb6fa1e-743e-48d9-b3fc-572d046c464d">
                </li>
            </ul>
        </li>
        <br/>
        <br/>
        <li>
            <strong>상품, 상품리뷰, 상품 QnA</strong>
            <ul>
                <li>수량 변경 가능</li>
                <li>리뷰 : 상품 구매시에만 리뷰 작성 가능, 정렬 기능</li>
                <li>QnA : 비공개여부 선택 가능, 관리자만 답글 가능</li>
                <li>일반 페이징, noOffset 페이징</li>
                <li>
                    <img width="1468" alt="image" src="https://github.com/user-attachments/assets/960f665a-1f06-409c-bc79-85d33248352f">
                  <img width="1459" alt="image" src="https://github.com/user-attachments/assets/2f21bfff-2723-4c8e-83b0-ecf612428324">
                </li>
                <li>
                    <img width="736" alt="image" src="https://github.com/user-attachments/assets/2d987d08-bba4-44d6-bb96-c6eae4a644bd">
                </li>
                <li>
                    <img width="1469" alt="image" src="https://github.com/user-attachments/assets/98bb888f-f67a-435b-926a-8d00f9984073">
                </li>
            </ul>
        </li>
        <br/>
        <br/>
        <li>
            <strong>장바구니</strong>
            <ul>
                <li>개별 혹은 선택주문</li>
                <li>비동기 수량변경, 삭제 가능</li>
                <li>
                    <img width="1470" alt="image" src="https://github.com/user-attachments/assets/84f7f65d-19d1-4f1d-b99e-f169a5fe685a">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>상품 주문</strong>
            <ul>
                <li>구매하려는 상품 목록 표시</li>
                <li>구매 클릭시 서버에서 배송정보 유효성검사후 결제 진행</li>
                <li>가상계좌 결제 가능 (카카오페이 결제 카카오 회원가입, 로그인과 함께 추가할 예정입니다.</li>
                <li>
                    <img width="1463" alt="image" src="https://github.com/user-attachments/assets/bceb5477-5c3e-4aba-87a9-edeb4847e3cd">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>관리자 페이지 메인</strong>
            <ul>
                <li>최근 가입한 유저목록, 주문 목록 조회</li>
                <li>
                    <img width="734" alt="스크린샷 2024-07-29 16 48 24" src="https://github.com/user-attachments/assets/9b049547-f95a-4217-a3c5-c877c20444c7">
                </li>
            </ul>
        </li>
        <br/>
        <li>
            <strong>관리자 페이지 주문목록</strong>
            <ul>
                <li>주문상태 변경 기능</li>
                <li>동적 검색</li>
                <li>
                    <img width="735" alt="image" src="https://github.com/user-attachments/assets/771b0601-066e-4feb-a950-6f824ca083d2">
                </li>
            </ul>
        </li>
        <br/>
        <br/>
</div>
<br/>
<br/>
