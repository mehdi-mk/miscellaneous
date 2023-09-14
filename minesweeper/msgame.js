/* ================================================ */
/* ================================================ */
/* Mehdi Karamollahi - UCID: 30090208 ============= */
/* Assignment 2 =================================== */
/* SENG 513 - Web-based Systems =================== */
/* Professor: Pavol Federl ======================== */
/* TA: Emmanuel Onu =============================== */
/* University of Calgary ========================== */
/* Fall 2020 ====================================== */

/* ================================================ */
/* ================================================ */

/* The game engine has been taken from the instance */
/* provided by the instructor. The key code regard- */
/* ing the buttons (game field) and timer have been */
/* taken from the code snippets provided by the TA. */
/* The announcement messages (win / lose) have also */
/* been taken from the lights-out game implemented  */
/* by the instructor. Other parts have been written */
/* by the student and certain changes have been ma- */
/* de to all parts for the game to be working prop- */
/* erly. ========================================== */

"use strict";

let MSGame = (function(){

    // private constants
    const STATE_HIDDEN = "hidden";
    const STATE_SHOWN = "shown";
    const STATE_MARKED = "marked";

    function array2d( nrows, ncols, val) {
        const res = [];
        for( let row = 0 ; row < nrows ; row ++) {
            res[row] = [];
            for( let col = 0 ; col < ncols ; col ++)
                res[row][col] = val(row,col);
        }
        return res;
    }

    // returns random integer in range [min, max]
    function rndInt(min, max) {
        [min,max] = [Math.ceil(min), Math.floor(max)]
        return min + Math.floor(Math.random() * (max - min + 1));
    }

    class _MSGame {
        constructor() {
            this.init(8,10,10); // easy
        }

        validCoord(row, col) {
            return row >= 0 && row < this.nrows && col >= 0 && col < this.ncols;
        }

        init(nrows, ncols, nmines) {
            this.nrows = nrows;
            this.ncols = ncols;
            this.nmines = nmines;
            this.nmarked = 0;
            this.nuncovered = 0;
            this.exploded = false;
            // create an array
            this.arr = array2d(
                nrows, ncols,
                () => ({mine: false, state: STATE_HIDDEN, count: 0}));
        }

        count(row,col) {
            const c = (r,c) =>
                (this.validCoord(r,c) && this.arr[r][c].mine ? 1 : 0);
            let res = 0;
            for( let dr = -1 ; dr <= 1 ; dr ++ )
                for( let dc = -1 ; dc <= 1 ; dc ++ )
                    res += c(row+dr,col+dc);
            return res;
        }

        sprinkleMines(row, col) {
            // prepare a list of allowed coordinates for mine placement
            let allowed = [];
            for(let r = 0 ; r < this.nrows ; r ++ ) {
                for( let c = 0 ; c < this.ncols ; c ++ ) {
                    if(Math.abs(row-r) > 2 || Math.abs(col-c) > 2)
                        allowed.push([r,c]);
                }
            }
            this.nmines = Math.min(this.nmines, allowed.length);
            for( let i = 0 ; i < this.nmines ; i ++ ) {
                let j = rndInt(i, allowed.length-1);
                [allowed[i], allowed[j]] = [allowed[j], allowed[i]];
                let [r,c] = allowed[i];
                this.arr[r][c].mine = true;
            }
            // erase any marks (in case user placed them) and update counts
            for(let r = 0 ; r < this.nrows ; r ++ ) {
                for( let c = 0 ; c < this.ncols ; c ++ ) {
                    if(this.arr[r][c].state == STATE_MARKED)
                        this.arr[r][c].state = STATE_HIDDEN;
                    this.arr[r][c].count = this.count(r,c);
                }
            }
            let mines = []; let counts = [];
            for(let row = 0 ; row < this.nrows ; row ++ ) {
                let s = "";
                for( let col = 0 ; col < this.ncols ; col ++ ) {
                    s += this.arr[row][col].mine ? "B" : ".";
                }
                s += "  |  ";
                for( let col = 0 ; col < this.ncols ; col ++ ) {
                    s += this.arr[row][col].count.toString();
                }
                mines[row] = s;
            }
            console.log("Mines and counts after sprinkling:");
            console.log(mines.join("\n"), "\n");
        }

        // uncovers a cell at a given coordinate
        // this is the 'left-click' functionality
        uncover(row, col) {
            console.log("uncover", row, col);
            // if coordinates invalid, refuse this request
            if( ! this.validCoord(row,col)) return false;
            // if this is the very first move, populate the mines, but make
            // sure the current cell does not get a mine
            if( this.nuncovered === 0)
                this.sprinkleMines(row, col);
            // if cell is not hidden, ignore this move
            if( this.arr[row][col].state !== STATE_HIDDEN) return false;
            // floodfill all 0-count cells
            const ff = (r,c) => {
                if( ! this.validCoord(r,c)) return;
                if( this.arr[r][c].state !== STATE_HIDDEN) return;
                this.arr[r][c].state = STATE_SHOWN;
                this.nuncovered ++;
                if( this.arr[r][c].count !== 0) return;
                ff(r-1,c-1);ff(r-1,c);ff(r-1,c+1);
                ff(r  ,c-1);         ;ff(r  ,c+1);
                ff(r+1,c-1);ff(r+1,c);ff(r+1,c+1);
            };

            ff(row,col);
            // have we hit a mine?
            if( this.arr[row][col].mine) {
                this.exploded = true;
            }
            return true;
        }

        // puts a flag on a cell
        // this is the 'right-click' or 'long-tap' functionality
        mark(row, col) {
            console.log("mark", row, col);
            // if coordinates invalid, refuse this request
            if( ! this.validCoord(row,col)) return false;
            // if cell already uncovered, refuse this
            console.log("marking previous state=", this.arr[row][col].state);
            if( this.arr[row][col].state === STATE_SHOWN) return false;
            // accept the move and flip the marked status
            this.nmarked += this.arr[row][col].state == STATE_MARKED ? -1 : 1;
            this.arr[row][col].state = this.arr[row][col].state == STATE_MARKED ? STATE_HIDDEN : STATE_MARKED;
            return;
        }

        getRendering() {
            const res = [];
            let s = "";
            let i = 0;
            for( let row = 0 ; row < this.nrows ; row ++) {
                for( let col = 0 ; col < this.ncols ; col ++ ) {
                    let a = this.arr[row][col];
                    if( this.exploded && a.mine) s = "M";
                    else if( a.state === STATE_HIDDEN) s = "H";
                    else if( a.state === STATE_MARKED) s = "F";
                    else if( a.mine) s = "M";
                    else s = a.count.toString();
                    res[i] = s;
                    i++;
                }
            }
            return res;
        }

        getStatus() {
            let done = this.exploded ||
                this.nuncovered === this.nrows * this.ncols - this.nmines;
            return {
                done: done,
                exploded: this.exploded,
                nrows: this.nrows,
                ncols: this.ncols,
                nmarked: this.nmarked,
                nuncovered: this.nuncovered,
                nmines: this.nmines,
                nflags: this.nflags
            }
        }
    }

    return _MSGame;

})();

