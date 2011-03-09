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
                if (Math.random() < 0.1) {
                    $('#spasmosis').html('<h1>!!! SPASMOSIS !!!</h1>');
                } else {
                    $('#spasmosis').html('');
                }
            }, 50);
        }
    };
}();

