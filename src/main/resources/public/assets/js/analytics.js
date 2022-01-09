google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {

    fetch('/progress/info')
        .then(response => response.json())
        .then(data => {
            console.log(data.data)
            var data = google.visualization.arrayToDataTable(data.data);

            var options = {
                title: 'Progress card',
                vAxis: {title: 'Time taken In mins'},
                areaOpacity: 0,
                isStacked: false
            };

            var chart = new google.visualization.SteppedAreaChart(document.getElementById('chart_div'));

            chart.draw(data, options);
        })
}