let startBtn = document.getElementById('startBtn');
let headings = document.getElementById('header');
let remainingFlags = document.getElementById("status");
let chooseLevel = document.getElementById('levelCheck');
chooseLevel.style.display = 'none';
let menuBtn1 = document.getElementById('menu1');
let menuBtn2 = document.getElementById('menu2');
let menuBtn3 = document.getElementById('menu3');
menuBtn3.style.display = 'none';
remainingFlags.style.display = 'none';

let game = new MSGame();
let gamePlay = [];
let container = document.getElementById('btnContainer');
let button = [];
let newButton = [];
let nrows = 0;
let ncols = 0;
let nbuttons = 0;
let nmines = 0;
let nflags = 0;

let t = 0;
let m = 0;
let timer = null;

//Sounds
let soundShovel = new Audio("files/shovel.mp3");
let soundFlipping = new Audio("files/gravel-flipping.mp3");
let soundExplosion = new Audio("files/explosion.mp3");
let soundFlag = new Audio("files/flag.mp3");
let soundDislodge = new Audio("files/dislodge.mp3");
let soundTada = new Audio("files/tada.mp3");

function easyGame() {
    nrows = 8;
    ncols = 10;
    nbuttons = 80;
    nmines = 10;
    nflags = nmines;
    renew();

}

