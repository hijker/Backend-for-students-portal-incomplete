$.getJSON('/config/alladmins', function (admins) {
    document.getElementById("admins").innerHTML = '';
    for (var i = 0; i < admins.length; i++) {
        var user = admins[i];
        var pic = "<td width=\"10%\"><img src=" + user.picture + " alt=" + user.name + " style=\"width:50px;height:50px;border-radius:50%\">";
        var name = "<td width=\"20%\" style=\"padding-top: 20px\">" + user.name + "</td>";
        var email = "<td width=\"20%\" style=\"padding-top: 20px\">" + user.email + "</td>";
        var table = "<tr>" + pic + name + email + "</tr>"
        document.getElementById("admins").innerHTML += table;
    }
});

const getSettings = () => {
    $.getJSON('/config/getsettings', function (setting) {
        $('#clariOnly').text(setting.clariOnly);
        $('#startTime').text(setting.startTime);
        $('#endTime').text(setting.endTime);
        $('#threshold').text(setting.threshold);
    });
}
const addAdmin = () => {
    const url = '/config/addadmin?email=' + $('#add-admin-email').val()
    $('#add-admin-email').val('')
    fetch(url, {method: 'POST'})
}

const removeAdmin = () => {
    const url = '/config/removeadmin?email=' + $('#remove-admin-email').val();
    $('#remove-admin-email').val('');
    fetch(url, {method: 'POST'});
}

const updateSetting = () => {
    const url = '/config/postsettings?clariOnly=' + $('#clariOnlyInput').val()
        + '&startTime=' + $('#startTimeInput').val() + '&endTime=' + $('#endTimeInput').val() + '&threshold=' + $('#thresholdInput').val()
    fetch(url, {method: 'POST'}).then(() => {
        getSettings();
    });


}

getSettings();
