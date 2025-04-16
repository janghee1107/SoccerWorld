const leagueNames = document.querySelector('.league-names');
const leagueName = leagueNames.querySelectorAll('td');
const eplTitle = leagueName[0];
const laligaTitle = leagueName[1];
const serieaTitle = leagueName[2];
const bundesligaTitle = leagueName[3];

let eplRanking = document.getElementById('eplRanking');
let laligaRanking = document.getElementById('laligaRanking');
let serieaRanking = document.getElementById('serieaRanking');
let bundesligaRanking = document.getElementById('bundesligaRanking');


document.addEventListener('DOMContentLoaded', function() {
    laligaRanking.style.display = 'none';
    serieaRanking.style.display = 'none';
    bundesligaRanking.style.display = 'none';
    eplTitle.setAttribute('style', 'color: #1e1c54 !important;');
});


function changeLeague(leagueName) {
    if(leagueName=="EPL") {
        eplRanking.style.display = 'table-row-group';
        laligaRanking.style.display = 'none';
        serieaRanking.style.display = 'none';
        bundesligaRanking.style.display = 'none';
        eplTitle.setAttribute('style', 'color: #1e1c54 !important;');
        serieaTitle.setAttribute('style', 'color: #7fb6ff; !important;');
        laligaTitle.setAttribute('style', 'color: #7fb6ff; !important;');
        bundesligaTitle.setAttribute('style', 'color: #7fb6ff; !important;');

    } else if(leagueName=="LALIGA") {
        eplRanking.style.display = 'none';
        laligaRanking.style.display = 'table-row-group';
        serieaRanking.style.display = 'none';
        bundesligaRanking.style.display = 'none';
        eplTitle.setAttribute('style', 'color: #7fb6ff !important;');
        serieaTitle.setAttribute('style', 'color: #7fb6ff; !important;');
        laligaTitle.setAttribute('style', 'color: #1e1c54; !important;');
        bundesligaTitle.setAttribute('style', 'color: #7fb6ff; !important;');

    } else if(leagueName=="SERIEA") {
        eplRanking.style.display = 'none';
        laligaRanking.style.display = 'none';
        serieaRanking.style.display = 'table-row-group';
        bundesligaRanking.style.display = 'none';
        eplTitle.setAttribute('style', 'color: #7fb6ff !important;');
        serieaTitle.setAttribute('style', 'color: #1e1c54; !important;');
        laligaTitle.setAttribute('style', 'color: #7fb6ff; !important;');
        bundesligaTitle.setAttribute('style', 'color: #7fb6ff; !important;');

    } else {
        eplRanking.style.display = 'none';
        laligaRanking.style.display = 'none';
        serieaRanking.style.display = 'none';
        bundesligaRanking.style.display = 'table-row-group';
        eplTitle.setAttribute('style', 'color: #7fb6ff !important;');
        serieaTitle.setAttribute('style', 'color: #7fb6ff; !important;');
        laligaTitle.setAttribute('style', 'color: #7fb6ff; !important;');
        bundesligaTitle.setAttribute('style', 'color: #1e1c54; !important;');
    }
}