function hardGame() {
    nrows = 14;
    ncols = 18;
    nbuttons = 252;
    nmines = 40;
    nflags = nmines;
    renew();
}

function renew() {
    for( let row = 0 ; row < nrows ; row ++) {
        for( let col = 0 ; col < ncols ; col ++ ) {
            game.arr[row][col].state = 'hidden';
            game.arr[row][col].mine = false;
            game.arr[row][col].count = 0;
        }
    }
    game.nmarked = 0;
    game.nuncovered = 0;
    game.exploded = false;
    for (let i = 0; i < nbuttons; i++) {
        newButton[i].innerHTML = " ";
    }
    gamePlay = game.getRendering();
    nflags = nmines;
    coloring();
    for (let i = 0; i < nbuttons; i++) {
        button[i] = newButton[i];
    }
    timerStop();
    timerReset();
    document.querySelectorAll(".remainingMines").forEach(
        (e)=> {
            e.textContent = String(nflags);
        });
};

function playing() {

    if(nbuttons !== 0) {
        startBtn.style.display = 'none';
        headings.style.display = 'none';
        remainingFlags.style.display = 'block';
        chooseLevel.style.display = 'none';
        menuBtn1.style.display = 'none';
        menuBtn2.style.display = 'none';
        menuBtn3.style.display = 'block';
    }
    else {
        chooseLevel.style.display = 'block';
    }
    game.init(nrows, ncols, nmines);
    gamePlay = game.getRendering();

    // nbuttons = 80;
    // ncols = 10;
    // nrows = Math.ceil(nbuttons / ncols);
    let buttonSize = container.clientWidth / ncols;

    container.style.gridTemplateColumns = `repeat(${ncols}, ${buttonSize}px)`;
    container.style.gridTemplateRows = `repeat(${nrows}, ${buttonSize}px)`;

    /*========Making the boxes matrix for the first time======*/

    //Dynamic button creation
    for(let i = 0; i < nbuttons; i++){
        button[i] = document.createElement('button');
        button[i].innerHTML = " ";
        button[i].classList.add("btn");
        button[i].dataset.key = i;
        button[i].style.border = "thin #00000000";

        /*===============Coloring the boxes========*/
        if ((Math.floor(button[i].dataset.key / ncols) % 2 === 0 && button[i].dataset.key % 2 === 0) || (Math.floor(button[i].dataset.key / ncols) % 2 === 1 && button[i].dataset.key % 2 === 1)) {
            button[i].style.background='#81b622aa'
        } else {
            button[i].style.background='#81b622cc'
        }
        container.append(button[i]);
    }
    document.querySelectorAll(".remainingMines").forEach(
        (e)=> {
            e.textContent = String(nflags);
        });
    console.log("start");
};

//Button click event handler using a delegate
container.addEventListener('click',function(e){
    if(e.target && e.target.classList.contains("btn")) {
        //If the game is still one
        if(!game.getStatus().done) {
            let tbrow = Math.floor(e.target.dataset.key / ncols);
            let tbcol = (e.target.dataset.key % ncols);
            if (tbcol === -1) {
                tbcol = 9;
            }

            if (game.getStatus().nuncovered === 0) {
                timerStart();
            }

            game.uncover(tbrow , tbcol);
            gamePlay = game.getRendering();
            if (gamePlay[e.target.dataset.key] !== 'H' && gamePlay[e.target.dataset.key] !== 'M' && gamePlay[e.target.dataset.key] !== '0') {
                soundShovel.play();
            }
            else if (gamePlay[e.target.dataset.key] === '0') {
                soundFlipping.play();
            }
            else if (gamePlay[e.target.dataset.key] === 'M') {
                soundExplosion.play();
            }
            coloring();

            //Making the current state of boxes as the old one for the next click
            for (let i = 0; i < nbuttons; i++) {
                button[i] = newButton[i];
            }
            if (game.getStatus().exploded) {
                document.querySelector("#overlay1").classList.toggle("active");
                timerStop();
            }
            if (game.getStatus().done && !game.getStatus().exploded) {
                document.querySelector("#overlay").classList.toggle("active");
                timerStop();
                soundTada.play();
            }
        }
    }
});

