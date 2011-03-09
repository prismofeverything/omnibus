var spasmosis = function() {
    function spasmChannel() {
        return Math.floor(Math.random() * 255);
    }

    function spasmColor() {
        return 'rgb('+[spasmChannel(), spasmChannel(), spasmChannel()].join(',')+')';
    }

    return {
        spasmate: function() {
            setInterval(function() {
                $('body').css('background-color', spasmColor());
            }, 50);
        }
    };
}();

