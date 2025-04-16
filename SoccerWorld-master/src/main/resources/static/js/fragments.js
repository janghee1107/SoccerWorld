let leagueList = document.getElementById('leagueList');
let leagueScheduleList = document.getElementById('leagueScheduleList');
let teamList = document.getElementById('teamList');
let eplList = document.getElementById('eplList');
let laLigaList = document.getElementById('laLigaList');
let serieAList = document.getElementById('serieAList');
let bundesligaList = document.getElementById('bundesligaList');
let isTeamInfoVisible = false;
let isLeagueScheduleVisible = false;

//모든 리소스 로드 후 실행
window.onload = function() {
    hideAll();
};

function hideAll() {
    leagueList.style.display = 'none';
    eplList.style.display = 'none';
    laLigaList.style.display = 'none';
    serieAList.style.display = 'none';
    bundesligaList.style.display = 'none';
    //리그순위정보 테이블 감춤
    leagueScheduleList.style.display = 'none';
}

function showTeams(name) {
    if(name == 'EPL') {
        eplList.style.display = 'block';
        laLigaList.style.display = 'none';
        serieAList.style.display = 'none';
        bundesligaList.style.display = 'none';

    } else if(name == 'LALIGA') {
        laLigaList.style.display = 'block';
        eplList.style.display = 'none';
        serieAList.style.display = 'none';
        bundesligaList.style.display = 'none';
    } else if(name == 'SERIEA') {
        serieAList.style.display = 'block';
        eplList.style.display = 'none';
        laLigaList.style.display = 'none';
        bundesligaList.style.display = 'none';
    } else {
        bundesligaList.style.display = 'block';
        eplList.style.display = 'none';
        laLigaList.style.display = 'none';
        serieAList.style.display = 'none';
    }
}

function showLeagues() {
    if(!isTeamInfoVisible) {
        hideAll();
        leagueList.style.display = 'table';
        isTeamInfoVisible = true;
    }
    else {
        hideAll();
        isTeamInfoVisible = false;
    }
}

function showLeagueSchedules() {
    if(!isLeagueScheduleVisible) {
        hideAll();
        leagueScheduleList.style.display = 'table';
        isLeagueScheduleVisible = true;
    } else {
        hideAll();
        isLeagueScheduleVisible = false;
    }
}