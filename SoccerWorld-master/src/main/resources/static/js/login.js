let wrapper = document.getElementById("wrapper");
let x = document.getElementById("login");
let y = document.getElementById("register");
let z = document.getElementById("btn");
let register = document.getElementById("register");
let leagueSelector = document.getElementById("leagueSelector");
let teamSelector = document.getElementById('teamSelector');
let idChecked = false;
let nickChecked = false;

document.addEventListener('DOMContentLoaded', function() {
    // 현재 URL에서 파라미터 가져오기
    const params = new URLSearchParams(window.location.search);

    // 특정 파라미터 값 가져오기
    const pageType = params.get("page-type");

    //페이지 요청에 따른 전환
    if(pageType === "login") goLogin();
    else if(pageType === "register") goRegister();


    leagueSelector.addEventListener('change', function () {
        //선택된 option 값 가져오기
        const leagueName = leagueSelector.value;

        //미선택 시
        if(leagueName != "") {
            //option을 넣기 전에 초기화
            teamSelector.innerHTML = '';
            //선택 시 해당 정보를 비동기로 서버에 전송
            fetch(`/teams/league/${leagueName}`)
                .then(response => response.json())
                .then(data => {
                    //teamList에서 team 하나씩 접근
                    data.forEach(team => {
                        //option 요소 생성
                        const option = document.createElement('option');
                        option.textContent = team.name;
                        option.value = team.teamId;

                        //select box에 넣기
                        teamSelector.appendChild(option);
                    })
                })
                .catch(error => {
                    console.error(`Error fetching get teamList:`, error);
                });
        } else {
            //팀 selectbox 초기화
            teamSelector.innerHTML = '';
        }
    })
});

function goLogin(){
    x.style.left = "50px";
    y.style.left = "450px";
    y.style.width = "280px";
    z.style.left = "0";
    wrapper.style.width= "380px";
    wrapper.style.height= "480px";
}
function goRegister(){
    x.style.left = "-400px";
    y.style.left = "50px";
    y.style.width = "600px";
    z.style.left = "110px";
    wrapper.style.width= "700px";
    wrapper.style.height= "1100px";
}

function setStyle(event) {
    if(event.target.checked)  {
        if(event.target.id == "modern") {
            classic.checked=false;
            upcycling.checked=false;
            styleId = event.target.value;
        } else if(event.target.id == "classic") {
            modern.checked=false;
            upcycling.checked=false;
            styleId = event.target.value;
        } else if(event.target.id == "upcycling") {
            modern.checked=false;
            classic.checked=false;
            styleId = event.target.value;
        }
        register.action = "/member/register/"+styleId;
    }
}

function idCheck() {
    const formId = document.getElementById("id");
    fetch(`/member/id/${formId.value}`)
        .then(response => response.text())
        .then(data => {
            if(!data || data.trim() === "") {
                alert("사용 가능한 아이디입니다");
                idChecked = true;
            } else {
                alert("이미 존재하는 아이디입니다");
            }
        })
}

function nickCheck() {
    const nickname = document.getElementById("nickname");
    fetch(`/member/nickname/${nickname.value}`)
        .then(response => response.text())
        .then(data => {
            if(!data || data.trim() === "") {
                alert("사용 가능한 닉네임입니다");
                nickChecked = true;
            } else {
                alert("이미 존재하는 닉네임입니다");
            }
        })
}

function registerSubmit() {
    if(idChecked && nickChecked) {
        const form = document.getElementById("register");
        form.submit();
    } else {
        alert("아이디 혹은 닉네임 중복확인을 먼저 해주세요");
    }
}