var omniomnibus = function() {
    var bus = {
        floaters: []
    };

    var floating = linkage.type({
        init: function(id) {
            this.id = id;

            var dim = Math.min($(window).width(), $(window).height());
            this.x = ($(window).width() * 0.5) + ((Math.random() * dim) - (dim * 0.5));
            this.y = ($(window).height() * 0.5) + ((Math.random() * dim) - (dim * 0.5));
        },

        sync: function() {
            $('#' + this.id)
                .css('position', 'relative')
                .css('left', this.x)
                .css('top', this.y);
            return this;
        },

        position: function(position) {
            this.x = position[0];
            this.y = position[1];
            this.sync();

            return this;
        },

        nudge: function(pos) {
            return this.position([this.x + pos[0], this.y + pos[1]]);
        },

        to: function(other) {
            return [other.x - this.x, other.y - this.y];
        },

        orientation: function() {
            var sum = [0, 0], to;
            for (var i = 0; i < bus.floaters.length; i++) {
                to = this.to(bus.floaters[i]);
                sum = [sum[0] + to[0], sum[1] + to[1]];
            }
    
            return sum;
        },

        veering: function(scale) {
            var orient = this.orientation();

            if (this.id === 'omnibus') {
                return [orient[0] * scale, orient[1] * scale];
            } else {
                return [-orient[1] * scale, orient[0] * scale];
            }
        }
    });

    bus.attach = function() {
        bus.floaters = $.map($('.floating'), function(floater) {
            return floating(floater.id).sync();
        });

        setInterval(function() {bus.veer()}, 50);
    };

    bus.veer = function() {
        $(bus.floaters).each(function() {
            var veering = this.veering(0.01);
            this.nudge(veering);
        });
    };

    bus.undefine = function() {
        $('#define').hide();
        $('#undefined-link')
            .html('U N D E F I N E D')
            .removeClass('pushed')
            .click(bus.define);
    };

    bus.define = function() {
        $('#define').show();
        $('#undefined-link')
            .html('D E F I N E')
            .addClass('pushed')
            .click(bus.undefine);
    };

    bus.submitDefinition = function() {
        $('#definition').submit();
    };

    return bus;
}();

