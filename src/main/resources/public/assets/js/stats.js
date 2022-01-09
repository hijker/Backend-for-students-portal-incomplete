let currentUser = '';
const endGame = () => {
    window.open('https://www.youtube.com/watch?v=h6Ol3eprKiw');
}
const fetchBanner = () => {
    $.getJSON('users/info', function (data) {
        currentUser = data;
        const userName = data.name;
        const txt = userName.split(' ')[0] + ", The hunt is on!";
        document.getElementById("displayName").innerHTML = txt;
    });
}

const audioToggle = () => {
    const myAudio = document.getElementById("myAudio");
    if (myAudio.paused) {
        myAudio.play();
        $('#mute').hide();
        $('#vol').show();
    } else {
        myAudio.pause();
        $('#mute').show();
        $('#vol').hide();
    }
}
const musicPlay = () => {
    audioToggle();
    document.removeEventListener('click', musicPlay);
    document.removeEventListener('keydown', musicPlay);
}

const fetchProgress = () => {
    $.getJSON('/progress/leader', function (data) {
        $.each(data, function (progressList) {
            document.getElementById("progressTable").innerHTML = '';
            var progressArray = data[progressList];
            for (var i = 0; i < progressArray.length; i++) {

                var user = progressArray[i].users;

                var questionAnswered = parseInt(progressArray[i].questionNumber);
                var questionAnsweredPercent = questionAnswered === 10 ? (questionAnswered) * 10 + "%" : (questionAnswered - 1) * 10 + "%";
                var rank = "<td width=\"5%\" class=\"cell\"></td>";
                var pic = "<td width=\"5%\" class=\"cell\"><img src=" + user.picture + " alt=" + user.name + " style=\"width:30px;height:30px;margin-top:2px;border-radius:50%\">";
                if (i < 3) {
                    rank = "<td width=\"5%\" class=\"cell\"><img src=\"assets/img/medal-" + i + ".png\" style=\"width:35px;height:35px;border-radius:50%\">";

                }
                var name = "<td width=\"30%\" class=\"cell\" style=\"padding-top: 15px\">" + user.name + "</td>";

                var prog = "<td width=\"40%\" class=\"cell\" style=\"padding-top: 15px\">" + "<div class=\"progress\"> <div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"" + questionAnswered + "\" aria-valuemin=\"0\" aria-valuemax=\"10\" style=\"background-color: #663300;width:" + questionAnsweredPercent + "\"> " + questionAnsweredPercent + " </div></div>" + "</td>";

                var table = "<tr>" + rank + pic + name + prog + "</tr>"
                if (user.email === currentUser.email) {
                    // document.getElementById('questionId').innerHTML = 'Clue: ' + questionAnswered + '/10';
                    if (questionAnswered === 10) {
                        $('#answer').hide();
                        $('#submit-answer').hide();
                    }
                }
                //Showing the table inside tbody
                document.getElementById("progressTable").innerHTML += table;
            }

        });
    });
}

const comment = () => {
    const ans = $('#comment').val();
    if (ans === '') {
        $('#comment').addClass('error');
    } else {
        const url = 'comments/add?message=' + ans;
        fetch(url, {method: "POST"}).then((res) => {
            if (res.status === 200) {
                $('#comment').val('');
                fetchComments();
            }
        });
    }
}

const fetchComments = () => {
    $.getJSON('/comments/all', function (comments) {
        document.getElementById("comments").innerHTML = '';
        for (var i = 0; i < comments.length; i++) {
            var user = comments[i].users;
            var msg = comments[i].message;
            // var pic = "<td width=\"10%\"><img src=" + user.picture + " alt=" + user.name + " style=\"width:50px;height:50px;border-radius:50%\">";
            var name = "<td width=\"20%\" style=\"padding-top: 20px;font-weight: 500\">" + user.name + ": </td>";
            var text = "<td style=\"padding-top: 20px\">" + msg + "</td>";
            var table = "<tr>" + name + text + "</tr>"
            document.getElementById("comments").innerHTML += table;
        }
    });
}