container.oncontextmenu = function(event) {
    if (!game.getStatus().done && game.getStatus().nuncovered !== 0) {
        // if(event.target && event.target.classList.contains("btn")) {
        event.preventDefault();
        rightClick(event);
        // }
    }
};

$(function(){
    if (!game.getStatus().done && game.getStatus().nuncovered !== 0) {
        $(container).bind( "taphold", tapholdHandler );
        console.log("first taphold function");
        function tapholdHandler( event ){
            rightClick(event);
            console.log("second taphold function");
        }
    }
});

function rightClick(event) {
    let tbrow = Math.floor(event.target.dataset.key / ncols);
    let tbcol = (event.target.dataset.key % ncols);
    if (tbcol === -1)
        tbcol = 9;

    game.mark(tbrow, tbcol);
    gamePlay = game.getRendering();
    if (gamePlay[event.target.dataset.key] === 'F') {
        soundFlag.play();
        nflags--;
    }
    else if (gamePlay[event.target.dataset.key] === 'H') {
        soundDislodge.play();
        nflags++;
    }
    for (let i = 0; i < nbuttons; i++) {
        newButton[i].innerHTML = '';
    }
    coloring();
    //Making the current state of boxes as the old one for the next click
    for (let i = 0; i < nbuttons; i++) {
        button[i] = newButton[i];
    }

    document.querySelectorAll(".remainingMines").forEach(
        (e)=> {
            e.textContent = String(nflags);
        });

    return;
};

//After user wins the game, when they click on the screen the game gets regenerated
document.querySelector("#overlay").addEventListener("click", () => {
    document.querySelector("#overlay").classList.remove("active");
    renew();
});

//After user loses the game, when they click on the screen the game gets regenerated
document.querySelector("#overlay1").addEventListener("click", () => {
    document.querySelector("#overlay1").classList.remove("active");
    renew();
});


