// Simulate a logging library

// Simulate a location service
var getUserLocation = function (onSuccess, onError) {
    // Bust the call stack so Safari doesn't choke.
    setTimeout(function () {
        navigator.geolocation.getCurrentPosition(onSuccess, onError);
    });
};

// Simulate an app.
console.log('getting your location');
getUserLocation(function(position) {
    console.log('you are here: ' + position.coords.latitude + ', ' + position.coords.longitude);
}, function (err) {
    console.log('oh noes! ' + err)
});