const getImage = () => {
    var outputImg = document.createElement('img');
    outputImg.id = "que";
    outputImg.src = 'questions/image?t=' + new Date().getTime().toString();
    const existingQue = $("#que");
    document.getElementById("questionLink").innerHTML = "";

    console.log(document.getElementById("questionLink"));
    document.getElementById("questionLink").appendChild(outputImg);
    fetchProgress();
    fetchComments();
}
const showCorrectAnimation = () => {
    $('#clue').hide();
    $('#wrong-answer').hide();
    $('#right-answer').show();
    setTimeout(() => {
        $('#clue').show();
        $('#right-answer').hide();
    }, 5000);
}
const showWrongAnimation = () => {
    $('#clue').hide();
    $('#wrong-answer').show();
    $('#right-answer').hide();
    setTimeout(() => {
        $('#clue').show();
        $('#wrong-answer').hide();
    }, 2000);
}
const submitAnswer = () => {
    const ans = $('#answer').val();
    if (ans === '') {
        $('#answer').addClass('error');
    } else {
        $('#wrongAnswer').text('');
        if($('#answer').hasClass('error')) {
            $('#answer').removeClass('error');
        }
        const url = $('#answer').val() ? 'questions/info?answerHash=' + ans : 'questions/info';
        $('#answer').val('');
        fetch(url).then((res) => {
            if (res.status === 200) {
                showCorrectAnimation();
                getImage();
            } else if (res.status === 400) {
                showWrongAnimation();
            } else if (res.status === 429) {
                document.getElementById("wrongAnswer").innerHTML = "Too many attempts. Try again in sometime!";
            } else {
                document.getElementById("wrongAnswer").innerHTML = "Something went wrong. Try Again!";
            }
        }, (err) => {
            document.getElementById("wrongAnswer").innerHTML = "Something went wrong. Try Again!";
        });
    }
}
const answerUpdated = () => {
    document.getElementById("wrongAnswer").innerHTML = "";
    $('#answer').removeClass('error');
}
const handleEnterOnAnswer = () => {
    $('#answer').on('keydown', (event) => {
        if (event.key === "Enter" && !event.handled) {
            // Cancel the default action, if needed
            event.handled = true;
            event.preventDefault();
            submitAnswer();
        }
    })
}

function connect() {
    var socket = new SockJS('/noti-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/notification', function (greeting) {
            console.log(greeting);
            fetchComments();
            fetchProgress();
        });
    });
}

const handleEnterOnComment = () => {
    $('#comment').on('keydown', (event) => {
        if (event.key === "Enter" && !event.handled) {
            // Cancel the default action, if needed
            event.handled = true;
            event.preventDefault();
            comment();
        }
    })
}

window.onload = function () {
    document.addEventListener('click', musicPlay);
    document.addEventListener('keydown', musicPlay);
    init();
    $('#answer').on('change', handleEnterOnAnswer);
    $('#comment').on('change', handleEnterOnComment);
}

const init = () => {
    fetchBanner();
    getImage();
    $('#right-answer').hide();
    $('#wrong-answer').hide();
    $('#mute').show();
    $('#vol').hide();
    connect();
}

const setTimer = function (time, endTime) {
    setInterval(function () {
        var remainingTime = parseInt((endTime - time) / 1000);
        const secs = remainingTime % 60 < 10 ? '0' + (remainingTime % 60) : remainingTime % 60;
        remainingTime = (remainingTime - secs) / 60;
        const minutes = remainingTime % 60 < 10 ? '0' + (remainingTime % 60) : remainingTime % 60;
        remainingTime = (remainingTime - minutes) / 60;
        const hours = remainingTime % 24 < 10 ? '0' + (remainingTime % 24) : remainingTime % 24;
        const days = (remainingTime - hours) / 24 ? '0' + ((remainingTime - hours) / 24) : (remainingTime - hours) / 24;
        let innerText = days > 0 ? days + " : " : '';
        innerText += hours + " : " + minutes + " : " + secs;
        // document.getElementById("timeLeft").innerHTML = innerText;
       if(days > 0) {
           document.getElementById('days').innerHTML = days;
       } else {
           $('#days-container').hide();
       }
        document.getElementById('minutes').innerHTML = minutes;
        document.getElementById('hours').innerHTML = hours;
        document.getElementById('seconds').innerHTML = secs;

        time = time + 1000;
        if(time > endTime) {
            $('#timer-container').hide();
            $('#mid-box').hide();
            window.location.reload();
        }
    }, 1000);
}
let meetingLink ="";
const launchZoom = () => {
    window.open(meetingLink);
}
fetch('/time')
    .then(r => r.json())
    .then(r => {
        var time = r.time;
        meetingLink = r.meetingLink;
        fetch('/config/getsettings')
            .then(r => r.json())
            .then(r => {
                var endTime = r.endTime;
                if (time > endTime) {
                    $('#timer-container').hide();
                    $('#mid-box').hide();
                    $('#launchZoom').show();
                } else {
                    $('#launchZoom').hide();
                    setTimer(time, endTime);
                }
            })
    })