function coloring() {
    //Changing the boxes matrix based on the new rendering
    for(let i = 0; i < nbuttons; i++){
        newButton[i] = document.createElement('button');
        if (gamePlay[i] === '0') {
            newButton[i].innerHTML = "";
        } else if(gamePlay[i] === '1') {
            newButton[i].innerHTML = "<img src='files/one.svg' class=\"image\">";
        } else if(gamePlay[i] === '2') {
            newButton[i].innerHTML = "<img src='files/two.svg' class=\"image\">";
        } else if(gamePlay[i] === '3') {
            newButton[i].innerHTML = "<img src='files/three.svg' class=\"image\">";
        } else if(gamePlay[i] === '4') {
            newButton[i].innerHTML = "<img src='files/four.svg' class=\"image\">";
        } else if(gamePlay[i] === '5') {
            newButton[i].innerHTML = "<img src='files/five.svg' class=\"image\">";
        } else if(gamePlay[i] === '6') {
            newButton[i].innerHTML = "<img src='files/six.svg' class=\"image\">";
        } else if(gamePlay[i] === '7') {
            newButton[i].innerHTML = "<img src='files/seven.svg' class=\"image\">";
        } else if(gamePlay[i] === '8') {
            newButton[i].innerHTML = "<img src='files/eight.svg' class=\"image\">";
        } else if(gamePlay[i] === 'M')  {
            newButton[i].innerHTML = "<img src='files/bomb.svg' class=\"image\">";
        } else {
            newButton[i].innerHTML = " ";
        }
        newButton[i].style.border = "0px #00000000";

        /*===============Coloring the boxes========*/

        //Coloring the boxes with light colors when they have either even rows and even columns, or odd rows and odd columns.
        if ((Math.floor(button[i].dataset.key / ncols) % 2 === 0 && button[i].dataset.key % 2 === 0) || (Math.floor(button[i].dataset.key / ncols) % 2 === 1 && button[i].dataset.key % 2 === 1)) {

            //coloring the hidden boxes with light green
            if (gamePlay[i] === 'H') {
                newButton[i].style.background='#81b622aa';

                //coloring the borders based on the unhidden neighbors
                if (gamePlay[i-1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] !== 'H') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] !== 'H') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
            else if (gamePlay[i] === 'F') {
                newButton[i].style.backgroundImage="url('files/flag-light.png')";
                newButton[i].style.backgroundSize="100% 100%";

                //coloring the borders based on the unhidden neighbors
                if (gamePlay[i-1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] !== 'H') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] !== 'H') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
            else if (gamePlay[i] === 'M') {
                newButton[i].style.background='#91592cdd';

                //coloring the borders based on the unhidden neighbors
                if (gamePlay[i-1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] !== 'H') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] !== 'H') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
            //coloring the unhidden boxes with light cream
            else {
                newButton[i].style.background='#e5c29fbb';

                //coloring the borders based on the hidden neighbors
                if (gamePlay[i-1] === 'H' || gamePlay[i-1] === 'F') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }
                }
                if (gamePlay[i+1] === 'H' || gamePlay[i+1] === 'F') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }
                }
                if (gamePlay[i-ncols] === 'H' || gamePlay[i-ncols] === 'F') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] === 'H' || gamePlay[i+ncols] === 'F') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }

            //Coloring the other boxes with dark colors
        } else {
            //coloring the hidden boxes with dark green
            if (gamePlay[i] === 'H') {
                newButton[i].style.background='#81b622cc';

                //coloring the borders based on the unhidden neighbors
                if (gamePlay[i-1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] !== 'H') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] !== 'H') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
            else if (gamePlay[i] === 'F') {
                newButton[i].style.backgroundImage="url('files/flag-dark.png')";
                newButton[i].style.backgroundSize="100% 100%";

                //coloring the borders based on the unhidden neighbors
                if (gamePlay[i-1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] !== 'H') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] !== 'H') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
            else if (gamePlay[i] === 'M') {
                newButton[i].style.background='#91592cdd';

                //coloring the borders based on the unhidden neighbors
                if (gamePlay[i-1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] !== 'H') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] !== 'H') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] !== 'H') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
            else {
                //coloring the unhidden boxes with dark cream
                newButton[i].style.background='#c6a788bb';

                //coloring the borders based on the hidden neighbors
                if (gamePlay[i-1] === 'H' || gamePlay[i-1] === 'F') {
                    if ((button[i].dataset.key % ncols) !== 0) {
                        newButton[i].style.borderLeft = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i+1] === 'H' || gamePlay[i+1] === 'F') {
                    if ((button[i].dataset.key % ncols) !== (ncols-1)) {
                        newButton[i].style.borderRight = "1px solid #4a752c";
                    }                    }
                if (gamePlay[i-ncols] === 'H' || gamePlay[i-ncols] === 'F') {
                    newButton[i].style.borderTop = "1px solid #4a752c";
                }
                if (gamePlay[i+ncols] === 'H' || gamePlay[i+ncols] === 'F') {
                    newButton[i].style.borderBottom = "1px solid #4a752c";
                }
            }
        }
        newButton[i].classList.add("btn");
        newButton[i].dataset.key = i;
        button[i].replaceWith(newButton[i]);
    }
};

function timerStart() {
    timer = setInterval(function(){
        t++;
        if (t >= 60) {
            m = t/60;
            t = t%t;
            if (m < 10) {
                m = '0' + m;
            }
        }
        if (t < 10) {
            t = '0' + t;
        }

        document.getElementById("timer").innerHTML = (m + ':' + t);
    }, 1000);
}

function timerStop(){
    if(timer) window.clearInterval(timer);
}

function timerReset() {
    t = '0' + 0;
    m = '0' + 0;
    timer = null;
    document.getElementById("timer").innerHTML = (m + ':' + t);
}