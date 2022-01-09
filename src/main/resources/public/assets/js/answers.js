$.getJSON('/answers/stats', function (data) {
    showAnswer(data);
});

function showAnswer(data) {
    document.getElementById("answerData").innerHTML = "";

    var answersArray = data;
    $('#noResults').hide();
    $('#answerData').show();
    if(answersArray.length > 0) {
       for (var i = 0; i < answersArray.length; i++) {

           var email = answersArray[i].email;
           var question = parseInt(answersArray[i].questionNumber);
           var answer = answersArray[i].givenAnswer;
           var time = new Date(answersArray[i].time);

           //Creating table
           var emailElem = "<td width=\"30px\">" + email + "</td>";
           var quesElem = "<td width=\"30px\">" + question + "</td>";
           var ansElem = "<td width=\"30px\">" + answer + "</td>";
           var timeElem = "<td width=\"30px\">" + time + "</td>";

           var table = "<tr>" + emailElem + quesElem + ansElem + timeElem + "</tr>"

           //Showing the table inside the body
           document.getElementById("answerData").innerHTML += table;
       }
   } else {
        $('#noResults').show();
        $('#answerData').hide();
   }
}

function getAnswer() {
    const ans = $('#qn').val();
    const email = $('#email').val();
    let url = ans ? 'answers/stats?questionNo=' + ans : 'answers/stats?';
    url += email ? ('&emailId=' + email) : '';
    fetch(url).then(response => response.json())
        .then(r => showAnswer(r));
}